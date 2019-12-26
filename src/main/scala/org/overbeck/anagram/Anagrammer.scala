package org.overbeck.anagram

import scala.io.Source

object Anagrammer {
  private val dictionary = Source.fromResource("enable.txt")
    .getLines()
    .toSeq
    .groupBy(_.sorted)

  @throws(classOf[AnagrammerException])
  private def matches(input: String, dictionary: Map[String, Seq[String]]): Seq[String] = {
    input.length match {
      case i @ _ if i < 2 => throw new AnagrammerException("At least two characters are required")
      case i @ _ if i > 10 => throw new AnagrammerException("A maximum of 10 characters is allowed")
      case _ =>
    }
    val found: Option[Seq[String]] = dictionary.get(input.toSeq.sorted.unwrap)
    val results: Seq[String] = found.map(s => s).getOrElse(Seq.empty)
    results ++ (for (i <- input.length - 1 to 2 by -1)
      yield input.toSeq.combinations(i).map(s => dictionary.get(s.toSeq.sorted.unwrap)).flatten.flatten.toSeq).flatten
  }

  @throws(classOf[AnagrammerException])
  def matches(input: String): Seq[String] = matches(input, dictionary)

}

class AnagrammerException(message: String) extends Exception(message);
