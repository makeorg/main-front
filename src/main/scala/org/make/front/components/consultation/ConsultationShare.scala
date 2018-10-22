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

package org.make.front.components.consultation

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM.{<, _}
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import org.make.front.Main.CssSettings._
import org.make.front.components.Components._
import org.make.front.models.{OperationExpanded => OperationModel}
import org.make.front.components.share.ShareProposal.ShareProps
import org.make.front.facades.I18n
import org.make.front.facades.Unescape.unescape
import org.make.front.styles.ThemeStyles
import org.make.front.styles.base.{LayoutRulesStyles, TextStyles}
import org.make.front.styles.utils._

import scala.scalajs.js

object ConsultationShare {

  case class ConsultationShareProps(operation: OperationModel)

  lazy val reactClass: ReactClass =
    React
      .createClass[ConsultationShareProps, Unit](
        displayName = "ConsultationShare",
        render = { self =>
          <.article(^.className := js.Array(ConsultationShareStyles.wrapper, LayoutRulesStyles.centeredRow))(
            <.h3(^.className := ConsultationShareStyles.title)(unescape(I18n.t("operation.share.title"))),
            if (self.props.wrapped.operation.featureSettings.share) {
              <.ShareComponent(^.wrapped := ShareProps(operation = self.props.wrapped.operation))()
            },
            <.style()(ConsultationShareStyles.render[String])
          )
        }
      )

}

object ConsultationShareStyles extends StyleSheet.Inline {
  import dsl._

  val wrapper: StyleA =
    style(
      backgroundColor(ThemeStyles.BackgroundColor.white),
      boxShadow := "0 1px 1px 0 rgba(0,0,0,0.50)",
      padding(ThemeStyles.SpacingValue.small.pxToEm()),
      marginTop(ThemeStyles.SpacingValue.small.pxToEm()),
      ThemeStyles.MediaQueries.beyondLargeMedium(padding(20.pxToEm()), marginTop(20.pxToEm()))
    )

  val title: StyleA =
    style(
      TextStyles.title,
      fontSize(15.pxToEm()),
      lineHeight(1),
      style(paddingBottom(ThemeStyles.SpacingValue.small.pxToEm(15))),
      ThemeStyles.MediaQueries.beyondSmall(
        fontSize(18.pxToEm()),
        paddingBottom(15.pxToEm(18)),
        marginBottom(20.pxToEm(18)),
        borderBottom(1.pxToEm(18), solid, ThemeStyles.BorderColor.veryLight)
      )
    )
}
