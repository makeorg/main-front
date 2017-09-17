addSbtPlugin("org.scala-js"     % "sbt-scalajs"         % "0.6.19")
addSbtPlugin("ch.epfl.scala"    % "sbt-scalajs-bundler" % "0.8.0")
addSbtPlugin("com.typesafe.sbt" % "sbt-native-packager" % "1.2.2")
addSbtPlugin("com.geirsson"     % "sbt-scalafmt"        % "0.6.8")
// Use the following version for sbt 1.0.1
// addSbtPlugin("com.geirsson"     % "sbt-scalafmt"        % "1.2.0")
addSbtPlugin("org.make" % "git-hooks-plugin" % "1.0.0")

resolvers += Resolver.url("bintray-flaroche-sbt-plugins", url("http://dl.bintray.com/flaroche/make-sbt-plugins/"))(
  Resolver.ivyStylePatterns
)
