package org.overbeck.anagram

import cask.model.Response
import ujson.{Arr, Obj, Value}

case class AnagramRoutes()(implicit val log: cask.Logger) extends cask.Routes {
  @cask.postJson("/anagram")
  def findAnagrams(input: Value): Response[Value] = {
    try {
      cask.Response(Anagrammer.matches(input.str))
    } catch {
      case ex: AnagrammerException => {
        log.exception(ex);
        cask.Response(Obj("message" -> ex.getMessage), 400)
      }
    }
  }

  initialize()

}
