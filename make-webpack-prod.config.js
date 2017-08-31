var webpack = require('webpack');
var path = require('path');
var ExtractTextPlugin = require('extract-text-webpack-plugin');
var HtmlWebpackPlugin = require('html-webpack-plugin');
var WebpackMd5Hash = require('webpack-md5-hash');
var pathBuild = path.join(__dirname, 'dist');

module.exports = require('./scalajs.webpack.config');

module.exports.module = module.exports.module || {};

module.exports.entry = {
    "make-app": path.join(__dirname, "opt-launcher.js"),
    "main": path.join(__dirname, "main.sass")
};

module.exports.output = {
    path: pathBuild,
    "filename": "[name].[chunkhash].js"
};

module.exports.plugins = [
    new HtmlWebpackPlugin({
        "title": "Make.org",
        "template": path.join(__dirname, "index.template.ejs"),
        "apiUrl": "https://api.prod.makeorg.tech",
        "googleAppId": "810331964280-qtdupbrjusihad3b5da51i5p66qpmhmr.apps.googleusercontent.com",
        "facebookAppId": "317128238675603"
    }),
    new WebpackMd5Hash(),
    new ExtractTextPlugin({ // define where to save the file
        filename: '[name].[chunkhash].bundle.css',
        allChunks: true
    })
];

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
    },
    {
        test: /\.(jpe?g|gif|png)$/,
        loader: 'file-loader?name=images/[name].[hash].[ext]',
        include: [path.join(__dirname, "images")]
    }
];
