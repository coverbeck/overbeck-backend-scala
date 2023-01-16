package org.overbeck.scwater

import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.select.Elements

import java.time.LocalDate

object WaterService {

  private val readingDatePattern = "ending on\\s+(\\S+)".r
  private val dateOnPagePattern = "(\\d{1,2})/(\\d{1,2})/(\\d{4})".r
  def latestWaterData(): LochLomondData = {
    val connection = Jsoup.connect("https://www.cityofsantacruz.com/government/city-departments/water/weekly-water-conditions")
    val document = connection.get()
    LochLomondData(readingDate(document.select("strong:contains(ending on)")).getOrElse(""), percentOfCapacity(document).getOrElse(null), Option.empty)

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
