package org.overbeck.uselections

import cask.Response
import ujson.Value

case class ElectionAnalyzerRoutes() (implicit val log: cask.Logger) extends cask.Routes {

  @cask.getJson("/elections/house")
  def houseData(summary: Boolean = false) = {
    if (summary) cask.Response(upickle.default.writeJs(ElectionAnalyzer.houseSummaryData()), 200, Seq("Content-Type" -> "application/json"))
    else cask.Response(upickle.default.writeJs(ElectionAnalyzer.houseData()), 200, Seq("Content-Type" -> "application/json"))
  }

  initialize()
}
