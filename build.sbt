lazy val root = (project in file(".")).enablePlugins(PlayScala)

libraryDependencies += "net.ruippeixotog" %% "scala-scraper" % "2.0.0"
libraryDependencies += "org.scalactic" %% "scalactic" % "3.0.4"
libraryDependencies += "org.scalatest" %% "scalatest" % "3.0.4" % "test"

libraryDependencies ++= Seq(
  "com.typesafe.slick" %% "slick" % "3.2.1",
  "org.slf4j" % "slf4j-nop" % "1.6.4",
  "com.typesafe.slick" %% "slick-hikaricp" % "3.2.1"
)

libraryDependencies += guice
libraryDependencies += "postgresql" % "postgresql" % "9.1-901-1.jdbc4"
libraryDependencies += "com.typesafe.play" %% "play" % "2.6.7"

EclipseKeys.preTasks := Seq(compile in Compile, compile in Test)
