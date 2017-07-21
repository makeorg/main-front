var webpack = require('webpack');
var ExtractTextPlugin = require('extract-text-webpack-plugin');

module.exports = require('./scalajs.webpack.config');


module.exports.entry = {
    "make-app": "./fastopt-launcher.js",
    "main": "./main.sass"
}

module.exports.output = {
    "filename": "[name].js"
}

module.exports.module.rules = [
    {
        test: /\.sass$/,
        loader: ExtractTextPlugin.extract(['css-loader', 'sass-loader'])
    },
    {
        "test": new RegExp("\\.js$"),
        "enforce": "pre",
        "loader": "source-map-loader"
    }
]

module.exports.plugins = [
    new ExtractTextPlugin({ // define where to save the file
        filename: 'dist/[name].bundle.css',
        allChunks: true,
    }),
]