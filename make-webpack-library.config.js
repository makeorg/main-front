var webpack = require('webpack');
var path = require('path');
var ExtractTextPlugin = require('extract-text-webpack-plugin');
var HtmlWebpackPlugin = require('html-webpack-plugin');
var WebpackMd5Hash = require('webpack-md5-hash');

module.exports = require('./scalajs.webpack.config');

module.exports.module = module.exports.module || {};

module.exports.plugins = [
    new HtmlWebpackPlugin({
        "title": "Make.org",
        "template": path.join(__dirname, "index-library.template.ejs"),
        "apiUrl": "http://192.168.1.121:9000",
        "googleAppId": "810331964280-qtdupbrjusihad3b5da51i5p66qpmhmr.apps.googleusercontent.com",
        "facebookAppId": "317128238675603",
        "inject": false
    }),
    new WebpackMd5Hash(),
    new ExtractTextPlugin({ // define where to save the file
        filename: '[name].[chunkhash].bundle.css',
        allChunks: true
    })
];



module.exports.module.rules = [
    {
        test: /\.css$/,
        loader: ExtractTextPlugin.extract(['css-loader'])
    },
    {
        "test": new RegExp("\\.js$"),
        "enforce": "pre",
        "loader": "source-map-loader",
        "exclude": [
            path.join(__dirname, "node_modules/react-google-login")
        ]
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