var webpack = require('webpack');
var path = require('path');
var ExtractTextPlugin = require('extract-text-webpack-plugin');
var HtmlWebpackPlugin = require('html-webpack-plugin');
var WebpackMd5Hash = require('webpack-md5-hash');
var scalajs = require('./scalajs.webpack.config')

var htmlWebpackParams = {
    "metaTitle": "Make.org",
    "metaDescription": "Make.org",
    "metaPicture": "https://uploads-ssl.webflow.com/598345cdee443e00013ae603/59a526e0a1a95c0001f8ca11_make.png",
    "template": path.join(__dirname, "index.template.ejs"),
    "apiUrl": "API_URL",
    "googleAppId": "810331964280-qtdupbrjusihad3b5da51i5p66qpmhmr.apps.googleusercontent.com",
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
        new HtmlWebpackPlugin(htmlWebpackParams),
        new HtmlWebpackPlugin(Object.assign({}, htmlWebpackParams, {
            "metaTitle": "Comment mieux vivre ensemble ?",
            "metaDescription": "Vous avez un avis sur le sujet ? Alors comme des milliers de citoyens participez à la consultation nationale Make.org : proposez vos idées, réagissez à celles des autres ! Les meilleures seront transformées en actions.",
            "metaPicture": "https://uploads-ssl.webflow.com/598345cdee443e00013ae603/5aaa3e43106bcfc5bc0979cc_simulation%20visuel%20fb.jpg",
            "filename": "./mieux-vivre-ensemble.html",
        })),
        new HtmlWebpackPlugin(Object.assign({}, htmlWebpackParams, {
            "metaTitle": "COME FAR FRONTE ALLA VIOLENZA SULLE DONNE?",
            "metaDescription": "Migliaia di cittadini propongono delle soluzioni Prendi posizione sulle soluzioni e proponi le tue",
            "metaPicture": "https://uploads-ssl.webflow.com/59833d390a24e50001b873d8/5ab8c7451f3370d8c1d74812_image-share-vff-it.png",
            "filename": "./vff-it.html",
        })),
        new HtmlWebpackPlugin(Object.assign({}, htmlWebpackParams, {
            "metaTitle": "HOW TO COMBAT VIOLENCE AGAINST WOMEN?",
            "metaDescription": "Thousands of citizens are submitting solutions. Take a stand on these solutions and offer yours.",
            "metaPicture": "https://uploads-ssl.webflow.com/59833d390a24e50001b873d8/5ab8c74541c0915ed0cead53_image-share-vff-uk.png",
            "filename": "./vff-gb.html",
        })),
        new HtmlWebpackPlugin(Object.assign({}, htmlWebpackParams, {
            "metaTitle": "Make.org",
            "metaDescription": "Make.org",
            "metaPicture": "https://uploads-ssl.webflow.com/598345cdee443e00013ae603/59a526e0a1a95c0001f8ca11_make.png",
            "filename": "./vff.html",
        })),
        new HtmlWebpackPlugin(Object.assign({}, htmlWebpackParams, {
            "metaTitle": "Comment donner une chance à chaque jeune ?",
            "metaDescription": "Vous avez un avis sur le sujet ? Alors comme des milliers de citoyens participez à la consultation nationale Make.org : proposez vos idées, réagissez à celles des autres ! Les meilleures seront transformées en actions.",
            "metaPicture": "https://uploads-ssl.webflow.com/59833d390a24e50001b873d8/5ac49addb50fc94d36e63221_share%20image%20une%20chance%20pour%20chaque%20jeune.jpg",
            "filename": "./chance-aux-jeunes.html",
        })),
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
