import java.time.LocalDate

import com.typesafe.sbt.GitVersioning
import sbt.Keys.baseDirectory

organization := "org.make.front"
name := "make-front"
scalaVersion := "2.12.1"

/* Npm versions */
val npmReactVersion = "15.6.1"
val npmWebpackVersion = "3.6.0"
val npmReactRouterVersion = "4.1.2"
val npmReactAutosuggestVersion = "9.3.1"
val npmExtractTextWebpackPluginVersion = "3.0.0"
val npmCssLoaderVersion = "0.28.4"
val npmStyleLoaderVersion = "0.18.2"
val npmReactModalVersion = "2.2.2"
val npmReactSlickVersion = "0.15.4"
val npmReactI18nifyVersion = "1.8.7"
val npmCleanWebpackPluginVersion = "0.1.16"
val npmHtmlWebpackPluginVersion = "2.29.0"
val npmWebpackMd5HashVersion = "0.0.5"
val npmFileLoaderVersion = "0.11.2"
val npmHardSourceWebpackVersion = "0.4.9"
val npmSourceMapLoaderVersion = "0.2.1"
val npmReactGoogleLogin = "2.9.3"
val npmReactFacebookLogin = "3.6.2"
val npmReactTextareaAutoresize = "5.1.0"
val npmReactInfiniteScroller = "1.0.15"
val npmHexToRgba = "0.2.0"
val npmJquery = "3.2.1"
val npmJsCookie = "2.1.4"
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
  "hex-to-rgba" -> npmHexToRgba,
  "jquery" -> npmJquery,
  "js-cookie" -> npmJsCookie
)

npmDevDependencies in Compile ++= Seq(
  "ajv" -> "5.2.2",
  "clean-webpack-plugin" -> npmCleanWebpackPluginVersion,
  "css-loader" -> npmCssLoaderVersion,
  "extract-text-webpack-plugin" -> npmExtractTextWebpackPluginVersion,
  "file-loader" -> npmFileLoaderVersion,
  "hard-source-webpack-plugin" -> npmHardSourceWebpackVersion,
  "html-webpack-plugin" -> npmHtmlWebpackPluginVersion,
  "style-loader" -> npmStyleLoaderVersion,
  "webpack-dev-server" -> "2.8.2",
  "webpack-md5-hash" -> npmWebpackMd5HashVersion,
  "webpack" -> npmWebpackVersion
)

npmResolutions in Compile := {
  (npmDependencies in Compile).value.toMap ++ (npmDevDependencies in Compile).value.toMap
}

version in webpack := npmWebpackVersion

webpackConfigFile in fastOptJS := Some(baseDirectory.value / "make-webpack-library.config.js")
webpackConfigFile in fullOptJS := Some(baseDirectory.value / "make-webpack-prod.config.js")

scalaJSUseMainModuleInitializer := true

webpackDevServerExtraArgs := Seq("--lazy", "--inline")
webpackDevServerPort := 9009

webpackBundlingMode in fastOptJS := BundlingMode.LibraryOnly("makeApp")
webpackBundlingMode in fullOptJS := BundlingMode.Application

emitSourceMaps := System.getenv("CI_BUILD") != "true"

// Prod settings
scalacOptions ++= {
  if (System.getenv("CI_BUILD") == "true") {
    // Seq("-Xelide-below", "OFF") do not uglify css until we know how to make it work correctly
    Seq.empty
  } else {
    Seq("-deprecation")
  }
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
  sha.take(7)
}

version in ThisBuild := {
  if (System.getenv().containsKey("CI_BUILD")) {
    s"${System.getenv("CI_COMMIT_REF_NAME")}-${LocalDate.now()}-${git.formattedShaVersion.value.get}"
  } else {
    s"${git.gitCurrentBranch.value}-${LocalDate.now()}-${git.formattedShaVersion.value.get}"
  }
}

enablePlugins(GitVersioning)
