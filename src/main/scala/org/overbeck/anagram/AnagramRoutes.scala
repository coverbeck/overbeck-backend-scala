package org.overbeck.anagram

import cask.model.Response
import ujson.{Obj, Value}

import scala.util.{Failure, Success}

case class AnagramRoutes()(implicit val log: cask.Logger) extends cask.Routes {
  @cask.postJson("/anagram")
  def findAnagrams(input: Value): Response[Value] = {
      val matches = Anagrammer.matches(input.str)
      matches match {
        case Success(strs) => cask.Response(strs, 200, Seq(("Content-type", "application/json")))
        case Failure(ex) => {
          log.exception(ex);
          cask.Response(Obj("message" -> ex.getMessage), 400)
        }
      }
  }

  initialize()

}
