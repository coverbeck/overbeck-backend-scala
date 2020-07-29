package org.overbeck.scraper
import cask.Response
import cask.util.Logger
import ujson.{Obj, Value, Arr}
import scala.jdk.CollectionConverters._

case class ScraperRoutes()(implicit val log: cask.Logger) extends cask.Routes {

  val APIKEY_NAME: String = "APIKEY"

  private lazy val APIKEY: Option[String] = System.getenv().asScala.get(APIKEY_NAME)

  @cask.getJson("/portal")
  def availableScrapes(apikey: String = ""): Response[Value] = {
    if (isAuthorized(apikey)) cask.Response("Unauthorized", 401)
    else cask.Response(Scraper.availableScrapes, 200)
  }

//  @cask.get("/portal/:item")
//  def item(item: String): Response = {
//    cask.Response()
//  }

  private def isAuthorized(apikey: String) = {
    APIKEY != Some(apikey)
  }

  initialize();

}
