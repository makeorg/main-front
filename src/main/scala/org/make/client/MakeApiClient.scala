package org.make.client

import io.circe.generic.auto._
import io.circe.parser._
import io.circe.syntax._
import io.circe.{Decoder, Printer}
import io.github.shogowada.statictags.MediaTypes
import org.make.core.URI._
import org.make.front.facades.Configuration
import org.make.front.models.Token
import org.scalajs.dom.XMLHttpRequest
import org.scalajs.dom.ext.Ajax.InputData
import org.scalajs.dom.ext.{Ajax, AjaxException}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.{Future, Promise}
import scala.scalajs.js
import scala.util.{Failure, Success, Try}

object MakeApiClient extends Client {

  var customHeaders: Map[String, String] = Map.empty
  def defaultHeaders: Map[String, String] = {
    Map(
      //TODO: X-Forwarded-For Header is set for dev only. Remove in prod.
      "X-Forwarded-For" -> "0.0.0.0",
      "Accept" -> MediaTypes.`application/json`,
      "Content-Type" -> "application/json;charset=UTF-8"
    ) ++
      customHeaders ++
      MakeApiClient.getToken.map { token =>
        Map("Authorization" -> s"${token.token_type} ${token.access_token}")
      }.getOrElse(Map.empty)
  }

  override lazy val baseUrl: String = Configuration.apiUrl

  private var token: Option[Token] = None
  def getToken: Option[Token] = token
  def setToken(newToken: Option[Token]): Unit = token = newToken
  def removeToken(): Unit = token = None
  def isAuthenticated: Boolean = token.isDefined

  val printer: Printer = Printer.noSpaces
  val maxTimeout: Int = 5000
  val withCredentials: Boolean = true

  val themeIdHeader: String = "x-make-theme-id"
  val operationHeader: String = "x-make-operation"
  val sourceHeader: String = "x-make-source"
  val locationHeader: String = "x-make-location"
  val questionHeader: String = "x-make-question"
  val languageHeader: String = "x-make-language"
  val countryHeader: String = "x-make-country"

  private def XHRResponseTo[ENTITY <: js.Object](responseTry: Try[XMLHttpRequest], promise: Promise[ENTITY]): Promise[ENTITY] = {
    responseTry match {
      case Success(response) =>
        promise.success(response.asInstanceOf[ENTITY])
      case Failure(AjaxException(response: XMLHttpRequest)) =>
        response.status match {
          case 400 =>
            promise.failure(BadRequestHttpException(response.asInstanceOf[Seq[ValidationError]]))
          case 401 => promise.failure(UnauthorizedHttpException)
          case 403 => promise.failure(ForbiddenHttpException)
          case 404 => promise.failure(NotFoundHttpException)
          case 500 => promise.failure(InternalServerHttpException)
          case 502 => promise.failure(BadGatewayHttpException)
          case _   => promise.failure(NotImplementedHttpException)
        }
    }
  }

  private def urlFrom(apiEndpoint: String, urlParams: Seq[(String, Any)] = Seq.empty): String =
    (baseUrl / apiEndpoint).addParams(urlParams)

  override def get[ENTITY <: js.Object](
    apiEndpoint: String = "",
    urlParams: Seq[(String, Any)] = Seq.empty,
    headers: Map[String, String] = Map.empty
  ): Future[ENTITY] = {
    val promiseReturn = Promise[ENTITY]()
    Ajax
      .get(
        url = urlFrom(apiEndpoint, urlParams),
        timeout = maxTimeout,
        headers = defaultHeaders ++ headers,
        withCredentials = withCredentials
      )
      .onComplete(responseTry => XHRResponseTo(responseTry, promiseReturn))
    promiseReturn.future
  }

  override def post[ENTITY <: js.Object](
    apiEndpoint: String = "",
    urlParams: Seq[(String, Any)] = Seq.empty,
    data: InputData = "",
    headers: Map[String, String] = Map.empty
  ): Future[ENTITY] = {
    val promiseReturn = Promise[ENTITY]()
    Ajax
      .post(
        url = urlFrom(apiEndpoint, urlParams),
        data = data,
        timeout = maxTimeout,
        headers = defaultHeaders ++ headers,
        withCredentials = withCredentials
      )
      .onComplete(responseTry => XHRResponseTo(responseTry, promiseReturn))
    promiseReturn.future
  }

  override def put[ENTITY <: js.Object](apiEndpoint: String,
                           urlParams: Seq[(String, Any)],
                           data: InputData,
                           headers: Map[String, String]): Future[ENTITY] = {
    val promiseReturn = Promise[ENTITY]()
    Ajax
      .put(
        url = urlFrom(apiEndpoint, urlParams),
        data = data,
        timeout = maxTimeout,
        headers = defaultHeaders ++ headers,
        withCredentials = withCredentials
      )
      .onComplete(responseTry => XHRResponseTo(responseTry, promiseReturn))
    promiseReturn.future
  }

  override def patch[ENTITY <: js.Object](
    apiEndpoint: String,
    urlParams: Seq[(String, Any)],
    data: InputData,
    headers: Map[String, String]
  ): Future[ENTITY] = {
    val promiseReturn = Promise[ENTITY]()
    Ajax
      .apply(
        method = "PATCH",
        url = urlFrom(apiEndpoint, urlParams),
        data = data,
        timeout = maxTimeout,
        headers = defaultHeaders ++ headers,
        withCredentials = withCredentials,
        responseType = ""
      )
      .onComplete(responseTry => XHRResponseTo(responseTry, promiseReturn))
    promiseReturn.future
  }

  override def delete[ENTITY <: js.Object](
    apiEndpoint: String,
    urlParams: Seq[(String, Any)],
    data: InputData,
    headers: Map[String, String]
  ): Future[ENTITY] = {
    val promiseReturn = Promise[ENTITY]()
    Ajax
      .delete(
        url = urlFrom(apiEndpoint, urlParams),
        data = data,
        timeout = maxTimeout,
        headers = defaultHeaders ++ headers,
        withCredentials = withCredentials
      )
      .onComplete(responseTry => XHRResponseTo(responseTry, promiseReturn))
    promiseReturn.future
  }

  def authenticate(username: String, password: String): Future[Boolean] = {
    if (MakeApiClient.isAuthenticated) {
      Future.successful(true)
    } else {
      askForAccessToken(username, password)
    }
  }

  private def askForAccessToken(username: String,
                                password: String): Future[Boolean] = {
    post[Token](
      "oauth" / "make_access_token",
      data = "".paramsToString(Seq("username" -> username, "password" -> password, "grant_type" -> "password")),
      headers = Map("Content-Type" -> MediaTypes.`application/x-www-form-urlencoded`)
    ).map { newToken =>
      MakeApiClient.setToken(Option(newToken))
      MakeApiClient.isAuthenticated
    }
  }

  def authenticateSocial(provider: String, token: String): Future[Boolean] = {
    if (MakeApiClient.isAuthenticated) {
      Future.successful(true)
    } else {
      askForAccessTokenSocial(provider, token)
    }
  }

  private def askForAccessTokenSocial(provider: String,
                                      token: String): Future[Boolean] = {
    post[Token](
      "user" / "login" / "social",
      data = Map("provider" -> provider, "token" -> token).asJson.pretty(MakeApiClient.printer)
    ).map { newToken =>
      MakeApiClient.setToken(Option(newToken))
      MakeApiClient.isAuthenticated
    }
  }

  def logout(): Future[Unit] =
    Ajax
      .post(url = urlFrom("logout"), timeout = maxTimeout, headers = defaultHeaders, withCredentials = withCredentials)
      .map(_ => MakeApiClient.removeToken())

}
