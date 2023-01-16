package app

import org.overbeck.anagram.AnagramRoutes
import org.overbeck.scraper.ScraperRoutes
import org.overbeck.scwater.{WaterRoutes, WaterService}
import org.overbeck.uselections.ElectionAnalyzerRoutes
import org.overbeck.weather.WeatherRoutes

import java.util.concurrent.{ScheduledThreadPoolExecutor, TimeUnit}

object MinimalRoutesMain extends cask.Main {
  // Won't work in Docker otherwise
  override def host: String = "0.0.0.0"
  val allRoutes = Seq(AnagramRoutes(), ScraperRoutes(), ElectionAnalyzerRoutes(), WeatherRoutes(), WaterRoutes())
  val ex = new ScheduledThreadPoolExecutor(1)

  override def main(args: Array[String]): Unit = {
    val task = new Runnable {
      def run() = WaterService.updateIfNecessary
    }
    val f = ex.scheduleAtFixedRate(task, 0, 10, TimeUnit.MINUTES)
    super.main(args)
  }


}
