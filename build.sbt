import sbt.Keys.baseDirectory

organization := "org.make.front"
name := "make-front"
version := "1.0.0-SNAPSHOT"
// TODO: Use git plugin to append branch name in version
scalaVersion := "2.12.1"

/* Npm versions */
val npmReactVersion = "15.6.1"
val npmWebpackVersion = "2.6.1"
val npmReactRouterVersion = "4.1.2"
val npmReactAutosuggestVersion = "9.3.1"
val npmExtractTextWebpackPluginVersion = "2.1.2"
val npmCssLoaderVersion = "0.28.4"
val npmStyleLoaderVersion = "0.18.2"
val npmReactModalVersion = "2.2.2"
val npmReactSlickVersion = "0.15.4"
val npmReactI18nifyVersion = "1.8.7"
val npmCleanWebpackPluginVersion = "0.1.16"
val npmHtmlWebpackPluginVersion = "2.29.0"
val npmWebpackMd5HashVersion = "0.0.5"
val npmFrontAwesomeVersion = "4.7.0"
val npmFileLoaderVersion = "0.11.2"
val npmHardSourceWebpackVersion = "0.4.9"
val npmSourceMapLoaderVersion = "0.2.1"
val npmReactGoogleLogin = "2.9.2"
val npmReactFacebookLogin = "3.6.2"

/* scala libraries version */
val scalaJsReactVersion = "0.14.0"
val circeVersion = "0.8.0"
val scalajsDomVersion = "0.9.1"
val scalaCssCoreVersion = "0.5.3"

enablePlugins(ScalaJSPlugin, ScalaJSBundlerPlugin)

libraryDependencies ++= Seq(
  "org.scala-js"                 %%% "scalajs-dom"                    % scalajsDomVersion,
  "io.github.shogowada"          %%% "scalajs-reactjs"                % scalaJsReactVersion, // For react facade
  "io.github.shogowada"          %%% "scalajs-reactjs-router-dom"     % scalaJsReactVersion, // Optional. For react-router-dom facade
  "io.github.shogowada"          %%% "scalajs-reactjs-router-redux"   % scalaJsReactVersion, // Optional. For react-router-dom facade
  "io.github.shogowada"          %%% "scalajs-reactjs-redux"          % scalaJsReactVersion, // Optional. For react-redux facade
  "io.github.shogowada"          %%% "scalajs-reactjs-redux-devtools" % scalaJsReactVersion, // Optional. For redux-devtools facade
  "com.github.japgolly.scalacss" %%% "core"                           % scalaCssCoreVersion,
  "io.circe"                     %%% "circe-core"                     % circeVersion,
  "io.circe"                     %%% "circe-java8"                    % circeVersion,
  "io.circe"                     %%% "circe-generic"                  % circeVersion,
  "io.circe"                     %%% "circe-parser"                   % circeVersion,
  "io.circe"                     %%% "circe-scalajs"                  % circeVersion
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
  "extract-text-webpack-plugin" -> npmExtractTextWebpackPluginVersion,
  "hard-source-webpack-plugin" -> npmHardSourceWebpackVersion,
  "css-loader" -> npmCssLoaderVersion,
  "style-loader" -> npmStyleLoaderVersion,
  "clean-webpack-plugin" -> npmCleanWebpackPluginVersion,
  "html-webpack-plugin" -> npmHtmlWebpackPluginVersion,
  "webpack-md5-hash" -> npmWebpackMd5HashVersion,
  "webpack" -> npmWebpackVersion,
  "file-loader" -> npmFileLoaderVersion,
  "font-awesome" -> npmFrontAwesomeVersion,
  "react-google-login" -> npmReactGoogleLogin,
  "react-facebook-login" -> npmReactFacebookLogin
)

npmResolutions in Compile := {
  (npmDependencies in Compile).value.toMap
}

version in webpack := npmWebpackVersion


webpackConfigFile in fastOptJS := Some(baseDirectory.value / "make-webpack-library.config.js")
//webpackConfigFile in fastOptJS := Some(baseDirectory.value / "make-webpack-dev.config.js")
webpackConfigFile in fullOptJS := Some(baseDirectory.value / "make-webpack-prod.config.js")

scalaJSUseMainModuleInitializer := true

webpackDevServerExtraArgs := Seq("--lazy", "--inline")
webpackDevServerPort := 9009
webpackBundlingMode := BundlingMode.LibraryOnly("makeApp")
emitSourceMaps := false

// Prod settings
scalacOptions ++= Seq("-Xelide-below", "OFF")

// Custome task to manage assets
val prepareAssets = taskKey[Unit]("prepareAssets")
prepareAssets in ThisBuild := {
  val npmDirectory = (npmUpdate in Compile).value
  IO.copyDirectory(baseDirectory.value / "src" / "main" / "static", npmDirectory, overwrite = true)
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