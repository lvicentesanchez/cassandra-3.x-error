import com.typesafe.sbt.SbtScalariform.ScalariformKeys
import scalariform.formatter.preferences._

// Resolvers
resolvers ++= Seq(
  Resolver.sonatypeRepo("snapshots")
)

// Dependencies
val dependencies = Seq(
  "com.datastax.cassandra" % "cassandra-driver-core" % "3.0.0"
)

// Settings
//
val compileSettings = Seq(
  "-deprecation",
  "-encoding", "UTF-8",
  "-feature",
  "-language:_",
  "-unchecked",
  //"-Xfatal-warnings",
  "-Xlint",
  "-Ybackend:GenBCode",
  "-Ydelambdafy:method",
  "-Yno-adapted-args",
  "-Ywarn-dead-code",
  "-Ywarn-numeric-widen",
  "-Ywarn-value-discard",
  "-Xfuture",
  "-Ywarn-unused-import"
)

val forkedJvmOption = Seq(
  "-server",
  "-Dfile.encoding=UTF8",
  "-Duser.timezone=GMT",
  "-Xss1m",
  "-Xms2048m",
  "-Xmx2048m",
  "-XX:+CMSClassUnloadingEnabled",
  "-XX:ReservedCodeCacheSize=256m",
  "-XX:+DoEscapeAnalysis",
  "-XX:+UseConcMarkSweepGC",
  "-XX:+UseParNewGC",
  "-XX:+UseCodeCacheFlushing",
  "-XX:+UseCompressedOops"
)

val pluginsSettings =
  scalariformSettings

val settings = Seq(
  name := "cassandra-3x-error",
  version := "0.1-SNAPSHOT",
  scalaVersion := "2.11.7",
  libraryDependencies ++= dependencies,
  fork in run := true,
  fork in Test := true,
  fork in testOnly := true,
  connectInput in run := true,
  javaOptions in run ++= forkedJvmOption,
  javaOptions in Test ++= forkedJvmOption,
  scalacOptions := compileSettings,
  //unmanagedClasspath in Compile += baseDirectory.value / "src" / "main" / "resources",
  // formatting
  //
  ScalariformKeys.preferences := PreferencesImporterExporter.loadPreferences(( file(".") / "formatter.preferences").getPath)
)

val main =
  project
    .in(file("."))
    .settings(
      pluginsSettings ++ settings:_*
    )
