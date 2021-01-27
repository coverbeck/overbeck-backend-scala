package org.overbeck.weather

object Weather {
  private val ambientWeather = new AmbientWeather(
      System.getenv("AMBIENT_APP_KEY"), System.getenv("AMBIENT_API_KEY"))

  def currentWeather() = ambientWeather.devices
}
