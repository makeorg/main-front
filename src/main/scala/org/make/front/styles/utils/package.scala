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
