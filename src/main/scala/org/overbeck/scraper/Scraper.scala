package org.overbeck.scraper

import java.time.{ZoneId, ZonedDateTime}

import org.jsoup.Jsoup

import scala.io.Source
import scala.jdk.CollectionConverters._


object Scraper {

  implicit val ScraperDataRW = upickle.default.macroRW[ScraperData]
  private val rawScraperData: String = readScrapeConfig()

  def availableScrapes() = {
    upickle.default.writeJs(scraperData(rawScraperData))
  }

  def item(item: Int) = {
    scraperData(rawScraperData).find(s => s.id == item ) match {
      case Some(s) => {
        val doc = Jsoup.connect(s.url).get()
        val elements = doc.select(s.selector)
        if (elements.isEmpty) None
        else {
          if (s.attribute.startsWith("data-")) elements.get(0).attributes().dataset().asScala.get(s.attribute.substring(5))
          else Some(elements.get(0).attributes().get(s.attribute))
        }
      }
      case None => None
    }
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
