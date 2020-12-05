package org.overbeck.anagram

import utest._

object AnagrammerTest extends TestSuite {
  val tests = Tests {
    val abcMatches = Anagrammer.matches("abc").get
    assert(Seq("cab", "ab", "ba") == abcMatches)

    val catalogMatchesLength = Anagrammer.matches("catalog").map(_.size).get
    assert(catalogMatchesLength == 60)
  }

}
