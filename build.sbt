name := "sjson Sample 1"

version := "0.0.1"

resolvers ++= Seq(
  "FuseSource Snapshot Repository" at "http://repo.fusesource.com/nexus/content/repositories/snapshots"
)

libraryDependencies ++= Seq(
  "net.debasishg" %% "sjson" % "0.15"
)

scalaVersion := "2.9.1"

fork := true

ivyLoggingLevel := UpdateLogging.Full

logLevel := Level.Info
