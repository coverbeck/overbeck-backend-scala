name := "anagram-micro-service"

version := "0.1"

scalaVersion := "2.13.1"

libraryDependencies += "com.lihaoyi" %% "cask" % "0.6.0"

libraryDependencies += "com.lihaoyi" %% "utest" % "0.7.1" % "test"

testFrameworks += new TestFramework("utest.runner.Framework")