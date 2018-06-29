/*
 *
 * Make.org Main Front
 * Copyright (C) 2018 Make.org
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 *
 */

import com.typesafe.sbt.packager.docker.{Cmd, ExecCmd}

enablePlugins(DockerPlugin)

dockerRepository := Some("nexus.prod.makeorg.tech")
packageName in Docker := "make-front"

val appContentDirectory = "/usr/src/app/front"
val nginxEnvParams = "/etc/nginx/env_params"
val nginxPerlModule = "/etc/nginx/modules/ngx_http_perl_module.so"

dockerCommands := Seq(
  Cmd("FROM", "nexus.prod.makeorg.tech/front-runner:master-latest"),
  Cmd("MAINTAINER", "technical2@make.org"),
  Cmd("ENV", "API_URL", "https://api.prod.makeorg.tech"),
  Cmd("ENV", "PORT", "80"),
  Cmd("COPY", "dist", appContentDirectory),
  ExecCmd("RUN", "chmod", "-R", "+rw", appContentDirectory)
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
