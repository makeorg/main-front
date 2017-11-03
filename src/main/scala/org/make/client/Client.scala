package org.make.client

import org.scalajs.dom.ext.Ajax.InputData

import scala.concurrent.Future
import scala.scalajs.js
import js.JSConverters._

trait Client {
  def baseUrl: String

  def get[ENTITY <: js.Object](apiEndpoint: String, urlParams: Seq[(String, Any)], headers: Map[String, String]): Future[ENTITY]

  def post[ENTITY <: js.Object](apiEndpoint: String, urlParams: Seq[(String, Any)], data: InputData, headers: Map[String, String]): Future[ENTITY]

  def put[ENTITY <: js.Object](apiEndpoint: String,
                  urlParams: Seq[(String, Any)],
                  data: InputData,
                  headers: Map[String, String] = Map.empty): Future[ENTITY]

  def patch[ENTITY <: js.Object](apiEndpoint: String,
                    urlParams: Seq[(String, Any)],
                    data: InputData,
                    headers: Map[String, String] = Map.empty): Future[ENTITY]

  def delete[ENTITY <: js.Object](
    apiEndpoint: String,
    urlParams: Seq[(String, Any)],
    data: InputData,
    headers: Map[String, String] = Map.empty
  ): Future[ENTITY]
}

trait HttpException extends Exception

case class ValidationError(field: String, message: Option[String])

trait ValidationFailedHttpException extends HttpException {
  val errors: Seq[ValidationError]
  override def getMessage: String = { errors.toJSArray.toString }
}

case class BadRequestHttpException(override val errors: Seq[ValidationError]) extends ValidationFailedHttpException
case object UnauthorizedHttpException extends HttpException
case object ForbiddenHttpException extends HttpException
case object NotFoundHttpException extends HttpException
case object InternalServerHttpException extends HttpException
case object BadGatewayHttpException extends HttpException
case object NotImplementedHttpException extends HttpException
case object TimeoutHttpException extends HttpException
