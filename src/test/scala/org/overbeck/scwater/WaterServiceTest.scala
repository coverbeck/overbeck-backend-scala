package org.overbeck.scwater

import utest.asserts.Asserts
import utest.{TestSuite, Tests}

object WaterServiceTest extends TestSuite {

  val tests = Tests {
    val waterData = WaterService.latestWaterData
    assert(waterData.recordingDate != None)
    print(waterData)
  }

}
