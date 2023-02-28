package org.overbeck.scwater

import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.select.Elements

import java.sql.{DriverManager, ResultSet, SQLException}
import java.time.LocalDate
import scala.util.{Failure, Success, Try, Using}

object WaterService {

  private val readingDatePattern = "ending on\\s+(\\S+)".r
  private val dateOnPagePattern = "(\\d{1,2})/(\\d{1,2})/(\\d{4})".r

  def latestWaterData: LochLomondData = {
    val connection = Jsoup.connect("https://www.cityofsantacruz.com/government/city-departments/water/weekly-water-conditions")
    val document = connection.get()
    LochLomondData(readingDate(document.select("strong:contains(ending on)")).getOrElse(""), percentOfCapacity(document).orNull, Option.empty)
  }

  def existingData(): List[LochLomondData] = {
    Using.resource(dbConnection) { connection =>
      {
        val statement = connection.createStatement()
        val resultSet = statement.executeQuery("SELECT recording_date, percent_full, created_timestamp from loch_lomond order by recording_date")
        val data: Seq[LochLomondData] = new Iterator[ResultSet] {
          override def hasNext: Boolean = resultSet.next

          override def next(): ResultSet = resultSet
        }.to(LazyList).map(rs => LochLomondData(
          rs.getString("recording_date"),
          rs.getBigDecimal("percent_full"),
          Option(rs.getString("created_timestamp"))
        ))
        val dataList = data.toList
        resultSet.close
        dataList
      }
    }
  }

  private def dbConnection = {
    val dbUrl = "jdbc:postgresql://postgres_db/overbeck"
    DriverManager.getConnection(dbUrl, "overbeck", null)
  }

  def updateIfNecessary(): Boolean = {
    val webSiteLatest = latestWaterData
    val dbData = existingData()
    val lastInDb = dbData.last
    if (webSiteLatest.recordingDate > lastInDb.recordingDate) {
      println("About to update database")
      Using.resource(dbConnection) {
        connection => {
          println("Got the connection")
          val statement = connection.prepareStatement("insert into loch_lomond (recording_date, percent_full) values(?, ?)")
          statement.setDate(1, java.sql.Date.valueOf(LochLomondData.localDate(webSiteLatest.recordingDate)))
          statement.setBigDecimal(2, webSiteLatest.percentFull.bigDecimal)
          val update = Try {
            statement.executeUpdate
            statement.close
            true
          }
          update match {
            case Success(value) => value
            case Failure(ex) => {
              println("Error updating DB: " + ex)
              ex.printStackTrace()
              false
            }
          }
        }
      }
    } else false
  }

  def readingDate(elements: Elements) = {
    if (elements.size() == 1) {
      readingDatePattern.findFirstMatchIn(elements.text)
        .map(_.group(1))
        .map(m => {
          val option = dateOnPagePattern.findFirstMatchIn(m)
          option.map(m => {
            val year = m.group(3).toInt
            val month = m.group(1).toInt
            val day = m.group(2).toInt
            LocalDate.of(year, month, day).toString
          })
        })
        .flatten
    } else None
  }

  def percentOfCapacity(document: Document): Option[BigDecimal] = {
    val elements = document.select("td:contains(Percent of capacity)")
    if (elements.size == 1) {
      val percentStr = elements.next().text()
      // Remove the percentage sign
      Some(BigDecimal(percentStr.substring(0, percentStr.length - 1)))
    } else None
  }
}
