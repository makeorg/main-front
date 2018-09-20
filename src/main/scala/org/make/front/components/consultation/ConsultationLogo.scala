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
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.router.WithRouter
import org.make.front.Main.CssSettings._
import org.make.front.components.Components._
import org.make.front.styles.utils._
import org.make.front.models.{OperationExpanded => OperationModel, OperationWording => OperationWordingModel}

object ConsultationLogo {

  case class ConsultationLogoProps(operation: OperationModel, language: String)

  lazy val reactClass: ReactClass =
    WithRouter(
      React
        .createClass[ConsultationLogoProps, Unit](displayName = "ConsultationLogo", render = (self) => {
          val consultation: OperationModel = self.props.wrapped.operation
          val wording: OperationWordingModel =
            self.props.wrapped.operation.getWordingByLanguageOrError(self.props.wrapped.language)
          <.h1()(
            <.img(
              ^.className := ConsultationLogoStyles.logo,
              ^.src := consultation.whiteLogoUrl,
              ^.alt := wording.title
            )(),
            <.style()(ConsultationLogoStyles.render[String])
          )
        })
    )
}

object ConsultationLogoStyles extends StyleSheet.Inline {
  import dsl._

  val logo: StyleA =
    style(maxHeight(90.pxToEm()), maxWidth(100.%%), display.block, marginLeft(auto), marginRight(auto))
}
