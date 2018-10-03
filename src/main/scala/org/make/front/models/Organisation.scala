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

import org.make.front.helpers.UndefToOption.undefToOption
import scala.scalajs.js

@js.native
trait OrganisationResponse extends js.Object {
  val organisationId: String
  val organisationName: js.UndefOr[String]
  val slug: js.UndefOr[String]
  val avatarUrl: js.UndefOr[String]
  val description: js.UndefOr[String]
  val publicProfile: Boolean
  val proposalsCount: js.UndefOr[Int]
  val votesCount: js.UndefOr[Int]
  val country: String
  val language: String
}
@js.native
trait OrganisationSearchResultResponse extends js.Object {
  val total: Int
  val results: js.Array[OrganisationResponse]
}

case class OrganisationSearchResult(total: Int, results: js.Array[Organisation])

object OrganisationSearchResult {
  def apply(searchResultResponse: OrganisationSearchResultResponse): OrganisationSearchResult = {
    OrganisationSearchResult(
      total = searchResultResponse.total,
      results = searchResultResponse.results.map(Organisation.apply)
    )
  }
}

case class Organisation(organisationId: UserId,
                        organisationName: Option[String],
                        slug: Option[String],
                        avatarUrl: Option[String],
                        description: Option[String],
                        publicProfile: Boolean,
                        proposalsCount: Option[Int],
                        votesCount: Option[Int],
                        language: String,
                        country: String)
object Organisation {
  def apply(organisationResponse: OrganisationResponse): Organisation =
    Organisation(
      organisationId = UserId(organisationResponse.organisationId),
      organisationName = undefToOption(organisationResponse.organisationName),
      slug = undefToOption(organisationResponse.slug),
      avatarUrl = undefToOption(organisationResponse.avatarUrl),
      description = undefToOption(organisationResponse.description),
      publicProfile = organisationResponse.publicProfile,
      proposalsCount = undefToOption(organisationResponse.proposalsCount),
      votesCount = undefToOption(organisationResponse.votesCount),
      language = organisationResponse.language,
      country = organisationResponse.country
    )

}
