package org.make.services.proposal

import org.make.front.models.Source

import scala.scalajs.js
import scala.scalajs.js.JSConverters._

final case class RegisterProposalRequest(content: String)

@js.native
trait JsRegisterProposalRequest extends js.Object {
  val content: String
}

object JsRegisterProposalRequest {
  def apply(registerProposalRequest: RegisterProposalRequest): JsRegisterProposalRequest = {
    js.Dynamic.literal(content = registerProposalRequest.content).asInstanceOf[JsRegisterProposalRequest]
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

@js.native
trait JsOrder extends js.Object {
  val shortName: String
}

object JsOrder {
  def apply(order: Order): JsOrder = {
    js.Dynamic.literal(shortName = order.shortName).asInstanceOf[JsOrder]
  }
}

final case class SortOptionRequest(field: String, mode: Option[Order])

@js.native
trait JsSortOptionRequest extends js.Object {
  val field: String
  val mode: js.UndefOr[JsOrder]
}

object JsSortOptionRequest {
  def apply(sortOptionRequest: SortOptionRequest): JsSortOptionRequest = {
    js.Dynamic
      .literal(field = sortOptionRequest.field, mode = sortOptionRequest.mode.map(JsOrder.apply).orUndefined)
      .asInstanceOf[JsSortOptionRequest]
  }
}
final case class ContextRequest(operation: Option[String] = None,
                                location: Option[String] = None,
                                source: Option[String] = None,
                                question: Option[String] = None)

@js.native
trait JsContextRequest extends js.Object {
  val operation: js.UndefOr[String]
  val location: js.UndefOr[String]
  val source: js.UndefOr[String]
  val question: js.UndefOr[String]
}

object JsContextRequest {
  def apply(contextRequest: ContextRequest): JsContextRequest = {
    js.Dynamic
      .literal(
        operation = contextRequest.operation.orUndefined,
        location = contextRequest.location.orUndefined,
        source = contextRequest.source.orUndefined,
        question = contextRequest.question.orUndefined
      )
      .asInstanceOf[JsContextRequest]
  }
}

case class SearchRequest(themesIds: Option[Seq[String]] = None,
                         operationsIds: Option[Seq[String]] = None,
                         tagsIds: Option[Seq[String]] = None,
                         labelsIds: Option[Seq[String]] = None,
                         content: Option[String] = None,
                         slug: Option[String] = None,
                         trending: Option[String] = None,
                         seed: Option[Int] = None,
                         context: Option[ContextRequest] = None,
                         sort: Seq[SortOptionRequest] = Seq.empty,
                         limit: Option[Int],
                         skip: Option[Int] = None,
                         isRandom: Option[Boolean] = None)

@js.native
trait JsSearchRequest extends js.Object {
  val themesIds: js.UndefOr[js.Array[String]]
  val operationsIds: js.UndefOr[js.Array[String]]
  val tagsIds: js.UndefOr[js.Array[String]]
  val labelsIds: js.UndefOr[js.Array[String]]
  val content: js.UndefOr[String]
  val slug: js.UndefOr[String]
  val trending: js.UndefOr[String]
  val seed: js.UndefOr[Int]
  val context: js.UndefOr[JsContextRequest]
  val sort: js.Array[SortOptionRequest]
  val limit: js.UndefOr[Int]
  val skip: js.UndefOr[Int]
  val isRandom: js.UndefOr[Boolean]
}

object JsSearchRequest {
  def apply(searchRequest: SearchRequest): JsSearchRequest = {

    js.Dynamic
      .literal(
        themesIds = searchRequest.themesIds.map(_.toJSArray).orUndefined,
        operationsIds = searchRequest.operationsIds.map(_.toJSArray).orUndefined,
        tagsIds = searchRequest.tagsIds.map(_.toJSArray).orUndefined,
        labelsIds = searchRequest.labelsIds.map(_.toJSArray).orUndefined,
        content = searchRequest.content.orUndefined,
        slug = searchRequest.slug.orUndefined,
        trending = searchRequest.trending.orUndefined,
        seed = searchRequest.seed.orUndefined,
        context = searchRequest.context.map(JsContextRequest.apply).orUndefined,
        sort = searchRequest.sort,
        limit = searchRequest.limit.orUndefined,
        skip = searchRequest.skip.orUndefined,
        isRandom = searchRequest.isRandom.orUndefined
      )
      .asInstanceOf[JsSearchRequest]
  }
}

final case class VoteRequest(voteKey: String)
final case class QualificationRequest(voteKey: String, qualificationKey: String)

@js.native
trait JsVoteRequest extends js.Object {
  val voteKey: String
}

object JsVoteRequest {
  def apply(voteRequest: VoteRequest): JsVoteRequest = {
    js.Dynamic.literal(voteKey = voteRequest.voteKey).asInstanceOf[JsVoteRequest]
  }
}

@js.native
trait JsQualificationRequest extends js.Object {
  val voteKey: String
  val qualificationKey: String
}

object JsQualificationRequest {
  def apply(qualificationRequest: QualificationRequest): JsQualificationRequest = {
    js.Dynamic
      .literal(voteKey = qualificationRequest.voteKey, qualificationKey = qualificationRequest.qualificationKey)
      .asInstanceOf[JsQualificationRequest]
  }
}
