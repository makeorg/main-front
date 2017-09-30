package org.make.front

import scalacss.internal.DslBase._
import scalacss.internal.Length

package object styles {

  implicit class NormalizedSize(val baseSize: Int) extends AnyVal {
    def pxToEm(browserContextSize: Int = 16): Length[Double] = {
      new DslNum(baseSize.toDouble / browserContextSize.toDouble).em
    }
  }

}
