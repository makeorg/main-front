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

package org.make.front.components.proposal

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM.{<, _}
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import org.make.front.components.Components._
import org.make.front.Main.CssSettings._
import org.make.front.styles.utils._
import org.make.front.facades.{I18n, Replacements}
import org.make.front.models.OrganisationInfo
import org.make.front.styles.ThemeStyles
import org.make.front.styles.base.TextStyles

import scala.scalajs.js

object ProposalActorVoted {
  final case class ProposalActorVotedProps(organisations: Seq[OrganisationInfo])

  val reactClass: ReactClass =
    React
      .createClass[ProposalActorVotedProps, Unit](
        displayName = "ProposalActorVoted",
        render = (self) => {
          val organisationElementList: Seq[Any] =
            self.props.wrapped.organisations.flatMap(_.organisationName).map { name =>
              name
            } match {
              case Seq(organisationElement) => Seq(organisationElement)
              case organisationElements if organisationElements.length > 1 =>
                organisationElements
                  .dropRight(1)
                  .foldLeft(Seq.empty[Any]) { (a, b) =>
                    a :+ b :+ ", "
                  }
                  .dropRight(1) :+ I18n.t("proposal.actor-voted-and") :+ organisationElements.last
              case _ => Seq.empty
            }

          <.div(^.className := js.Array(ProposalActorStyles.wrapper, TextStyles.smallerText))(
            if (organisationElementList.nonEmpty) {
              <.p()(
                organisationElementList :+ I18n
                  .t("proposal.actor-voted", Replacements(("count", self.props.wrapped.organisations.length.toString)))
              )
            },
            <.style()(ProposalActorStyles.render[String])
          )

        }
      )
}

object ProposalActorStyles extends StyleSheet.Inline {
  import dsl._

  val wrapper: StyleA =
    style(
      paddingLeft(ThemeStyles.SpacingValue.smaller.pxToEm()),
      borderLeft(3.pxToEm(), solid, ThemeStyles.BorderColor.veryLight),
      marginTop(ThemeStyles.SpacingValue.small.pxToEm()),
      color(ThemeStyles.TextColor.lighter)
    )
}
