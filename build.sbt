name := "overbeck-backend"

version := "0.1"

scalaVersion := "2.13.1"

libraryDependencies += "com.lihaoyi" %% "cask" % "0.6.0"

libraryDependencies += "com.lihaoyi" %% "utest" % "0.7.1" % "test"

libraryDependencies += "org.jsoup" % "jsoup" % "1.13.1"

testFrameworks += new TestFramework("utest.runner.Framework")