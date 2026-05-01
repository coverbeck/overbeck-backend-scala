package org.overbeck.scraper

import ujson.Value
import utest.{TestSuite, Tests}

object ScraperTest extends TestSuite {

  val tests = Tests {
    val scrapes: Seq[ScraperData] = Scraper.scraperData()
    val maybeData = scrapes.zipWithIndex.map({ case (data, i) => Scraper.item(i) })
    print(maybeData)
  }

}
