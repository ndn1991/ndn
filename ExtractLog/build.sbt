val copyOutPath             = SettingKey[File]("Where to copy all libs and built artifact")
val copyAllLibsAndArtifact  = TaskKey[Unit]("vin-pack", "Copy runtime dependencies and built artifact to 'lib'")

name := "ExtractLog"

version := "1.0"

scalaVersion := "2.11.7"

libraryDependencies += "mysql" % "mysql-connector-java" % "5.1.38" % Compile

libraryDependencies += "org.slf4j" % "slf4j-api" % "1.7.13" % Compile

libraryDependencies += "log4j" % "log4j" % "1.2.17" % Compile

libraryDependencies += "com.jcraft" % "jsch" % "0.1.53" % Compile

libraryDependencies += "com.github.ancane" % "hashids-scala_2.11" % "1.2" % Compile

libraryDependencies += "com.typesafe" % "config" % "1.3.0" % Compile

copyOutPath := baseDirectory.value / "dist"
copyAllLibsAndArtifact := {
  val allLibs: List[File] = dependencyClasspath.in(Compile).value.map(_.data).filter(_.isFile).toList
  val buildArtifact: File = packageBin.in(Compile).value
  val jars: List[File] = buildArtifact :: allLibs
  val mappings:   List[(File, File)]  = jars.map(f => (f, copyOutPath.value / f.getName))
  val log                                         = streams.value.log
  log.info(s"Copying to ${copyOutPath.value}:")
  log.info(s"  ${mappings.map(_._1).mkString("\n")}")
  IO.copy(mappings)

  IO.copyDirectory(baseDirectory.value / "src" / "main" / "resources", copyOutPath.value / "resources", true)
  IO.copyFile(baseDirectory.value / "sbin" / "run.sh", copyOutPath.value / "run.sh")
}