package org.overbeck.anagram

import utest._

object AnagrammerTest extends TestSuite {
  val tests = Tests {
    val abcMatches = Anagrammer.matches("abc")
    assert(Seq("cab", "ab", "ba") == abcMatches)

    val catalogMatchesLength = Anagrammer.matches("catalog").map(_.size)
    assert(catalogMatchesLength == 60)
  }

}
