package org.overbeck.uselections

import utest.{TestSuite, Tests}

object ElectionAnalyzerTest extends TestSuite {

  val tests = Tests {
    val data = ElectionAnalyzer.data
    data.get.foreach(println)
  }

}
