package org.overbeck.anagram

import cask.model.{FormValue, Response}
import ujson.{Obj, Value}

case class AnagramRoutes()(implicit val log: cask.Logger) extends cask.Routes {
  @cask.postForm("/anagram")
  def findAnagrams(input: FormValue): Response[Value] = {
    try {
      cask.Response(Anagrammer.matches(input.value))
    } catch {
      case ex: AnagrammerException => {
        log.exception(ex);
        cask.Response(Obj("message" -> ex.getMessage), 400)
      }
    }
  }

  initialize()

}
