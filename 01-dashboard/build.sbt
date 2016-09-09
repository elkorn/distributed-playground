import com.typesafe.sbt.SbtScalariform

lazy val root = (project in file(".")).
  settings(
    organization := "elkorn",
    name := "playground.distributed.dashboard",
    version := "0.0.1-SNAPSHOT",
    scalaVersion := "2.11.8"
  )
  .settings(SbtScalariform.scalariformSettings: _*)

scalacOptions ++= Seq(
  "-unchecked",
  "-deprecation",
  "-feature",
  "-language:postfixOps",
  "-Xfatal-warnings",
  "-Xlint:_")

resolvers ++= Seq(
  "Typesafe Akka @ maven.org" at "https://repo1.maven.org/maven2/com/typesafe/akka/",
  "Typesafe Releases" at "http://repo.typesafe.com/typesafe/maven-releases/"
)

libraryDependencies ++= {
  val akkaV = "2.4.8"
  val scalaTestV = "2.2.6"
  val scalaMockV = "3.2.2"
  Seq(
    "com.typesafe.akka"           %% "akka-actor"                           % akkaV,
    "com.typesafe.akka"           %% "akka-http-experimental"               % akkaV,
    "com.typesafe.akka"           %% "akka-stream"                          % akkaV,
    "com.typesafe"                % "config"                                % "1.3.0"
  )
}

fork in run := true
cancelable in Global := true
