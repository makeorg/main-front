/*
 *
 * Make.org Main Front
 * Copyright (C) 2018 Make.org
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 *
 */

var webpack = require('webpack');
var path = require('path');
var ExtractTextPlugin = require('extract-text-webpack-plugin');
var HtmlWebpackPlugin = require('html-webpack-plugin');
var WebpackMd5Hash = require('webpack-md5-hash');

module.exports = require('./scalajs.webpack.config');

module.exports.module = module.exports.module || {};

var htmlWebpackParams = {
    "metaTitle": "META_TITLE",
    "metaDescription": "META_DESCRIPTION",
    "metaPicture": "META_PICTURE",
    "template": path.join(__dirname, "index-library.template.ejs"),
    "apiUrl": "https://api.preprod.makeorg.tech",
    "googleAppId": "810331964280-qtdupbrjusihad3b5da51i5p66qpmhmr.apps.googleusercontent.com",
    "googleAnalyticsId": "UA-97647514-1",
    "facebookAppId": "317128238675603",
    "facebookPixelId": "260470104426586",
    "detectedCountry": "FR", //Modify this to simulate x-detected-country
    "forcedCountry": "FR", //Modify this to simulate x-forced-country
    "inject": false
}
module.exports.plugins = [
    new HtmlWebpackPlugin(htmlWebpackParams),
    new WebpackMd5Hash(),
    new ExtractTextPlugin({ // define where to save the file
        filename: '[name].[chunkhash].bundle.css',
        allChunks: true
    })
];

module.exports.output.publicPath = '/'

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
        loader: 'file-loader?name=fonts/[name].[ext]',
        include: [path.join(__dirname, "fonts")],
    },
    {
        test: /\.(jpe?g|gif|svg|png)$/,
        loader: 'file-loader?name=images/[name].[hash].[ext]',
        include: [path.join(__dirname, "images")]
    }
];