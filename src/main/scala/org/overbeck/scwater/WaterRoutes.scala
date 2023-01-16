package org.overbeck.scwater

import cask.model.Response
import ujson.Value

import java.sql.{DriverManager, ResultSet, Timestamp}
import java.time.LocalDate
import scala.util.Using

case class WaterRoutes() (implicit val log: cask.Logger) extends cask.Routes {

  @cask.getJson("/lochlomond")
  def lochLomondReservoirLevels(): Response[Value] = {
    val appJson = Seq("Content-Type" -> "application/json")
    cask.Response(upickle.default.writeJs(WaterService.existingData()), 200, appJson)
  }

  initialize()
}

object WaterRoutes {
  Class.forName("org.postgresql.Driver")
}

case class LochLomondData(
                           recordingDate: String,
                           percentFull: BigDecimal,
                           createdDate: Option[String]
                         )
object LochLomondData {
  implicit def lochLomondRS: upickle.default.ReadWriter[LochLomondData] = upickle.default.macroRW[LochLomondData]
  def localDate(recordingDate: String): LocalDate = LocalDate.parse(recordingDate)
}