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
import org.make.front.facades.I18n
import org.make.front.facades.Unescape.unescape
import org.make.front.styles.ThemeStyles
import org.make.front.styles.base._
import org.make.front.styles.utils._

import scala.scalajs.js

object TabNav {

  final case class TabNavProps(activeTab: String, changeActiveTab: String => Unit)

  val reactClass: ReactClass =
    React
      .createClass[TabNavProps, Unit](
        displayName = "TabNav",
        render = self => {

          def changeTab(newTab: String): () => Unit = { () =>
            self.props.wrapped.changeActiveTab(newTab)
          }

          <("TabNav")()(if (self.props.wrapped.activeTab == "settings") {
            <.nav(^.className := js.Array(LayoutRulesStyles.centeredRow, TabNavStyles.tabWrapper))(
              <.button(
                ^.className := js
                  .Array(TabNavStyles.tab, TabNavStyles.tabSelection(self.props.wrapped.activeTab == "settings")),
                ^.onClick := changeTab("settings")
              )(<.span(^.className := TabNavStyles.titleLink)(unescape(I18n.t("user-profile.settings-tab")))),
              <.div(^.className := TabNavStyles.sep)()
            )
          } else {
            <.nav(^.className := js.Array(LayoutRulesStyles.centeredRow, TabNavStyles.tabWrapper))(
              /*<.button(
                ^.className := js
                  .Array(TabNavStyles.tab, TabNavStyles.tabSelection(self.props.wrapped.activeTab == "summary")),
                ^.onClick := changeTab("summary")
              )(<.span(^.className := TabNavStyles.titleLink)(unescape(I18n.t("user-profile.summary-tab")))),*/
              <.button(
                ^.className := js
                  .Array(TabNavStyles.tab, TabNavStyles.tabSelection(self.props.wrapped.activeTab == "proposals")),
                ^.onClick := changeTab("proposals")
              )(<.span(^.className := TabNavStyles.titleLink)(unescape(I18n.t("user-profile.proposals-tab")))),
              /*<.button(
                ^.className := js
                  .Array(TabNavStyles.tab, TabNavStyles.tabSelection(self.props.wrapped.activeTab == "actions")),
                ^.onClick := changeTab("actions")
              )(<.span(^.className := TabNavStyles.titleLink)(unescape(I18n.t("user-profile.actions-tab")))),*/
              <.div(^.className := TabNavStyles.sep)()
            )
          }, <.style()(TabNavStyles.render[String]))
        }
      )
}

object TabNavStyles extends StyleSheet.Inline {
  import dsl._

  val tabWrapper: StyleA =
    style(
      position.relative,
      marginTop(ThemeStyles.SpacingValue.medium.pxToEm()),
      ThemeStyles.MediaQueries
        .beyondLargeMedium(marginTop(-53.pxToEm()), padding(`0`))
    )

  val tab: StyleA =
    style(
      position.relative,
      display.inlineBlock,
      zIndex(1),
      verticalAlign.bottom,
      padding(14.pxToEm(), 14.pxToEm(), 7.pxToEm()),
      backgroundColor(ThemeStyles.BackgroundColor.altGrey),
      textAlign.center,
      ThemeStyles.MediaQueries
        .beyondLarge(unsafeChild("span")(display.inlineBlock, paddingLeft(5.pxToEm()), paddingRight(5.pxToEm()))),
    )

  val tabSelection: Boolean => StyleA = styleF.bool(
    active =>
      if (active) {
        styleS(
          paddingTop(19.pxToEm()),
          backgroundColor(ThemeStyles.BackgroundColor.lightGrey),
          borderTop(4.pxToEm(), solid, ThemeStyles.BackgroundColor.black),
          borderLeft(1.pxToEm(), solid, ThemeStyles.BackgroundColor.black),
          borderRight(1.pxToEm(), solid, ThemeStyles.BackgroundColor.black),
          borderBottom(1.pxToEm(), solid, ThemeStyles.BackgroundColor.lightGrey)
        )
      } else
        styleS(border(1.pxToEm(), solid, ThemeStyles.BackgroundColor.black))
  )

  val titleLink: StyleA =
    style(TextStyles.title, fontSize(16.pxToEm()), ThemeStyles.MediaQueries.beyondMedium(fontSize(18.pxToEm())))

  val sep: StyleA =
    style(
      position.absolute,
      bottom(`0`),
      left(`0`),
      width(100.%%),
      height(1.pxToEm()),
      backgroundColor(ThemeStyles.BackgroundColor.black),
      zIndex(0)
    )
}
