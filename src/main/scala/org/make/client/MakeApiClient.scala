package org.make.client

import io.github.shogowada.statictags.MediaTypes
import org.make.core.URI._
import org.make.front.facades.Configuration
import org.make.front.models.{Token, TokenResponse}
import org.scalajs.dom.XMLHttpRequest
import org.scalajs.dom.ext.Ajax.InputData
import org.scalajs.dom.ext.{Ajax, AjaxException}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.{Future, Promise}
import scala.scalajs.js
import scala.scalajs.js.JSON
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

  val maxTimeout: Int = 3000
  val withCredentials: Boolean = true

  val themeIdHeader: String = "x-make-theme-id"
  val operationHeader: String = "x-make-operation"
  val sourceHeader: String = "x-make-source"
  val locationHeader: String = "x-make-location"
  val questionHeader: String = "x-make-question"
  val languageHeader: String = "x-make-language"
  val countryHeader: String = "x-make-country"
  val retryAfterTimeout: Int = 4

  case class RequestData(method: String,
                         url: String,
                         data: InputData = null,
                         timeout: Int = 0,
                         headers: Map[String, String] = Map.empty,
                         withCredentials: Boolean = false,
                         responseType: String = "")

  private def XHRResponseTo[ENTITY <: js.Object](responseTry: Try[XMLHttpRequest],
                                                 promise: Promise[ENTITY],
                                                 requestDataToRetry: Option[RequestData] = None,
                                                 tries: Int = 0): Promise[ENTITY] = {

    responseTry match {
      case Success(response) =>
        promise.success(scala.scalajs.js.JSON.parse(response.responseText).asInstanceOf[ENTITY])
      case Failure(ajaxException: AjaxException) if ajaxException.isTimeout => {
        requestDataToRetry match {
          case Some(requestData) if tries > 0 => {
            Ajax
              .apply(
                method = requestData.method,
                url = requestData.url,
                data = requestData.data,
                timeout = requestData.timeout,
                headers = requestData.headers,
                withCredentials = requestData.withCredentials,
                responseType = requestData.responseType
              )
              .onComplete(responseTry => XHRResponseTo(responseTry, promise, requestDataToRetry, tries - 1))
            promise
          }
          case _ => promise.failure(TimeoutHttpException)
        }
      }
      case Failure(AjaxException(response: XMLHttpRequest)) =>
        response.status match {
          case 400 =>
            promise.failure(
              BadRequestHttpException(
                scala.scalajs.js.JSON.parse(response.responseText).asInstanceOf[Seq[ValidationError]]
              )
            )
          case 401 => promise.failure(UnauthorizedHttpException)
          case 403 => promise.failure(ForbiddenHttpException)
          case 404 => promise.failure(NotFoundHttpException)
          case 500 => promise.failure(InternalServerHttpException)
          case 502 => promise.failure(BadGatewayHttpException)
          case _   => promise.failure(NotImplementedHttpException)
        }
      case Failure(e) =>
        promise.failure(InternalServerHttpException)
    }
  }

  private def urlFrom(apiEndpoint: String, urlParams: Seq[(String, Any)] = Seq.empty): String =
    (baseUrl / apiEndpoint).addParams(urlParams)

  override def get[ENTITY <: js.Object](apiEndpoint: String = "",
                                        urlParams: Seq[(String, Any)] = Seq.empty,
                                        headers: Map[String, String] = Map.empty): Future[ENTITY] = {
    val promiseReturn = Promise[ENTITY]()
    val requestData = RequestData(
      method = "GET",
      url = urlFrom(apiEndpoint, urlParams),
      timeout = maxTimeout,
      withCredentials = withCredentials,
      headers = defaultHeaders ++ headers
    )

    ajaxApply(requestData).onComplete(
      responseTry => XHRResponseTo(responseTry, promiseReturn, Some(requestData), retryAfterTimeout)
    )

    promiseReturn.future
  }

  private def ajaxApply(requestData: RequestData): Future[XMLHttpRequest] = {
    Ajax
      .apply(
        method = requestData.method,
        url = requestData.url,
        data = requestData.data,
        timeout = requestData.timeout,
        headers = requestData.headers,
        withCredentials = requestData.withCredentials,
        responseType = requestData.responseType
      )
  }

  override def post[ENTITY <: js.Object](apiEndpoint: String = "",
                                         urlParams: Seq[(String, Any)] = Seq.empty,
                                         data: InputData = "",
                                         headers: Map[String, String] = Map.empty): Future[ENTITY] = {
    val promiseReturn = Promise[ENTITY]()
    val requestData = RequestData(
      method = "POST",
      url = urlFrom(apiEndpoint, urlParams),
      data = data,
      timeout = maxTimeout,
      withCredentials = withCredentials,
      headers = defaultHeaders ++ headers
    )

    ajaxApply(requestData).onComplete(
      responseTry => XHRResponseTo(responseTry, promiseReturn, Some(requestData), retryAfterTimeout)
    )
    promiseReturn.future
  }

  override def put[ENTITY <: js.Object](apiEndpoint: String,
                                        urlParams: Seq[(String, Any)],
                                        data: InputData,
                                        headers: Map[String, String]): Future[ENTITY] = {
    val promiseReturn = Promise[ENTITY]()
    val requestData = RequestData(
      method = "PUT",
      url = urlFrom(apiEndpoint, urlParams),
      data = data,
      timeout = maxTimeout,
      withCredentials = withCredentials,
      headers = defaultHeaders ++ headers
    )
    ajaxApply(requestData).onComplete(
      responseTry => XHRResponseTo(responseTry, promiseReturn, Some(requestData), retryAfterTimeout)
    )
    promiseReturn.future
  }

  override def patch[ENTITY <: js.Object](apiEndpoint: String,
                                          urlParams: Seq[(String, Any)],
                                          data: InputData,
                                          headers: Map[String, String]): Future[ENTITY] = {
    val promiseReturn = Promise[ENTITY]()

    val requestData = RequestData(
      method = "PATCH",
      url = urlFrom(apiEndpoint, urlParams),
      data = data,
      timeout = maxTimeout,
      headers = defaultHeaders ++ headers,
      withCredentials = withCredentials
    )

    ajaxApply(requestData).onComplete(
      responseTry => XHRResponseTo(responseTry, promiseReturn, Some(requestData), retryAfterTimeout)
    )
    promiseReturn.future
  }

  override def delete[ENTITY <: js.Object](apiEndpoint: String,
                                           urlParams: Seq[(String, Any)],
                                           data: InputData,
                                           headers: Map[String, String]): Future[ENTITY] = {
    val promiseReturn = Promise[ENTITY]()

    val requestData = RequestData(
      method = "DELETE",
      url = urlFrom(apiEndpoint, urlParams),
      data = data,
      timeout = maxTimeout,
      headers = defaultHeaders ++ headers,
      withCredentials = withCredentials
    )
    ajaxApply(requestData).onComplete(
      responseTry => XHRResponseTo(responseTry, promiseReturn, Some(requestData), retryAfterTimeout)
    )

    promiseReturn.future
  }

  def authenticate(username: String, password: String): Future[Boolean] = {
    if (MakeApiClient.isAuthenticated) {
      Future.successful(true)
    } else {
      askForAccessToken(username, password)
    }
  }

  private def askForAccessToken(username: String, password: String): Future[Boolean] = {
    post[TokenResponse](
      "oauth" / "make_access_token",
      data = "".paramsToString(Seq("username" -> username, "password" -> password, "grant_type" -> "password")),
      headers = Map("Content-Type" -> MediaTypes.`application/x-www-form-urlencoded`)
    ).map(Token.apply).map { newToken =>
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

  private def askForAccessTokenSocial(provider: String, token: String): Future[Boolean] = {
    post[TokenResponse](
      "user" / "login" / "social",
      data = JSON.stringify(js.Dictionary("provider" -> provider, "token" -> token))
    ).map(Token.apply).map { newToken =>
      MakeApiClient.setToken(Option(newToken))
      MakeApiClient.isAuthenticated
    }
  }

  def logout(): Future[Unit] =
    Ajax
      .post(url = urlFrom("logout"), timeout = maxTimeout, headers = defaultHeaders, withCredentials = withCredentials)
      .map(_ => MakeApiClient.removeToken())

}
