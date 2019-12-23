package app

import scala.io.Source
import ujson._

object Application extends cask.MainRoutes {

  // Won't work in Docker otherwise
  override def host: String = "0.0.0.0"

  private val dictionary = Source.fromResource("enable.txt")
    .getLines()
    .toSeq
    .groupBy(_.sorted)

  def matches(input: String, dictionary: Map[String, Seq[String]]): Seq[String] = {
    val found: Option[Seq[String]] = dictionary.get(input.toSeq.sorted.unwrap)
    val results: Seq[String] = found.map(s => s).getOrElse(Seq.empty)
    results ++ (for (i <- input.length - 1 to 2 by -1)
      yield input.toSeq.combinations(i).map(s => dictionary.get(s.toSeq.sorted.unwrap)).flatten.flatten.toSeq).flatten
  }

  @cask.postJson("/")
  def hello(input: Value) = {
    val hits = matches(input.str, dictionary)
    Obj (
      "hits" -> hits
    )
  }

  initialize()
}
