package org.overbeck.anagram

import cask.model.Response
import ujson.{Arr, Obj, Value}

case class AnagramRoutes()(implicit val log: cask.Logger) extends cask.Routes {
  @cask.postJson("/anagram")
  def hello(input: Value): Response[Value] = {
    try {
      val hits = Anagrammer.matches(input.str)
      cask.Response(hits)
    } catch {
      case ex: AnagrammerException => cask.Response(Obj("message" -> ex.getMessage), 400)
    }
  }

  initialize()

}
