package org.overbeck.scwater

import cask.model.Response
import ujson.Value

import java.sql.{DriverManager, ResultSet}
import scala.util.Using

case class WaterRoutes() (implicit val log: cask.Logger) extends cask.Routes {

  @cask.getJson("/lochlomond")
  def lochLomondReservoirLevels(): Response[Value] = {
    val appJson = Seq("Content-Type" -> "application/json")
    val url = "jdbc:postgresql://localhost/overbeck"
    Using.resource(DriverManager.getConnection(url, "overbeck", null)) { connection => {
      val statement = connection.createStatement()
      val resultSet = statement.executeQuery("SELECT recording_date, percent_full, created_timestamp  from loch_lomond order by recording_date")
      val data: Seq[LochLomondData] = new Iterator[ResultSet] {
        override def hasNext: Boolean = resultSet.next

        override def next(): ResultSet = resultSet
      }.to(LazyList).map(rs => LochLomondData(
        rs.getString("recording_date"),
        rs.getBigDecimal("percent_full"),
        rs.getString("created_timestamp")
      ))
      cask.Response(upickle.default.writeJs(data), 200, appJson)
    }}
  }

  initialize()
}

object WaterRoutes {
  Class.forName("org.postgresql.Driver")
}

case class LochLomondData(
                           recordingDate: String,
                           percentFull: BigDecimal,
                           createdDate: String
                         )
object LochLomondData {
  implicit def lochLomondRS: upickle.default.ReadWriter[LochLomondData] = upickle.default.macroRW[LochLomondData]
}