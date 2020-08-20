package org.overbeck.scraper

import java.time.{ZoneId, ZonedDateTime}

import org.jsoup.Jsoup
import org.jsoup.select.Elements

import scala.io.Source
import scala.jdk.CollectionConverters._

case class FetchImageData(url: String, referer: String)
case class ScrapedData(bytes: Array[Byte], statusCode: Int, headers: Seq[(String, String)])

object Scraper {

  implicit val ScraperDataRW = upickle.default.macroRW[ScraperData]
  private val rawScraperData: String = readScrapeConfig()

  def availableScrapes() = {
    upickle.default.writeJs(scraperData(rawScraperData))
  }

  def item(item: Int): Option[ScrapedData] = {
    scraperData(rawScraperData).find(s => s.id == item ) match {
      case Some(s) => {
        val doc = Jsoup.connect(s.url).get()
        val elements: Elements = doc.select(s.selector)
        if (elements.isEmpty) None
        else {
          if (s.elementType == "text") {
            val text = elements.get(0).text()
            Some(ScrapedData(text.getBytes, 200, Seq(("Content-Type", "text/plain"))))
          } else {
            imageUrl(s, elements) match {
              case Some(url) =>
                val value = requests.get(url, check = false, headers = Seq(("Referer" -> s.url)))
                Some(ScrapedData(value.bytes, value.statusCode, headers(value)))
              case None => None
            }
          }
        }
      }
      case None => None
    }
  }

  private def imageUrl(s: ScraperData, elements: Elements): Option[String] = {
    if (s.attribute.startsWith("data-")) elements.get(0).attributes().dataset().asScala.get(s.attribute.substring(5))
    else Option(elements.get(0).attributes().get(s.attribute))
  }

  private def headers(item: requests.Response) = {
    Seq("content-type", "content-disposition", "content-transfer-encoding").flatMap(headerName => {
      item.headers.get(headerName).getOrElse(Seq.empty[String]).map(s => (headerName -> s))
    })
  }


  def scraperData(rawScraperData: String): Seq[ScraperData] = {
    upickle.default.read[Seq[ScraperData]](processDates(rawScraperData))
  }

  def processDates(input: String): String = {
    val zoneId = ZoneId.of("America/Los_Angeles")
    val zonedDateTime = ZonedDateTime.now(zoneId)
    input.replaceAll("\\$\\{date}", s"${zonedDateTime.getYear}/${zonedDateTime.getMonthValue}/${zonedDateTime.getDayOfMonth}")
  }

  def readScrapeConfig() = {
    System.getenv().asScala.get("SCRAPE_CONFIG") match {
      case Some(file) => Source.fromFile(file).mkString
      case _ => Source.fromResource("scrape.json").mkString
    }
  }

}
