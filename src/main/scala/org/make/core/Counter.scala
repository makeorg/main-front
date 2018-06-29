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

package org.make.core

class Counter(initialValue: Int = 0) {

  private var internalValue: Int = initialValue

  def getAndIncrement(step: Int = 1): Int = {
    val result = internalValue
    internalValue += step
    result
  }

  def get: Int = internalValue

  def incrementAndGet(step: Int = 1): Int = {
    internalValue += step
    internalValue
  }

}

object Counter {
  val showcaseCounter: Counter = new Counter()
}
