package org.overbeck.scwater

import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.select.Elements

object WaterService {

  private val readingDatePattern = "ending on\\s+(\\S+)".r
  private val dateOnPagePattern = "(\\d\\d)/(\\d\\d)/(\\d\\d\\d\\d)".r
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
          option.map(m => s"${m.group(3)}-${m.group(1)}-${m.group(2)}")
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
