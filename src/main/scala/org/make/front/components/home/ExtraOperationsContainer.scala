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

package org.make.front.components.home
import io.github.shogowada.scalajs.reactjs.React.Props
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.redux.ReactRedux
import io.github.shogowada.scalajs.reactjs.redux.Redux.Dispatch
import org.make.front.components.AppState
import org.make.front.models.{GradientColor, ExtraOperations => ExtraOperationsModel}

import scala.scalajs.js

object ExtraOperationsContainer {

  lazy val reactClass: ReactClass = ReactRedux.connectAdvanced(selectorFactory)(ExtraOperations.reactClass)

  def selectorFactory: (Dispatch) => (AppState, Props[Unit]) => ExtraOperations.ExtraOperationsProps =
    (dispatch: Dispatch) => { (state: AppState, props: Props[Unit]) =>
      ExtraOperations.ExtraOperationsProps(
        operations = js.Array(
          ExtraOperationsModel(
            title = "Comment faire émerger des champions européens du numérique ?",
            operationGradient = "linear-gradient(175deg, #7db7e3, #5da113)",
            operationLink = "/#/FR/consultation/european-digital-champions/consultation"
          ),
          ExtraOperationsModel(
            title = "Comment vous aider à construire votre avenir en Hauts de France ?",
            operationGradient = "linear-gradient(352deg, #fea8fe, #35bfff)",
            operationLink = "/#/FR/consultation/jeunesse-hautsdefrance/consultation"
          ),
          ExtraOperationsModel(
            title = "Comment inciter les jeunes à mieux prendre soin de leur santé ?",
            operationGradient = "linear-gradient(352deg, #ffb662, #ff5757)",
            operationLink = "https://app.make.org/FR-fr/consultation/prevention-jeunes/consultation"
          )
        )
      )
    }
}
