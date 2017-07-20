organization := "org.make.front"
name := "make-front"
version := "1.0.0-SNAPSHOT"
scalaVersion := "2.12.1"

/* Npm versions */
val npmReactVersion = "15.6.1"
val npmWebpackVersion = "2.3.2"
val npmReactRouterVersion = "4.1.2"

/* scala libraries version */
val scalaJsReactVersion = "0.14.0"
val circeVersion = "0.8.0"

enablePlugins(ScalaJSPlugin, ScalaJSBundlerPlugin)

libraryDependencies ++= Seq(
  "org.scala-js" %%% "scalajs-dom" % "0.9.1",
  "io.github.shogowada" %%% "scalajs-reactjs" % scalaJsReactVersion, // For react facade
  "io.github.shogowada" %%% "scalajs-reactjs-router-dom" % scalaJsReactVersion, // Optional. For react-router-dom facade
  "io.github.shogowada" %%% "scalajs-reactjs-router-redux" % scalaJsReactVersion, // Optional. For react-router-dom facade
  "io.github.shogowada" %%% "scalajs-reactjs-redux" % scalaJsReactVersion, // Optional. For react-redux facade
  "io.github.shogowada" %%% "scalajs-reactjs-redux-devtools" % scalaJsReactVersion, // Optional. For redux-devtools facade
  "com.github.japgolly.scalacss" %%% "core" % "0.5.3",
  "io.circe" %% "circe-core" % circeVersion,
  "io.circe" %% "circe-generic" % circeVersion,
  "io.circe" %% "circe-parser" % circeVersion,
  "io.circe" %%% "circe-scalajs" % circeVersion
)

npmDependencies in Compile ++= Seq(
  "react" -> npmReactVersion,
  "react-dom" -> npmReactVersion,
  "react-router" -> npmReactRouterVersion,
  "react-router-dom" -> npmReactRouterVersion,
  "react-modal" -> "2.2.2",
  "react-i18nify" -> "1.8.7"
)

npmDevDependencies in Compile += "webpack-merge" -> "4.1.0"

npmResolutions in Compile := {
  (npmDependencies in Compile).value.toMap
}

version in webpack := npmWebpackVersion

webpackResources := {
  baseDirectory.value / "src" / "main" / "webpack" ** "*.js" +++
    baseDirectory.value / "src" / "main" / "universal" ** "*.css" +++
    baseDirectory.value / "src" / "main" / "universal" / "index.html" +++
    baseDirectory.value / "src" / "main" / "universal" / "logo.svg"
}

webpackConfigFile := Some(baseDirectory.value / "make-webpack.config.js")

webpackDevServerPort := 9009
