package org.overbeck.scraper
import cask.Response
import ujson.Value

import scala.jdk.CollectionConverters._

case class ScraperRoutes()(implicit val log: cask.Logger) extends cask.Routes {

  val APIKEY_NAME: String = "APIKEY"

  private lazy val APIKEY: Option[String] = System.getenv().asScala.get(APIKEY_NAME)
  private val TEXT_PLAIN_HEADER = Seq("Content-Type" -> "text/plain")

  @cask.getJson("/portal")
  def availableScrapes(apikey: String = ""): Response[Value] = {
    if (!isAuthorized(apikey)) cask.Response("Unauthorized", 401)
    else cask.Response(Scraper.availableScrapes, 200, Seq("Content-Type" -> "application/json"))
  }


  @cask.decorators.compress
  @cask.get("/portal/:id")
  def item(id: Int, apikey: String = ""): Response[Array[Byte]] = {
    if (!isAuthorized(apikey)) cask.Response("Unauthorized".getBytes, 403, TEXT_PLAIN_HEADER)
    Scraper.item(id) match {
      case Some(url) => {
        val item = requests.get(url)
        cask.Response(item.bytes, item.statusCode, headers(item))
      }
      case None => cask.Response("Not found".getBytes, 404, TEXT_PLAIN_HEADER)
    }
  }

  private def headers(item: requests.Response) = {
    Seq("content-type", "content-disposition", "content-transfer-encoding").flatMap(headerName => {
      item.headers.get(headerName).getOrElse(Seq.empty[String]).map(s => (headerName -> s))
    })
  }

  private def isAuthorized(apikey: String) = {
    APIKEY == Some(apikey)
  }

  initialize();

}
