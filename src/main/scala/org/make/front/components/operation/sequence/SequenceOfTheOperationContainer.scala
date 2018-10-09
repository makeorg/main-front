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

package org.make.front.components.operation.sequence

import io.github.shogowada.scalajs.reactjs.React.Props
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.redux.ReactRedux
import io.github.shogowada.scalajs.reactjs.redux.Redux.Dispatch
import io.github.shogowada.scalajs.reactjs.router.RouterProps._
import org.make.front.actions.SetCountry
import org.make.front.components.AppState
import org.make.front.components.operation.sequence.SequenceLoader.SequenceLoaderProps
import org.make.front.helpers.{Normalizer, QueryString}
import org.make.front.models.{
  Operation,
  ProposalId,
  SequenceId,
  Tag,
  OperationExpanded => OperationModel,
  Sequence          => SequenceModel
}
import org.make.services.operation.OperationService
import org.make.services.proposal.ProposalService
import org.make.services.sequence.SequenceService
import org.make.services.tag.TagService

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.scalajs.js

object SequenceOfTheOperationContainer {

  lazy val reactClass: ReactClass = ReactRedux.connectAdvanced(selectorFactory)(SequenceLoader.reactClass)

  def selectorFactory: (Dispatch) => (AppState, Props[Unit]) => SequenceLoaderProps =
    (dispatch: Dispatch) => { (state: AppState, props: Props[Unit]) =>
      {
        val operationSlug: String = Normalizer.normalizeSlug(props.`match`.params("operationSlug"))
        // toDo remove default "FR" when backward compatibility not anymore required
        val countryCode: String = props.`match`.params.get("country").getOrElse("FR").toUpperCase
        val countryChanged = state.country != countryCode
        if (countryChanged) {
          dispatch(SetCountry(countryCode))
        }

        val queryParams: Map[String, String] = QueryString.parse(props.location.search)
        val configParameter: String = queryParams.getOrElse("config", "")

        val config: Map[String, Boolean] = configParameter
          .split(",")
          .map { slideConfig =>
            slideConfig.split(":") match {
              case Array(slideName, "on")  => slideName -> true
              case Array(slideName, "off") => slideName -> false
              case _                       => "" -> false
            }
          }
          .toMap

        val search = org.scalajs.dom.window.location.search
        def searchParam(paramName: String): Option[String] =
          (if (search.startsWith("?")) { search.substring(1) } else { search })
            .split("&")
            .map(_.split("="))
            .find {
              case Array(`paramName`, _) => true
              case _                     => false
            }
            .flatMap {
              case Array(_, value) => Some(value)
              case _               => None
            }

        val firstProposalId = searchParam("firstProposalId")
        @Deprecated
        val firstProposalSlug = searchParam("firstProposal")

        def startSequence(sequenceId: SequenceId)(proposals: js.Array[ProposalId]): Future[SequenceModel] = {
          val firstProposal = firstProposalId.map { proposalId =>
            ProposalService
              .searchProposals(
                proposalIds = Some(js.Array(ProposalId(proposalId))),
                language = Some(state.language),
                country = Some(state.country)
              )
              .map(_.results.map(_.id))

          }.orElse(firstProposalSlug.map { slug =>
              ProposalService
                .searchProposals(slug = Some(slug), language = Some(state.language), country = Some(state.country))
                .map(_.results.map(_.id))
            })
            .getOrElse(Future.successful(js.Array()))

          firstProposal.flatMap { proposalsToInclude =>
            SequenceService.startSequenceById(
              sequenceId,
              proposalsToInclude ++ proposals.filter(id => !proposalsToInclude.contains(id))
            )
          }
        }

        val futureMaybeOperationAndSequence: () => Future[Option[(OperationModel, SequenceModel)]] = () => {
          val operationAndTags: Future[(Option[Operation], js.Array[Tag])] = for {
            operation <- OperationService.getOperationBySlugAndCountry(operationSlug, state.country)
            tags      <- TagService.getTags
          } yield (operation, tags)
          operationAndTags.map {
            case (maybeOperation, tagsList) =>
              OperationModel.getOperationExpandedFromOperation(maybeOperation, tagsList, state.country)
          }.flatMap {
            case None => Future.successful(None)
            case Some(operation) =>
              startSequence(operation.landingSequenceId)(js.Array())
                .map(sequence => Some((operation, sequence)))
          }
        }

        val load: () => Future[Option[(OperationModel, SequenceModel)]] = if (countryChanged) { () =>
          {
            Future.successful(None)
          }
        } else {
          futureMaybeOperationAndSequence
        }

        SequenceLoaderProps(
          loadSequenceAndOperation = load,
          operationSlug = operationSlug,
          language = state.language,
          country = state.country,
          redirectHome = () => props.history.push("/"),
          isConnected = state.connectedUser.isDefined,
          startSequence = startSequence,
          config = config
        )

      }
    }
}
