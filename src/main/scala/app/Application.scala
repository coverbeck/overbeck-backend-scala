package app

import org.overbeck.anagram.AnagramRoutes
import org.overbeck.scraper.ScraperRoutes
import org.overbeck.uselections.ElectionAnalyzerRoutes
import org.overbeck.weather.WeatherRoutes

object MinimalRoutesMain extends cask.Main {
  // Won't work in Docker otherwise
  override def host: String = "0.0.0.0"
  val allRoutes = Seq(AnagramRoutes(), ScraperRoutes(), ElectionAnalyzerRoutes(), WeatherRoutes())

}
