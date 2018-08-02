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

package org.make.front.facades

import scala.scalajs.js
import scala.scalajs.js.annotation.{JSGlobal, JSImport, JSName}

@js.native
@JSImport("@vimeo/player", JSImport.Default)
class VimeoPlayer(element: String) extends js.Object {

  def on(eventName: String, callback: js.Function1[js.Object, Unit]): Unit = js.native

  def off(eventName: String): Unit = js.native
}

@js.native
@JSGlobal
object VimeoPlayer extends js.Object {

  @JSName("constructor")
  def apply(element: String): VimeoPlayer = js.native
}
