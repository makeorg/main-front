package org.make.core.URI

import scala.scalajs.js
import scala.scalajs.js.URIUtils

// Inspired by https://github.com/lemonlabsuk/scala-uri/blob/master/shared/src/main/scala/io/lemonlabs/uri/dsl/UrlDsl.scala
class UriDsl(val uri: String) extends AnyVal {

  private def urlify(unencodedUrl: String): String = URIUtils.encodeURIComponent(unencodedUrl)

  private def hasParams(uri: String) = uri.contains("?")

  def paramToString(param: (String, Any)): String = param match {
    case (key, None)        => urlify(key)
    case (key, Some(value)) => s"${urlify(key)}=${urlify(value.toString)}"
    case (key, value)       => s"${urlify(key)}=${urlify(value.toString)}"
  }

  def paramsToString(params: js.Array[(String, Any)], separator: String = "&"): String =
    params.map(paramToString).mkString(separator)

  def ?(param: (String, Any)) = s"$uri?${paramToString(param)}"

  def &(param: (String, Any)) = s"$uri&${paramToString(param)}"

  def `#`(fragment: String) = s"$uri#$fragment"

  def /(path: String) = s"$uri/$path"

  def addParams(params: js.Array[(String, Any)]): String =
    if (params.isEmpty) {
      uri
    } else if (hasParams(uri)) {
      s"$uri&${paramsToString(params)}"
    } else {
      s"$uri?${paramsToString(params)}"
    }

}
