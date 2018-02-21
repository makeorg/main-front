var webpack = require('webpack');
var path = require('path');
var ExtractTextPlugin = require('extract-text-webpack-plugin');
var HtmlWebpackPlugin = require('html-webpack-plugin');
var WebpackMd5Hash = require('webpack-md5-hash');
var scalajs = require('./scalajs.webpack.config')

// Content that will be included in docker image
var build = {
    name: "build",
    entry: {
        'make-front-opt': scalajs.entry["make-front-opt"],
        'css': path.join(__dirname, 'styles/main.css')
    },
    output: {
        path: path.join(__dirname, 'dist'),
        "filename": "[name].[chunkhash].js"
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
            },
            {
                test: /\.json$/,
                loader: 'json-loader'
            }
        ]
    },
    plugins: [
        new webpack.DefinePlugin({
            'process.env': {
                'NODE_ENV': JSON.stringify('production')
            }
        }),
        new HtmlWebpackPlugin({
            "title": "Make.org",
            "template": path.join(__dirname, "index.template.ejs"),
            "apiUrl": "API_URL",
            "googleAppId": "810331964280-qtdupbrjusihad3b5da51i5p66qpmhmr.apps.googleusercontent.com",
            "googleAnalyticsId": "UA-97647514-1",
            "facebookAppId": "317128238675603",
            "facebookPixelId": "260470104426586"
        }),
        new WebpackMd5Hash(),
        new ExtractTextPlugin({ // define where to save the file
            filename: '[name].[chunkhash].bundle.css',
            allChunks: true
        }),
        new webpack.optimize.UglifyJsPlugin({
            compressor: {
                warnings: false
            }
        })
    ]

};


// Content to avoid crashes from the scalajs-bundler
// I hope this hack can be removed some day
var fake =
    {
        name: "fake",
        entry: scalajs.entry,
        output: scalajs.output,
        module: {
            rules: [
                {
                    test: /\.(ttf|otf|eot|svg|woff(2)?)(\?[a-z0-9]+)?$/,
                    loader: 'file-loader?name=fonts/[name].[ext]'
                },
                {
                    test: /\.(jpe?g|gif|png)$/,
                    loader: 'file-loader?name=images/[name].[ext]',
                    include: [path.join(__dirname, "images")]
                }
            ]
        }
    };


module.exports = [fake, build];
