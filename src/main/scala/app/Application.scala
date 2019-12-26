package app

import org.overbeck.anagram.{Anagrammer, AnagrammerException}
import ujson._

object Application extends cask.MainRoutes {

  // Won't work in Docker otherwise
  override def host: String = "0.0.0.0"

  @cask.postJson("/")
  def hello(input: Value) = {
    try {
      val hits = Anagrammer.matches(input.str)
      cask.Response(Obj(
        "hits" -> hits
      ))
    } catch {
      case ex: AnagrammerException => cask.Response(Obj("message" -> ex.getMessage), 400)
    }
  }

  initialize()
}
