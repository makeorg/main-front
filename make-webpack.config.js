var webpack = require('webpack');
var path = require('path');
var ExtractTextPlugin = require('extract-text-webpack-plugin');
var CleanWebpackPlugin = require('clean-webpack-plugin');
var HtmlWebpackPlugin = require('html-webpack-plugin');
var WebpackMd5Hash = require('webpack-md5-hash');

module.exports = require('./scalajs.webpack.config');

module.exports.plugins = [
    new HtmlWebpackPlugin({
        "title": "Make.org",
        "template": path.join(__dirname, "index.template.ejs")
    }),
    new WebpackMd5Hash(),
    new ExtractTextPlugin({ // define where to save the file
        filename: '[name].[chunkhash].bundle.css',
        allChunks: true,
    })
]

module.exports.entry = {
    "make-app": "./fastopt-launcher.js",
    "main": "./main.sass",
}

module.exports.output = {
    path: path.join(__dirname, 'dist'),
    "filename": "[name].[chunkhash].js"
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
    },
    {
        test: /\.(ttf|otf|eot|svg|woff(2)?)(\?[a-z0-9]+)?$/,
        loader: 'file-loader?name=fonts/[name].[ext]'
    }
]