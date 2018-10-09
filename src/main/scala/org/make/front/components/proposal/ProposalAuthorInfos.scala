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
import io.github.shogowada.scalajs.reactjs.router.dom.RouterDOM._
import org.make.front.components.Components._
import org.make.front.facades.Localize.DateLocalizeOptions
import org.make.front.facades.Unescape.unescape
import org.make.front.facades.{I18n, Replacements}
import org.make.front.models.{Proposal => ProposalModel}
import org.make.front.styles.vendors.FontAwesomeStyles

import scala.scalajs.js

object ProposalAuthorInfos {

  final case class ProposalAuthorInfosProps(proposal: ProposalModel,
                                            country: Option[String],
                                            withCreationDate: Boolean = true)

  val reactClass: ReactClass =
    React
      .createClass[ProposalAuthorInfosProps, Unit](
        displayName = "ProposalAuthorInfos",
        render = self => {

          val proposal: ProposalModel = self.props.wrapped.proposal

          val age: String = if (proposal.author.age.getOrElse(0) != 0) {
            I18n
              .t("proposal.author-infos.age", Replacements(("age", s"${proposal.author.age.getOrElse("")}")))
          } else {
            ""
          }

          def formatAuthorName(maybeName: Option[String]): String = {
            maybeName.getOrElse(I18n.t("proposal.author-infos.anonymous")).toLowerCase.capitalize
          }

          if (proposal.author.organisationName.isDefined) {
            <.span(^.className := ProposalInfosStyles.infos)(
              (self.props.wrapped.proposal.author.organisationSlug, self.props.wrapped.country) match {
                case (Some(slug), Some(country)) =>
                  <.Link(^.className := ProposalInfosStyles.actorLink, ^.to := s"/$country/profile/$slug")(
                    formatAuthorName(proposal.author.organisationName)
                  )
                case (Some(slug), None) =>
                  <.Link(^.className := ProposalInfosStyles.actorLink, ^.to := s"/FR/profile/$slug")(
                    formatAuthorName(proposal.author.organisationName)
                  )
                case _ => formatAuthorName(proposal.author.organisationName)
              },
              <.i(^.className := js.Array(FontAwesomeStyles.checkCircle, ProposalInfosStyles.checkCircle))(),
              if (self.props.wrapped.withCreationDate) {
                Seq(
                  unescape("&nbsp;&#8226;&nbsp;"),
                  I18n.l(proposal.createdAt, DateLocalizeOptions("common.date.long"))
                )
              }
            )
          } else {
            <.span(^.className := ProposalInfosStyles.infos)(
              formatAuthorName(proposal.author.firstName),
              age,
              if (self.props.wrapped.withCreationDate) {
                Seq(
                  unescape("&nbsp;&#8226;&nbsp;"),
                  I18n.l(proposal.createdAt, DateLocalizeOptions("common.date.long"))
                )
              }
            )
          }
        }
      )
}
