# Build

The build of the application is maded using sbt and webpack.

## Webpack
We use webpack to bundle the application. scalajs-bundler provide a webpack default config
that define the entry point file generated from scalajs.
This file is overrided by our configuration who use some loaders and plugins.

#### Loaders:
The loaders used by the webpack config are:
##### file-loader:
This is used to load asset like images, json and fonts.
##### source-map-loader:
Needed to load third parties librairies having their own source mpas.
##### css-loader:
This loader allow us to import css file into js class

#### Plugins:

##### HtmlWebpackPlugin:
This plugin allow to create customizable index.html file including the output javascript file.
We use this plugin to generate multi html file with different meta-tags defined by Operation
##### WebpackMd5Hash:
This plugin add a hash to compiled file, this is needed to generate new hash when file are updated
to avoid browser cache invalidation.
##### ExtractTextPlugin:
This plugin allow us to separate the inlined css in scala file into a separated css file.
##### UglifyJsPlugin:
This plugin is need to minify Javascript generated code in production environnement
##### DefinePlugin:
This plugin allow us to define an Environnement variable to use in Jvascript code or build process.


## sbt
To Bundle the application we use sbt task
[build.sbt](build.sbt)

## Developpement build
The bundling of the Developpement file is maded by
fastOptJS (fast Scala.js-specific optimizations). It's an sbt task that compile
scala file into js and group them into one file.

To launch the developpement build:
```

sbt fastOptJS::startWebpackDevServer ~fastOptJS

```

## Production build
The bundling of the Production file is maded by
fullOptJS (full Scala.js-specific optimizations). It's an sbt task that compile
scala file into js and group them into one file fully optimized (minimification..).
