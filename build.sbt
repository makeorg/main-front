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

import java.time.{ZoneOffset, ZonedDateTime}

import com.typesafe.sbt.GitVersioning
import com.typesafe.sbt.SbtGit.GitKeys
import sbt.Keys.baseDirectory

organization := "org.make.front"
name := "make-front"
scalaVersion := "2.12.6"

licenses += "GPL-3.0-or-later" -> url("https://www.gnu.org/licenses/gpl.html")

/* Npm versions */
val npmReactVersion = "15.6.1"
val npmWebpackVersion = "4.12.0"
val npmReactRouterVersion = "4.1.2"
val npmReactAutosuggestVersion = "9.3.1"
val npmExtractTextWebpackPluginVersion = "v4.0.0-beta.0"
val npmCssLoaderVersion = "0.28.11"
val npmStyleLoaderVersion = "0.21.0"
val npmReactModalVersion = "3.3.2"
val npmReactSlickVersion = "0.15.4"
val npmReactI18nifyVersion = "1.8.7"
val npmCleanWebpackPluginVersion = "0.1.19"
val npmHtmlWebpackPluginVersion = "3.2.0"
val npmWebpackMd5HashVersion = "0.0.6"
val npmFileLoaderVersion = "1.1.11"
val npmHardSourceWebpackVersion = "0.8.1"
val npmSourceMapLoaderVersion = "0.2.1"
val npmReactGoogleLogin = "2.9.3"
val npmReactFacebookLogin = "3.6.2"
val npmReactTextareaAutoresize = "5.1.0"
val npmReactInfiniteScroller = "1.0.15"
val npmReactShareVersion = "2.0.0"
val npmHexToRgba = "1.0.1"
val npmJquery = "3.2.1"
val npmJsCookie = "2.1.4"
val npmReactDeviceDetect = "1.3.4"
val npmReactTransitionGroup = "2.4.0"
val npmVimeoPlayer = "2.6.3"
/* scala libraries version */
val scalaJsReactVersion = "0.14.0"
val scalajsDomVersion = "0.9.1"
val scalaCssCoreVersion = "0.5.3"

enablePlugins(ScalaJSPlugin, ScalaJSBundlerPlugin)

libraryDependencies ++= Seq(
  "org.scala-js"                 %%% "scalajs-dom"                    % scalajsDomVersion,
  "io.github.shogowada"          %%% "scalajs-reactjs"                % scalaJsReactVersion,
  "io.github.shogowada"          %%% "scalajs-reactjs-router-dom"     % scalaJsReactVersion,
  "io.github.shogowada"          %%% "scalajs-reactjs-redux"          % scalaJsReactVersion,
  "io.github.shogowada"          %%% "scalajs-reactjs-redux-devtools" % scalaJsReactVersion,
  "com.github.japgolly.scalacss" %%% "core"                           % scalaCssCoreVersion
)

npmDependencies in Compile ++= Seq(
  "react" -> npmReactVersion,
  "react-dom" -> npmReactVersion,
  "react-router" -> npmReactRouterVersion,
  "react-router-dom" -> npmReactRouterVersion,
  "react-modal" -> npmReactModalVersion,
  "react-slick" -> npmReactSlickVersion,
  "react-i18nify" -> npmReactI18nifyVersion,
  "react-autosuggest" -> npmReactAutosuggestVersion,
  "react-infinite-scroller " -> npmReactInfiniteScroller,
  "react-google-login" -> npmReactGoogleLogin,
  "react-facebook-login" -> npmReactFacebookLogin,
  "react-textarea-autosize" -> npmReactTextareaAutoresize,
  "react-share" -> npmReactShareVersion,
  "hex-to-rgba" -> npmHexToRgba,
  "jquery" -> npmJquery,
  "js-cookie" -> npmJsCookie,
  "react-device-detect" -> npmReactDeviceDetect,
  "react-transition-group " -> npmReactTransitionGroup,
  "@vimeo/player" -> npmVimeoPlayer
)

npmDevDependencies in Compile ++= Seq(
  "ajv" -> "6.5.1",
  "clean-webpack-plugin" -> npmCleanWebpackPluginVersion,
  "css-loader" -> npmCssLoaderVersion,
  "extract-text-webpack-plugin" -> npmExtractTextWebpackPluginVersion,
  "file-loader" -> npmFileLoaderVersion,
  "hard-source-webpack-plugin" -> npmHardSourceWebpackVersion,
  "html-webpack-plugin" -> npmHtmlWebpackPluginVersion,
  "style-loader" -> npmStyleLoaderVersion,
  "webpack-dev-server" -> "3.1.4",
  "webpack-md5-hash" -> npmWebpackMd5HashVersion,
  "uglifyjs-webpack-plugin" -> "1.2.5",
  "webpack" -> npmWebpackVersion
)

npmResolutions in Compile := {
  (npmDependencies in Compile).value.toMap ++ (npmDevDependencies in Compile).value.toMap
}

version in webpack := npmWebpackVersion

webpackConfigFile in fastOptJS := Some(baseDirectory.value / "make-webpack-library.config.js")
webpackConfigFile in fullOptJS := Some(baseDirectory.value / "make-webpack-prod.config.js")

scalaJSUseMainModuleInitializer := true

webpackDevServerExtraArgs := Seq("--lazy", "--inline", "--host", "0.0.0.0")
webpackDevServerPort := 9009

webpackBundlingMode in fastOptJS := BundlingMode.LibraryOnly("makeApp")
webpackBundlingMode in fullOptJS := BundlingMode.Application

emitSourceMaps := System.getenv("CI_BUILD") != "true"

// Prod settings
scalacOptions ++= {
  if (System.getenv("CI_BUILD") == "true") {
    // Seq("-Xelide-below", "OFF") do not uglify css until we know how to make it work correctly
    Seq()
  } else {
    Seq("-deprecation", "-feature")
  }
}

lazy val buildTime: SettingKey[String] = SettingKey[String]("buildTime", "time of build")

buildTime := ZonedDateTime.now(ZoneOffset.UTC).toString

lazy val buildVersion: TaskKey[String] = taskKey[String]("build version")

buildVersion := {
  val buildVersion: String = s"""{
     |    "name": "${name.value}",
     |    "version": "${version.value}",
     |    "gitHeadCommit": "${GitKeys.gitHeadCommit.value.getOrElse("DETACHED")}",
     |    "gitBranch": "${GitKeys.gitCurrentBranch.value}",
     |    "buildTime": "${buildTime.value}"
     |}""".stripMargin

  streams.value.log.info("Building version")
  streams.value.log.info(buildVersion)

  buildVersion
}

// Custome task to manage assets
val prepareAssets = taskKey[Unit]("prepareAssets")
prepareAssets in ThisBuild := {
  val npmDirectory = (npmUpdate in Compile).value

  IO.copyDirectory(baseDirectory.value / "src" / "main" / "static", npmDirectory, overwrite = true)

  IO.copyDirectory(
    baseDirectory.value / "src" / "main" / "static" / "consultation",
    npmDirectory / "dist" / "consultation",
    overwrite = true
  )

  IO.copyDirectory(
    baseDirectory.value / "src" / "main" / "static" / "consultation" / "images",
    npmDirectory / "dist" / "consultation" / "images",
    overwrite = true
  )

  IO.copyDirectory(
    baseDirectory.value / "src" / "main" / "static" / "consultation" / "fonts",
    npmDirectory / "dist" / "consultation" / "fonts",
    overwrite = true
  )

  IO.listFiles(baseDirectory.value / "src" / "main" / "static" / "favicon").foreach {
    file =>
      IO.copyFile(file, npmDirectory / "dist" / file.name)
  }

  val buildVersionFile: File = npmDirectory / "dist" / "version"
  val buildVersionLocalFile: File = npmDirectory / "version"
  val contents: String = buildVersion.value

  IO.write(buildVersionFile, contents)
  IO.write(buildVersionLocalFile, contents)

  streams.value.log.info("Copy assets to working directory")
}

fastOptJS in Compile := {
  prepareAssets.value
  (fastOptJS in Compile).value
}

fullOptJS in Compile := {
  prepareAssets.value
  (fullOptJS in Compile).value
}

gitCommitMessageHook := Some(baseDirectory.value / "bin" / "commit-msg.hook")

enablePlugins(GitHooks)

useYarn := true

git.formattedShaVersion := git.gitHeadCommit.value.map { sha =>
  sha.take(10)
}

version in ThisBuild := {
  git.formattedShaVersion.value.get
}

enablePlugins(GitVersioning)

scalastyleFailOnError := true