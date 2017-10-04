package org.make.front.styles

import scalacss.internal.DslBase._
import scalacss.internal.Length

package object utils {

  implicit class NormalizedSize(val baseSize: Int) extends AnyVal {
    def pxToEm(browserContextSize: Int = 16): Length[Double] = {
      new DslNum(baseSize.toDouble / browserContextSize.toDouble).em
    }
  }
}
