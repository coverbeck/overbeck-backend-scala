import sbt.Keys.libraryDependencies

resolvers +=
  "Sonatype OSS Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots"

lazy val root = (project in file("."))
  .enablePlugins(ScalaTsiPlugin)
  .settings(
    typescriptExports := Seq("org.overbeck.uselections.ElectionAnalyzer.USHouseSummary",
      "org.overbeck.uselections.ElectionAnalyzer.USHouseDataSlim",
      "org.overbeck.weather.Weather.WeatherData",
      "org.overbeck.scwater.LochLomondData"
    ),
    typescriptOutputFile := baseDirectory.value / "model.ts"
  )
name := "overbeck-backend"

version := "0.0.1"

scalaVersion := "2.13.4"

libraryDependencies ++= Seq(
  "com.lihaoyi" %% "cask" % "0.7.4",
  "com.lihaoyi" %% "utest" % "0.7.1" % "test",
  "org.jsoup" % "jsoup" % "1.13.1",
  "com.lihaoyi" %% "requests" % "0.6.5",
  "com.lihaoyi" %% "upickle" % "0.9.5",
  "org.overbeck" %% "ambient-weather-scala" % "0.0.1",
  "org.postgresql" % "postgresql" % "42.2.16"
)
testFrameworks += new TestFramework("utest.runner.Framework")

assemblyJarName := "overbeck-backend.jar"

mainClass in (Compile, packageBin) := Some("app.MinimalRoutesMain")

