package org.overbeck.scwater

import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.select.Elements

object WaterService {

  private val readingDatePattern = "ending on\\s+(\\S+)".r
  def latestWaterData(): WaterData = {
    val connection = Jsoup.connect("https://www.cityofsantacruz.com/government/city-departments/water/weekly-water-conditions")
    val document = connection.get()
    WaterData(readingDate(document.select("strong:contains(ending on)")), percentOfCapacity(document), None)

  }

  def readingDate(elements: Elements) = {
    if (elements.size() == 1) {
      readingDatePattern.findFirstMatchIn(elements.text)
        .map(_.group(1))
    } else None
  }

  def percentOfCapacity(document: Document): Option[Float] = {
    val elements = document.select("td:contains(Percent of capacity)")
    if (elements.size == 1) {
      val percentStr = elements.next().text()
      // Remove the percentage sign
      percentStr.substring(0, percentStr.length - 1).toFloatOption
    } else None
  }
}
