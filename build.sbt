organization := "org.make.front"
name := "make-front"
version := "1.0.0-SNAPSHOT"

isScalaJSProject := true

libraryDependencies ++= Seq(
  "io.github.shogowada" %%% "scalajs-reactjs" % "0.10.0", // For react facade
  "io.github.shogowada" %%% "scalajs-reactjs-router-dom" % "0.10.0", // Optional. For react-router-dom facade
  "io.github.shogowada" %%% "scalajs-reactjs-redux" % "0.10.0", // Optional. For react-redux facade
  "io.github.shogowada" %%% "scalajs-reactjs-redux-devtools" % "0.10.0" // Optional. For redux-devtools facade
)
