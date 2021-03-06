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
var scalajs = require('./scalajs.webpack.config')
var UglifyJsPlugin = require('uglifyjs-webpack-plugin');

var htmlWebpackParams = {
    "metaTitle": "META_TITLE",
    "metaDescription": "META_DESCRIPTION",
    "metaPicture": "META_PICTURE",
    "template": path.join(__dirname, "index.template.ejs"),
    "apiUrl": "API_URL",
    "googleAppId": "810331964280-qtdupbrjusihad3b5da51i5p66qpmhmr.apps.googleusercontent.com",
    "googleAdWordsId": "AW-819115721",
    "googleAnalyticsId": "UA-97647514-1",
    "facebookAppId": "317128238675603",
    "facebookPixelId": "260470104426586"
}
// Content that will be included in docker image
var build = {
    name: "build",
    entry: {
        'make-front-opt': scalajs.entry["make-front-opt"],
        'css': path.join(__dirname, 'styles/main.css')
    },
    output: {
        path: path.join(__dirname, 'dist'),
        "filename": "[name].[chunkhash].js",
        publicPath: '/'
    },
    module: {
        rules: [
            {
                test: /\.css$/,
                loader: ExtractTextPlugin.extract({
                    fallback: "style-loader",
                    use: "css-loader"
                })
            },
            {
                test: /\.(ttf|otf|eot|svg|woff(2)?)(\?[a-z0-9]+)?$/,
                loader: 'file-loader?name=fonts/[name].[hash].[ext]'
            },
            {
                test: /\.(jpe?g|gif|png)$/,
                loader: 'file-loader?name=images/[name].[hash].[ext]',
                include: [path.join(__dirname, "images")]
            }
        ]
    },
    plugins: [
        new webpack.DefinePlugin({
            'process.env': {
                'NODE_ENV': JSON.stringify('production')
            }
        }),
        new HtmlWebpackPlugin(htmlWebpackParams),
        new WebpackMd5Hash(),
        new ExtractTextPlugin({ // define where to save the file
            filename: '[name].[chunkhash].bundle.css',
            allChunks: true
        })

    ],
    optimization: {
      minimizer: [
        new UglifyJsPlugin({
            cache: true,
            parallel: true,
            sourceMap: true
          })
      ]
    }

};


module.exports = build;
