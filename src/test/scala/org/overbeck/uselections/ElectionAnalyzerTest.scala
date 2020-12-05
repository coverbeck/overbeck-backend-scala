package org.overbeck.uselections

import utest.{ArrowAssert, TestSuite, Tests}

object ElectionAnalyzerTest extends TestSuite {

  val tests = Tests {
    val data = ElectionAnalyzer.houseData
    val zerovotes = data.filter(_.winningVotes == 0)
    //    assert(zerovotes.isEmpty)
  }

}
