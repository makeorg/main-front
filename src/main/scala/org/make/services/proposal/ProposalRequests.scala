package org.make.services.proposal

import io.circe.{Decoder, Encoder, Json}
import org.make.front.models.Source

final case class RegisterProposalRequest(content: String)
sealed trait Order { val shortName: String }

case object OrderAsc extends Order { override val shortName: String = "ASC" }
case object OrderDesc extends Order { override val shortName: String = "DESC" }

object Order {
  implicit lazy val orderEncoder: Encoder[Order] = (order: Order) => Json.fromString(order.shortName)
  implicit lazy val orderDecoder: Decoder[Order] = Decoder.decodeString.map(
    order => matchOrder(order).getOrElse(throw new IllegalArgumentException(s"$order is not a Order"))
  )

  val orders: Map[String, Order] = Map(OrderAsc.shortName -> OrderAsc, OrderDesc.shortName -> OrderDesc)

  def matchOrder(order: String): Option[Order] =
    orders.get(order.toUpperCase)
}

final case class SortOptionRequest(field: String, mode: Option[Order])
final case class ContextRequest(operation: Option[String] = Some(""),
                                location: Option[String] = None,
                                source: Option[String] = Some(Source.Core.name),
                                question: Option[String] = None)
final case class SearchRequest(themesIds: Option[Seq[String]] = None,
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

final case class VoteRequest(voteKey: String)
final case class QualificationRequest(voteKey: String, qualificationKey: String)