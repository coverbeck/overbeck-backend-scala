package org.overbeck.weather

object Weather {
  private val ambientWeather = new AmbientWeather(
      System.getenv("AMBIENT_APP_KEY"), System.getenv("AMBIENT_API_KEY"))

  def currentWeather() =
    ambientWeather.devices.map(
      devices => devices.map(device => {
        val lastData = device.lastData
        WeatherData(lastData.tempf,
          lastData.tempinf,
          lastData.winddir,
          lastData.windspeedmph,
          lastData.hourlyrainin,
          lastData.eventrainin,
          lastData.dailyrainin,
          lastData.date)
      }))

  case class WeatherData(outdoorTemp: Float,
                         indoorTemp: Float,
                         windDirection: Int,
                         windSpeedMph: Float,
                         hourlyRain: Float,
                         eventRain: Float,
                         dailyRain: Float,
                         date: String)

  object WeatherData {
    implicit def weatherRS: upickle.default.ReadWriter[WeatherData] = upickle.default.macroRW[WeatherData]
  }
}
