package org.make.front.facades

import scala.scalajs.js
import scala.scalajs.js.annotation.JSImport

@js.native
@JSImport("js-cookie", JSImport.Default)
object Cookies extends js.Object {
  def get(): String = js.native
  def get(name: String): js.UndefOr[String] = js.native
  def getJSON(): js.Any = js.native
  def getJSON(name: String): js.UndefOr[js.Any] = js.native

  def set(name: String, value: String): Unit = js.native
  def set(name: String, value: String, opts: CookieOpts): Unit = js.native

  def remove(name: String, path: Path): Unit = js.native
}

@js.native
trait CookieOpts extends js.Object {
  val expires: Int = js.native
  val path: String = js.native
  val domain: String = js.native
  val secure: Boolean = js.native
}

object CookieOpts {
  def apply(
             expires: Int,
             path: String = "/",
             domain: String = null,
             secure: Boolean = false
           ): CookieOpts = {
    js.Dynamic.literal(
      expires = expires,
      path = path,
      domain = domain,
      secure = secure
    ).asInstanceOf[CookieOpts]
  }
}

@js.native
trait Path extends js.Object {
  val path: String = js.native
}

object Path {
  def apply(path: String = "/"): Path = {
    js.Dynamic.literal(
      path = path
    ).asInstanceOf[Path]
  }
}