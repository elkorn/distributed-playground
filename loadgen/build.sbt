name := "loadgen"

version := "1.0"

enablePlugins(GatlingPlugin)

scalacOptions := Seq(
  "-encoding", "UTF-8", "-target:jvm-1.8", "-deprecation",
  "-feature", "-unchecked", "-language:implicitConversions", "-language:postfixOps")

scalaVersion := "2.11.8"

libraryDependencies ++= Seq(
  "io.gatling.highcharts" % "gatling-charts-highcharts" % "2.2.2" % "test" ,
  "io.gatling"            % "gatling-test-framework"    % "2.2.2" % "test"
)
