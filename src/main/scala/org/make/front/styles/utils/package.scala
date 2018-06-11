package org.make.front.styles

import scalacss.internal.DslBase.DslNum
import scalacss.internal.{Length, Percentage}

package object utils {

  implicit class NormalizedSize(val baseSize: Int) extends AnyVal {
    def pxToEm(browserContextSize: Int = 16): Length[Double] = {
      new DslNum(baseSize.toDouble / browserContextSize.toDouble).em
    }
  }

  implicit class ChildSize(val childValue: Int) extends AnyVal {
    def pxToPercent(parentValue: Int): Percentage[Double] = {
      new DslNum(childValue.toDouble * 100 / parentValue.toDouble).%%
    }
  }
}
