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

package org.make.services.organisation
import org.make.client.{Client, MakeApiClient}
import org.make.core.URI._
import org.make.front.models.{Organisation, OrganisationSearchResult, OrganisationSearchResultResponse}
import org.make.services.ApiService
import org.make.services.proposal.{
  ProposalsResultWithUserVoteSeeded,
  ProposalsResultWithUserVoteSeededResponse,
  SearchResult,
  SearchResultResponse
}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.scalajs.js

object OrganisationService extends ApiService {

  override val resourceName: String = "organisations"
  var client: Client = MakeApiClient

  def getOrganisationBySlug(slug: String): Future[Option[Organisation]] = {
    client
      .get[OrganisationSearchResultResponse](
        apiEndpoint = resourceName,
        urlParams = js.Array(("slug", slug)),
        headers = Map.empty
      )
      .map(OrganisationSearchResult.apply)
      .map {
        case OrganisationSearchResult(0, _)                        => None
        case OrganisationSearchResult(total, results) if total > 0 => results.headOption
      }
  }

  def getOrganisationProposals(organisationId: String): Future[SearchResult] = {
    MakeApiClient
      .get[SearchResultResponse](
        apiEndpoint = resourceName / organisationId / "proposals",
        urlParams = js.Array(("sort", "createdAt"), ("order", "DESC"))
      )
      .map(SearchResult.apply)
  }

  def getOrganisationVotes(organisationId: String,
                           filterVotes: Seq[String],
                           filterQualifications: Seq[String]): Future[ProposalsResultWithUserVoteSeeded] = {

    MakeApiClient
      .get[ProposalsResultWithUserVoteSeededResponse](
        apiEndpoint = resourceName / organisationId / "votes",
        urlParams = js.Array(("votes", filterVotes.mkString(",")))
      )
      .map(ProposalsResultWithUserVoteSeeded.apply)
  }

}
