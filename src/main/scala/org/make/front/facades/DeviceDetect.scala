package org.make.front.facades

import scala.scalajs.js
import scala.scalajs.js.annotation.JSImport
import scala.scalajs.js.|

@js.native
@JSImport("react-device-detect", JSImport.Namespace)
object DeviceDetect extends js.Object {
  def isMobile: Boolean = js.native
  def isBrowser: Boolean = js.native
  def isMobileOnly: Boolean = js.native
  def isTablet: Boolean = js.native
  def isSmartTV: Boolean = js.native
  def isWearable: Boolean = js.native
  def isConsole: Boolean = js.native
  def isAndroid: Boolean = js.native
  def isWinPhone: Boolean = js.native
  def isIOS: Boolean = js.native
  def isChrome: Boolean = js.native
  def isFirefox: Boolean = js.native
  def isSafari: Boolean = js.native
  def isOpera: Boolean = js.native
  def isIE: Boolean = js.native
  def isEdge: Boolean = js.native
  def isChromium: Boolean = js.native
  def isMobileSafari: Boolean = js.native
  def osVersion: String = js.native
  def osName: String = js.native
  def fullBrowserVersion: String = js.native
  def browserVersion: String = js.native
  def browserName: String = js.native
  def mobileVendor: String = js.native
  def mobileModel: String = js.native
  def engineName: String = js.native
  def engineVersion: String = js.native
  def getUA: String = js.native
  def deviceDetect(): DeviceData = js.native

  @js.native
  trait DeviceData extends js.Object {
    val isBrowser: Boolean
    val browserMajorVersion: js.UndefOr[String]
    val browserFullVersion: js.UndefOr[String]
    val browserName: js.UndefOr[String]
    val engineName: js.UndefOr[String | Boolean]
    val engineVersion: js.UndefOr[String]
    val osName: js.UndefOr[String]
    val osVersion: js.UndefOr[String]
    val userAgent: js.UndefOr[String]
    val vendor: js.UndefOr[String]
    val model: js.UndefOr[String]
    val os: js.UndefOr[String]
    val ua: js.UndefOr[String]
    val isSmartTV: js.UndefOr[Boolean]
    val isConsole: js.UndefOr[Boolean]
    val isWearable: js.UndefOr[Boolean]
  }
}
