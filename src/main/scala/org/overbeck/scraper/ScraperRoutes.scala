package org.overbeck.scraper
import cask.Response
import cask.util.Logger
import ujson.{Obj, Value, Arr}
import scala.jdk.CollectionConverters._

case class ScraperRoutes()(implicit val log: cask.Logger) extends cask.Routes {

  val APIKEY_NAME: String = "APIKEY"

  private val APIKEY: Option[String] = System.getenv().asScala.get(APIKEY_NAME)
  @cask.getJson("/portal")
  def availableScrapes(apikey: String = ""): Response[Value] = {
    if (APIKEY != Some(apikey)) cask.Response("Unauthorized", 401)
    else cask.Response((Arr(
      Obj("style" -> "wide", "name" -> "adamathome")
    )), 200)
  }

  initialize();

  /*
      'adam': 'adamathome',
    'calvin': 'calvinandhobbes',
    'duplex': 'duplex',
    'nancy': 'nancy',
    'pooch': 'poochcafe'

   */

}
