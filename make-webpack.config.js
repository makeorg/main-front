var webpack = require('webpack');

module.exports = require('./scalajs.webpack.config');


module.exports.output = {
    "filename": "make-app.js"
}