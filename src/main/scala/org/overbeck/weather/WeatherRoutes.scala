package org.overbeck.weather

import cask.model.Response
import ujson.Value

import scala.util.{Failure, Success}

case class WeatherRoutes() (implicit val log: cask.Logger) extends cask.Routes {

  @cask.getJson("/weather")
  def weather(): Response[Value] = {
    val appJson = Seq("Content-Type" -> "application/json")
    Weather.currentWeather() match {
      case Success(devices) =>
        cask.Response(upickle.default.writeJs(devices(0)), 200, appJson) // I only have one device
      case Failure(ex) =>
        cask.Response(ujson.Obj("message" -> ex.getMessage), 500, appJson)
    }
  }

  initialize()
}
