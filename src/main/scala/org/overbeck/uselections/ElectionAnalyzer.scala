package org.overbeck.uselections

import scala.io.Source
import scala.util.Using

object ElectionAnalyzer {


  val data = Using(Source.fromResource("1976-2018-house2.tab")) { reader => {
    reader.getLines
      .drop(1) // TSV Header
      .map(line => line.replaceAllLiterally("\"", ""))
      .map(line => {
        val elems = line.split("\t")
        USHouseData(elems(0).toInt, elems(1), elems(2), elems(3).toInt, elems(4).toInt,
          elems(5).toInt, elems(6), elems(7).toInt, elems(8), elems(9), elems(10).toBoolean, elems(11), elems(12),
          elems(13).toBoolean, elems(14), elems(15).toInt, elems(16).toInt, elems(17).toBoolean, elems(18).toInt)
      }).toVector
  }
  }

  def houseData() = data.get
    .groupBy(r => r.year + r.state_po + r.district) // Map[String, Vector[USHouseData]]
    .view
    .mapValues((vector: Vector[USHouseData]) => {
      val firstValue = vector.head
      val year = firstValue.year
      val state = firstValue.state_po
      val district = firstValue.district
      val total = firstValue.totalvotes
      // There can be more than one candidate per party
      val democrat = vector.filter(_.party == "democrat").map(_.candidatevotes).sum
      val republic = vector.filter(_.party == "republican").map(_.candidatevotes).sum
      val other = vector.filter(r => r.party != "democrat" && r.party != "republican").map(_.candidatevotes).sum
      val winner = vector.maxBy(_.candidatevotes)
      USHouseDataSlim(year, state, district, democrat, republic, other, total, winner.party, winner.candidatevotes, winner.candidate)
    })
    .values
    .toVector
    .sortBy(r => r.year + r.state + r.district)

  case class USHouseData(year: Int, state: String, state_po: String, state_fips: Int, state_cen: Int,
                         state_ic: Int, office: String, district: Int, stage: String, runoff: String, special: Boolean,
                         candidate: String, party: String, writein: Boolean, mode: String, candidatevotes: Int, totalvotes: Int,
                         unofficial: Boolean, version: Int)

  case class USHouseDataSlim(year: Int, state: String, district: Int, democrat: Int, republican: Int, other: Int, total: Int,
                             winningParty: String, winningVotes: Int, winningCandidate: String)

  object USHouseDataSlim {
    implicit def todoRW: upickle.default.ReadWriter[USHouseDataSlim] = upickle.default.macroRW[USHouseDataSlim]
  }
}
