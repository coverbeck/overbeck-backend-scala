package org.overbeck.scraper

import utest.{TestSuite, Tests}

object ScraperTest extends TestSuite {

  val tests = Tests {
    val availableScrapes = Scraper.availableScrapes
    print(availableScrapes)
  }

}
