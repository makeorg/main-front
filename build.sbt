organization := "org.make.front"
name := "make-front"
version := "1.0.0-SNAPSHOT"
scalaVersion := "2.12.1"

/* Npm versions */
val npmReactVersion = "15.6.1"
val npmWebpackVersion = "2.3.2"
val npmReactRouterVersion = "4.1.2"
val npmBulmaVersion = "0.4.3"
val npmSassLoaderVersion = "6.0.6"
val npmNodeSassVersion = "4.5.3"
val npmExtractTextWebpackPluginVersion = "2.1.2"
val npmCssLoaderVersion = "0.28.4"
val npmStyleLoaderVersion = "0.18.2"
val npmReactModalVersion = "2.2.2"
val npmReactI18nifyVersion = "1.8.7"
val npmCleanWebpackPluginVersion = "0.1.16"
val npmHtmlWebpackPluginVersion = "2.29.0"
val npmWebpackMd5HashVersion = "0.0.5"
val npmFrontAwesomeVersion = "4.7.0"
val npmFileLoaderVersion = "0.11.2"
val npmNormalizeVersion = "7.0.0"

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
  "io.circe"                     %% "circe-core"                      % circeVersion,
  "io.circe"                     %% "circe-generic"                   % circeVersion,
  "io.circe"                     %% "circe-parser"                    % circeVersion,
  "io.circe"                     %%% "circe-scalajs"                  % circeVersion
)

npmDependencies in Compile ++= Seq(
  "react" -> npmReactVersion,
  "react-dom" -> npmReactVersion,
  "react-router" -> npmReactRouterVersion,
  "react-router-dom" -> npmReactRouterVersion,
  "react-modal" -> npmReactModalVersion,
  "react-i18nify" -> npmReactI18nifyVersion,
  "bulma" -> npmBulmaVersion,
  "sass-loader" -> npmSassLoaderVersion,
  "node-sass" -> npmNodeSassVersion,
  "extract-text-webpack-plugin" -> npmExtractTextWebpackPluginVersion,
  "css-loader" -> npmCssLoaderVersion,
  "style-loader" -> npmStyleLoaderVersion,
  "clean-webpack-plugin" -> npmCleanWebpackPluginVersion,
  "html-webpack-plugin" -> npmHtmlWebpackPluginVersion,
  "webpack-md5-hash" -> npmWebpackMd5HashVersion,
  "file-loader" -> npmFileLoaderVersion,
  "font-awesome" -> npmFrontAwesomeVersion,
  "normalize-scss" -> npmNormalizeVersion
)

npmResolutions in Compile := {
  (npmDependencies in Compile).value.toMap
}

version in webpack := npmWebpackVersion

webpackResources := {
  baseDirectory.value / "src" / "main" / "static" ** "*.sass" +++
    baseDirectory.value / "src" / "main" / "static" ** "*.ejs" +++
    baseDirectory.value / "src" / "main" / "static" ** "*.svg" +++
    baseDirectory.value / "src" / "main" / "static" ** "*.png" +++
    baseDirectory.value / "src" / "main" / "static" ** "*.jpg" +++
    baseDirectory.value / "src" / "main" / "static" ** "*.jpeg"
}

webpackConfigFile := Some(baseDirectory.value / "make-webpack.config.js")

webpackDevServerPort := 9009

// Prod settings
scalacOptions ++= Seq("-Xelide-below", "OFF")
