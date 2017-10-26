package org.make.services.proposal

import org.make.front.models.Source
import scala.scalajs.js
import js.JSConverters._
import scala.scalajs.js.UndefOr

final case class RegisterProposalRequest(content: String)
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
    js.Dynamic.literal(
      shortName = order.shortName
    ).asInstanceOf[JsOrder]
  }
}

final case class SortOptionRequest(field: String, mode: Option[Order])

@js.native
trait JsSortOptionRequest extends js.Object {
  val field: String
  val mode: js.UndefOr[JsOrder]
}

object JsSortOptionRequest {
  def appl(sortOptionRequest: SortOptionRequest): JsSortOptionRequest = {
    js.Dynamic.literal(
      field = sortOptionRequest.field,
      mode = sortOptionRequest.mode.map(JsOrder.apply).orUndefined
    ).asInstanceOf[JsSortOptionRequest]
  }
}
final case class ContextRequest(operation: Option[String] = None,
                                location: Option[String] = None,
                                source: Option[String] = Some(Source.Core.name),
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
    js.Dynamic.literal(
      operation = contextRequest.operation.orUndefined,
      location = contextRequest.location.orUndefined,
      source = contextRequest.source.orUndefined,
      question = contextRequest.question.orUndefined
    ).asInstanceOf[JsContextRequest]
  }
}

case class SearchRequest(themesIds: Option[Seq[String]] = None,
                               operationsIds: Option[Seq[String]] = None,
                               tagsIds: Option[Seq[String]] = None,
                               labelsIds: Option[Seq[String]] = None,
                               content: Option[String] = None,
                               slug: Option[String] = None,
                               trending: Option[String] = None,
                               context: Option[ContextRequest] = None,
                               sort: Seq[SortOptionRequest] = Seq.empty,
                               limit: Option[Int],
                               skip: Option[Int] = None)

@js.native
trait JsSearchRequest extends js.Object {
  val themesIds: js.UndefOr[js.Array[String]]
  val operationsIds: js.UndefOr[js.Array[String]]
  val tagsIds: js.UndefOr[js.Array[String]]
  val labelsIds: js.UndefOr[js.Array[String]]
  val content: js.UndefOr[String]
  val slug: js.UndefOr[String]
  val trending: js.UndefOr[String]
  val context: js.UndefOr[JsContextRequest]
  val sort: js.Array[SortOptionRequest]
  val limit: js.UndefOr[Int]
  val skip: js.UndefOr[Int]
}

object JsSearchRequest {
  def apply(searchRequest: SearchRequest): JsSearchRequest = {

    js.Dynamic.literal(
      themesIds = searchRequest.themesIds.map(_.toJSArray).orUndefined,
      operationsIds = searchRequest.operationsIds.map(_.toJSArray).orUndefined,
      tagsIds = searchRequest.tagsIds.map(_.toJSArray).orUndefined,
      labelsIds = searchRequest.labelsIds.map(_.toJSArray).orUndefined,
      content = searchRequest.content.orUndefined,
      slug = searchRequest.slug.orUndefined,
      trending = searchRequest.trending.orUndefined,
      context = searchRequest.context.map(JsContextRequest.apply).orUndefined,
      sort = searchRequest.sort,
      limit = searchRequest.limit.orUndefined,
      skip = searchRequest.skip.orUndefined
    ).asInstanceOf[JsSearchRequest]
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
    js.Dynamic.literal(
      voteKey = voteRequest.voteKey
    ).asInstanceOf[JsVoteRequest]
  }
}

@js.native
trait JsQualificationRequest extends js.Object {
  val voteKey: String
  val qualificationKey: String
}

object JsQualificationRequest {
  def apply(qualificationRequest: QualificationRequest): JsQualificationRequest = {
    js.Dynamic.literal(
      voteKey = qualificationRequest.voteKey,
      qualificationKey = qualificationRequest.qualificationKey
    ).asInstanceOf[JsQualificationRequest]
  }
}
