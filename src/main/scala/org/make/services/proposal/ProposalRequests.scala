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

package org.make.services.proposal

import scala.scalajs.js
import scala.scalajs.js.JSConverters._

final case class RegisterProposalRequest(content: String,
                                         country: String,
                                         language: String,
                                         operationId: Option[String])

@js.native
trait JsRegisterProposalRequest extends js.Object {
  val content: String
  val country: String
  val language: String
  val operationId: js.UndefOr[String]
}

object JsRegisterProposalRequest {
  def apply(registerProposalRequest: RegisterProposalRequest): JsRegisterProposalRequest = {
    js.Dynamic
      .literal(
        content = registerProposalRequest.content,
        country = registerProposalRequest.country,
        language = registerProposalRequest.language,
        operationId = registerProposalRequest.operationId.orUndefined
      )
      .asInstanceOf[JsRegisterProposalRequest]
  }
}

sealed trait Order { val shortName: String }

case object OrderAsc extends Order { override val shortName: String = "ASC" }
case object OrderDesc extends Order { override val shortName: String = "DESC" }

object Order {

  val orders: Map[String, Order] = Map(OrderAsc.shortName -> OrderAsc, OrderDesc.shortName -> OrderDesc)

  def matchOrder(order: String): Option[Order] =
    orders.get(order.toUpperCase)
}

final case class SortOptionRequest(field: String, mode: Option[Order])

final case class ContextRequest(operation: Option[String] = None,
                                location: Option[String] = None,
                                source: Option[String] = None,
                                question: Option[String] = None)

final case class VoteRequest(voteKey: String, proposalKey: String)
final case class QualificationRequest(voteKey: String, qualificationKey: String, proposalKey: String)

@js.native
trait JsVoteRequest extends js.Object {
  val voteKey: String
  val proposalKey: String
}

object JsVoteRequest {
  def apply(voteRequest: VoteRequest): JsVoteRequest = {
    js.Dynamic.literal(voteKey = voteRequest.voteKey, proposalKey = voteRequest.proposalKey).asInstanceOf[JsVoteRequest]
  }
}

@js.native
trait JsQualificationRequest extends js.Object {
  val voteKey: String
  val qualificationKey: String
  val proposalKey: String
}

object JsQualificationRequest {
  def apply(qualificationRequest: QualificationRequest): JsQualificationRequest = {
    js.Dynamic
      .literal(
        voteKey = qualificationRequest.voteKey,
        qualificationKey = qualificationRequest.qualificationKey,
        proposalKey = qualificationRequest.proposalKey
      )
      .asInstanceOf[JsQualificationRequest]
  }
}
