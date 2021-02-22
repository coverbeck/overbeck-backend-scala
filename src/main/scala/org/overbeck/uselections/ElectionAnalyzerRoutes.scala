package org.overbeck.uselections

case class ElectionAnalyzerRoutes() (implicit val log: cask.Logger) extends cask.Routes {

  @cask.getJson("/elections/house")
  def houseData(summary: Boolean = false) = {
    val contentTypeJson = "Content-Type" -> "application/json"
    if (summary) cask.Response(upickle.default.writeJs(ElectionAnalyzer.houseSummaryData()), 200, Seq(contentTypeJson))
    else cask.Response(upickle.default.writeJs(ElectionAnalyzer.houseData()), 200, Seq(contentTypeJson))
  }

  initialize()
}
