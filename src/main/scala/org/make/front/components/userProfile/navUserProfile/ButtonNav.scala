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

package org.make.front.components.userProfile.navUserProfile

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM.{<, ^, _}
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import org.make.front.Main.CssSettings._
import org.make.front.components.Components._
import org.make.front.components.userProfile.UserProfileInformationsStyles
import org.make.front.components.userProfile.UserProfileInformationsStyles.style
import org.make.front.facades.I18n
import org.make.front.facades.Unescape.unescape
import org.make.front.styles.ThemeStyles
import org.make.front.styles.base._
import org.make.front.styles.ui.CTAStyles
import org.make.front.styles.utils._
import org.make.front.styles.vendors.FontAwesomeStyles

import scala.scalajs.js

object ButtonNav {

  final case class ButtonNavProps(onClickMethod: () => Unit, icon: StyleA, wording: String)

  val reactClass: ReactClass =
    React
      .createClass[ButtonNavProps, Unit](
      displayName = "ButtonNav",
      render = self => {

        <("ButtonNav")()(
          <.button(
            ^.className := js
              .Array(CTAStyles.basic, CTAStyles.basicOnButton, ButtonNavStyles.slidingPannelGreyButton),
            ^.onClick := self.props.wrapped.onClickMethod
          )(
            <.i(
              ^.className := js
                .Array(self.props.wrapped.icon, ButtonNavStyles.slidingPannelButtonIcon)
            )(),
            <.span()(self.props.wrapped.wording)
          ),
          <.style()(ButtonNavStyles.render[String]))
      }
    )
}

object ButtonNavStyles extends StyleSheet.Inline {
  import dsl._

  val slidingPannelGreyButton: StyleA =
    style(backgroundColor(ThemeStyles.TextColor.lighter))


  val slidingPannelButtonIcon: StyleA =
    style(marginRight(5.pxToEm()))

}
