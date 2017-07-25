import com.typesafe.sbt.packager.docker.Cmd

enablePlugins(DockerPlugin)

dockerRepository := Some("nexus.prod.makeorg.tech")
packageName in Docker := "repository/docker-dev/make-front"

val nginxContentDirectory = "/usr/share/nginx/html"

dockerCommands := Seq(
  Cmd("FROM", "nginx:1.13.3"),
  Cmd("MAINTAINER", "technical2@make.org"),
  Cmd("COPY", "dist", nginxContentDirectory),
  Cmd("COPY", "conf/nginx.conf", "/etc/nginx/conf.d/make.conf")
)

val copyDockerResources: TaskKey[Unit] = taskKey[Unit]("copy directories")

copyDockerResources := {
  streams.value.log.info("Copying resources to the docker directory")
  val base = baseDirectory.value
  val dockerDirectory = base / "target" / "docker" / "stage"
  dockerDirectory.mkdirs()
  IO.copyDirectory(base / "src" / "main" / "universal", dockerDirectory, overwrite = true)
  IO.copyDirectory(
    base / "target" / s"scala-${scalaBinaryVersion.value}" / "scalajs-bundler" / "main" / "dist",
    dockerDirectory / "dist",
    overwrite = true
  )
}

publishLocal := {
  copyDockerResources.value
  (webpack in (Compile, fullOptJS)).value
  (publishLocal in Docker).value
}

publish := {
  copyDockerResources.value
  (webpack in (Compile, fullOptJS)).value
  (publish in Docker).value
}
