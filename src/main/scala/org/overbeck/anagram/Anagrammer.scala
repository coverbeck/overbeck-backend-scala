package org.overbeck.anagram

import scala.io.Source
import scala.util.{Failure, Success, Try}

object Anagrammer {
  private val dictionary = Source.fromResource("enable.txt")
    .getLines()
    .toSeq
    .groupBy(_.sorted)

  private def matches(input: String, dictionary: Map[String, Seq[String]]): Try[Seq[String]] = {
    input.length match {
      case i @ _ if i < 2 => Failure(new AnagrammerException("At least two characters are required"))
      case i @ _ if i > 10 => Failure(new AnagrammerException("A maximum of 10 characters is allowed"))
      case _ =>
    }
    val results: Seq[String] = dictionary.get(input.toSeq.sorted.unwrap).getOrElse(Seq.empty)
    val response = results ++ (for (i <- input.length - 1 to 2 by -1)
      yield input.toSeq.combinations(i).map(s => dictionary.get(s.toSeq.sorted.unwrap)).flatten.flatten.toSeq).flatten
    Success(response)
  }

  def matches(input: String): Try[Seq[String]] = matches(input, dictionary)

}

class AnagrammerException(message: String) extends Exception(message);
