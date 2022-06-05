package org.overbeck.scwater

case class WaterData(readingDate: Option[String],
                     reservoirPercentFull: Option[Float],
                     averageDailyProduction: Option[Float])
