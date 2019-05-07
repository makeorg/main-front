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

package org.make.front.models

sealed trait Location { val name: String }

object Location {
  case class Sequence(sequenceId: SequenceId) extends Location { override val name = s"sequence ${sequenceId.value}" }
  case class ThemePage(themeId: ThemeId) extends Location { override val name = s"theme_page ${themeId.value}" }
  case class OperationPage(operationId: OperationId) extends Location {
    override val name = s"operation_page ${operationId.value}"
  }
  case class ProposalPage(proposalId: ProposalId) extends Location {
    override val name = s"proposal_page ${proposalId.value}"
  }
  case object Homepage extends Location { override val name = "homepage" }
  case object SearchResultsPage extends Location { override val name = "search_results" }
  case class OrganisationPage(slug: String) extends Location { override val name = s"organisation $slug" }
  case class UnknownLocation(path: String = "") extends Location { override val name = s"unknown_location $path" }

  def firstByPrecedence(location: Option[Location],
                        sequence: Option[Sequence],
                        themePage: Option[ThemePage],
                        operationPage: Option[OperationPage],
                        fallback: UnknownLocation): Location =
    location.orElse(sequence).orElse(themePage).orElse(operationPage).getOrElse(fallback)
}
