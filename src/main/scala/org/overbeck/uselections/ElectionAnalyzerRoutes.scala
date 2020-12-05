package org.overbeck.uselections

import cask.Response
import ujson.Value

case class ElectionAnalyzerRoutes() (implicit val log: cask.Logger) extends cask.Routes {

  @cask.getJson("/elections/house")
  def houseData() = {
    cask.Response(upickle.default.writeJs(ElectionAnalyzer.houseData()), 200, Seq("Content-Type" -> "application/json"))
  }

  initialize()
}
