package org.overbeck.weather

import scala.util.{Failure, Success}

case class WeatherRoutes() (implicit val log: cask.Logger) extends cask.Routes {

  @cask.getJson("/weather")
  def weather() = {
    Weather.currentWeather() match {
      case Success(devices) => cask.Response(devices.mkString, 200, Seq("Content-Type" -> "text/plain"))
      case Failure(ex) => cask.Response(ex.getMessage, 500, Seq("Content-Type" -> "text/plain"))
    }
  }

  initialize()
}
