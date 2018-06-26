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

package org.make.client

import org.make.front.helpers.UndefToOption.undefToOption
import org.scalajs.dom.ext.Ajax.InputData

import scala.concurrent.Future
import scala.scalajs.js
import js.JSConverters._

trait Client {
  def baseUrl: String

  def get[ENTITY <: js.Object](apiEndpoint: String,
                               urlParams: js.Array[(String, Any)],
                               headers: Map[String, String]): Future[ENTITY]

  def post[ENTITY <: js.Object](apiEndpoint: String,
                                urlParams: js.Array[(String, Any)],
                                data: InputData,
                                headers: Map[String, String]): Future[ENTITY]

  def put[ENTITY <: js.Object](apiEndpoint: String,
                               urlParams: js.Array[(String, Any)],
                               data: InputData,
                               headers: Map[String, String] = Map.empty): Future[ENTITY]

  def patch[ENTITY <: js.Object](apiEndpoint: String,
                                 urlParams: js.Array[(String, Any)],
                                 data: InputData,
                                 headers: Map[String, String] = Map.empty): Future[ENTITY]

  def delete[ENTITY <: js.Object](apiEndpoint: String,
                                  urlParams: js.Array[(String, Any)],
                                  data: InputData,
                                  headers: Map[String, String] = Map.empty): Future[ENTITY]
}

trait HttpException extends Exception

@js.native
trait JsValidationError extends js.Object {
  val field: String
  val message: js.UndefOr[String]
}

case class ValidationError(field: String, message: Option[String])

object ValidationError {
  def apply(jsValidationError: JsValidationError): ValidationError = {
    ValidationError(field = jsValidationError.field, message = undefToOption(jsValidationError.message))
  }
}

trait ValidationFailedHttpException extends HttpException {
  val errors: js.Array[ValidationError]
  override def getMessage: String = { errors.toJSArray.toString }
}

case class BadRequestHttpException(override val errors: js.Array[ValidationError]) extends ValidationFailedHttpException
case object UnauthorizedHttpException extends HttpException
case object ForbiddenHttpException extends HttpException
case object NotFoundHttpException extends HttpException
case object InternalServerHttpException extends HttpException
case object BadGatewayHttpException extends HttpException
case object NotImplementedHttpException extends HttpException
case object TimeoutHttpException extends HttpException
