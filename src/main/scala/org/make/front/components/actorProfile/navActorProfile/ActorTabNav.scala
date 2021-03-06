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

package org.make.front.components.actorProfile.navActorProfile

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM.{<, ^, _}
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.statictags.Element
import org.make.front.Main.CssSettings._
import org.make.front.components.Components._
import org.make.front.facades.I18n
import org.make.front.facades.Unescape.unescape
import org.make.front.styles.ThemeStyles
import org.make.front.styles.base._
import org.make.front.styles.utils._

import scala.scalajs.js

object ActorTabNav {

  final case class ActorTabNavProps(activeTab: String, changeActiveTab: String => Unit)

  val reactClass: ReactClass =
    React
      .createClass[ActorTabNavProps, Unit](
        displayName = "ActorTabNav",
        render = self => {

          def changeTab(newTab: String): () => Unit = { () =>
            self.props.wrapped.changeActiveTab(newTab)
          }

          val listTabs: Seq[Element] = {
            val tabNames = Seq("proposals", "contributions")
            val tabContents = Seq(
              <.span(^.className := ActorTabNavStyles.titleLink)(unescape(I18n.t("actor-profile.proposals-tab"))),
              <.span(
                ^.className := ActorTabNavStyles.titleLink,
                ^.dangerouslySetInnerHTML := I18n.t(s"actor-profile.contributions-tab")
              )()
            )

            def isLeftToActiveTab(tabName: String) =
              tabNames.indexOf(tabName) == tabNames.indexOf(self.props.wrapped.activeTab) - 1
            def isFirstTab(tabName: String) =
              tabNames.indexOf(tabName) == 0

            def isActive(tabName: String) = self.props.wrapped.activeTab == tabName

            def tabButton(tabName: String, content: Element) =
              <.button(
                ^.className := js
                  .Array(
                    ActorTabNavStyles.tab,
                    ActorTabNavStyles.tabSelection(isActive(tabName)),
                    ActorTabNavStyles.isLeftToActiveTab(isLeftToActiveTab(tabName)),
                    ActorTabNavStyles.withLeftBorder(isFirstTab(tabName) || isActive(tabName))
                  ),
                ^.onClick := changeTab(tabName)
              )(content)

            tabNames.zip(tabContents).map {
              case (tabName, content) => tabButton(tabName, content)
            }
          }

          <("ActorTabNav")()(
            <.nav(^.className := js.Array(LayoutRulesStyles.centeredRow, ActorTabNavStyles.tabWrapper))(
              listTabs,
              <.div(^.className := ActorTabNavStyles.sep)()
            ),
            <.style()(ActorTabNavStyles.render[String])
          )
        }
      )
}

object ActorTabNavStyles extends StyleSheet.Inline {
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
      unsafeChild(".fa")(color(ThemeStyles.ThemeColor.assertive)),
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

  val isLeftToActiveTab: Boolean => StyleA = styleF.bool(
    isLeft =>
      if (isLeft) {
        styleS(borderRight(0.em))
      } else {
        styleS()
    }
  )

  val withLeftBorder: Boolean => StyleA = styleF.bool(
    withLeft =>
      if (!withLeft) {
        styleS(borderLeft(0.em))
      } else {
        styleS()
    }
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
