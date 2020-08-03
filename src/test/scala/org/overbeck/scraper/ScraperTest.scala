package org.overbeck.scraper

import utest.{TestSuite, Tests}

object ScraperTest extends TestSuite {

  val tests = Tests {
    val maybeString = Scraper.item(1)
    print(maybeString)
  }

}
