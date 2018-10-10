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

package org.make.front.components.actorProfile

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM.{<, _}
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import org.make.front.styles.ui.CTAStyles
import org.make.front.components.Components._
import scalacss.internal.mutable.StyleSheet
import org.make.front.Main.CssSettings._
import org.make.front.facades.I18n
import org.make.front.styles.ThemeStyles
import org.make.front.styles.utils._
import org.make.front.styles.vendors.FontAwesomeStyles

import scala.concurrent.Future
import scala.scalajs.js

object FollowButton {

  final case class FollowButtonProps(isFollowedByUser: Boolean, triggerFollowToggle: () => Future[Any])

  val reactClass: ReactClass =
    React
      .createClass[FollowButtonProps, Unit](
        displayName = "FollowButton",
        render = self => {
          <.div(^.className := FollowButtonStyles.wrapper)(
            <.button(
              ^.className := js.Array(
                CTAStyles.basic,
                CTAStyles.basicOnA,
                FollowButtonStyles.button,
                FollowButtonStyles.activeButton(self.props.wrapped.isFollowedByUser)
              ),
              ^.onClick := self.props.wrapped.triggerFollowToggle
            )(if (self.props.wrapped.isFollowedByUser) {
              <("ButtonWithIcon")()(
                <.i(^.className := js.Array(FollowButtonStyles.icon, FontAwesomeStyles.check))(),
                I18n.t("actor-profile.followed")
              )
            } else {
              I18n.t("actor-profile.follow")
            }),
            <.style()(FollowButtonStyles.render[String])
          )
        }
      )
}

object FollowButtonStyles extends StyleSheet.Inline {

  import dsl._

  val wrapper: StyleA =
    style(display.flex, justifyContent.center, margin(ThemeStyles.SpacingValue.small.pxToEm(), `0`))

  val button: StyleA =
    style(display.flex, justifyContent.center, width(100.%%), maxWidth(280.pxToEm()))

  val icon: StyleA =
    style(marginRight(5.pxToEm()))

  val activeButton: (Boolean) => StyleA = styleF.bool(
    isFollowedByUser =>
      if (isFollowedByUser) {
        styleS(color(ThemeStyles.TextColor.lighter), backgroundColor(ThemeStyles.BackgroundColor.blackMoreTransparent))
      } else {
        styleS()
    }
  )

}
