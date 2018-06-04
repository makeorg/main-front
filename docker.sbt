import com.typesafe.sbt.packager.docker.{Cmd, ExecCmd}

enablePlugins(DockerPlugin)

dockerRepository := Some("nexus.prod.makeorg.tech")
packageName in Docker := "make-front"

val appContentDirectory = "/usr/src/app/front"
val nginxEnvParams = "/etc/nginx/env_params"
val nginxPerlModule = "/etc/nginx/modules/ngx_http_perl_module.so"

dockerCommands := Seq(
  Cmd("FROM", "front-runner:latest"),
  Cmd("MAINTAINER", "technical2@make.org"),
  Cmd("ENV", "API_URL", "https://api.prod.makeorg.tech"),
  Cmd("COPY", "dist", appContentDirectory),
  ExecCmd("RUN", "chmod", "-R", "+rw", appContentDirectory),
  ExecCmd("CMD", "PORT=80 nodemon", "bin/www")
)

val copyDockerResources: TaskKey[String] = taskKey[String]("copy directories")

copyDockerResources := {
  val files = (webpack in (Compile, fullOptJS)).value
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
  "Done"
}

publishLocal := {
  Def.sequential(copyDockerResources, publishLocal in Docker).value
}

publish := {
  Def.sequential(copyDockerResources, publish in Docker).value
}
