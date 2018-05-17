package org.make.client

import io.github.shogowada.statictags.MediaTypes
import org.make.core.URI._
import org.make.front.facades.Configuration
import org.make.front.models.{OperationId, Token, TokenResponse}
import org.scalajs.dom.XMLHttpRequest
import org.scalajs.dom.ext.Ajax.InputData
import org.scalajs.dom.ext.{Ajax, AjaxException}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.scalajs.js
import scala.scalajs.js.JSON

object MakeApiClient extends Client {

  override lazy val baseUrl: String = Configuration.apiUrl

  private var authToken: Option[Token] = None
  private def getToken: Option[Token] = authToken
  private def setToken(newToken: Option[Token]): Unit = authToken = newToken
  private def removeToken(): Unit = authToken = None
  private def isAuthenticated: Boolean = authToken.isDefined

  def getDefaultHeaders: Map[String, String] = {
    defaultHeaders ++
      MakeApiClient.getToken.map { authToken =>
        "Authorization" -> s"${authToken.token_type} ${authToken.access_token}"
      }
  }

  var defaultHeaders: Map[String, String] = {
    Map("Accept" -> MediaTypes.`application/json`, "Content-Type" -> "application/json;charset=UTF-8")
  }

  def addHeaders(headers: Map[String, String]): Unit = {
    defaultHeaders = defaultHeaders ++ headers
  }

  val themeIdHeader: String = "x-make-theme-id"
  val operationHeader: String = "x-make-operation"
  val sourceHeader: String = "x-make-source"
  val locationHeader: String = "x-make-location"
  val questionHeader: String = "x-make-question"
  val languageHeader: String = "x-make-language"
  val countryHeader: String = "x-make-country"

  val retryAfterTimeout: Int = 4

  val maxTimeout: Int = 9000
  val withCredentials: Boolean = true

  case class RequestData(method: String,
                         url: String,
                         data: InputData = null,
                         timeout: Int = 0,
                         headers: Map[String, String] = Map.empty,
                         withCredentials: Boolean = false,
                         responseType: String = "")

  private def retryOnFailure[T](fn: => Future[T], retries: Int): Future[T] = {
    fn.recoverWith {
      case ajaxException: AjaxException if ajaxException.isTimeout && retries > 0 => retryOnFailure(fn, retries - 1)
      case AjaxException(response: XMLHttpRequest) =>
        response.status match {
          case 400 =>
            val errors: js.Array[JsValidationError] =
              JSON.parse(response.responseText).asInstanceOf[js.Array[JsValidationError]]
            Future.failed(BadRequestHttpException(errors.map(ValidationError.apply)))
          case 401                => Future.failed(UnauthorizedHttpException)
          case 403                => Future.failed(ForbiddenHttpException)
          case 404                => Future.failed(NotFoundHttpException)
          case 500                => Future.failed(InternalServerHttpException)
          case 502 if retries > 0 => retryOnFailure(fn, retries - 1)
          case 502                => Future.failed(BadGatewayHttpException)
          case _                  => Future.failed(NotImplementedHttpException)
        }
      case other => Future.failed(other)
    }
  }

  private def urlFrom(apiEndpoint: String, urlParams: js.Array[(String, Any)] = js.Array()): String =
    (baseUrl / apiEndpoint).addParams(urlParams)

  override def get[ENTITY <: js.Object](apiEndpoint: String = "",
                                        urlParams: js.Array[(String, Any)] = js.Array(),
                                        headers: Map[String, String] = Map.empty): Future[ENTITY] = {
    val requestData = RequestData(
      method = "GET",
      url = urlFrom(apiEndpoint, urlParams),
      timeout = maxTimeout,
      withCredentials = withCredentials,
      headers = getDefaultHeaders ++ headers
    )

    retryOnFailure(MakeApiClient(requestData), retryAfterTimeout)
  }

  private def apply[ENTITY <: js.Object](requestData: RequestData): Future[ENTITY] = {
    Ajax(
      method = requestData.method,
      url = requestData.url,
      data = requestData.data,
      timeout = requestData.timeout,
      headers = requestData.headers,
      withCredentials = requestData.withCredentials,
      responseType = requestData.responseType
    ).map { response =>
      if (response.responseText.nonEmpty) JSON.parse(response.responseText).asInstanceOf[ENTITY]
      else response.response.asInstanceOf[ENTITY]
    }
  }

  override def post[ENTITY <: js.Object](apiEndpoint: String = "",
                                         urlParams: js.Array[(String, Any)] = js.Array(),
                                         data: InputData = "",
                                         headers: Map[String, String] = Map.empty): Future[ENTITY] = {
    val requestData = RequestData(
      method = "POST",
      url = urlFrom(apiEndpoint, urlParams),
      data = data,
      timeout = maxTimeout,
      withCredentials = withCredentials,
      headers = getDefaultHeaders ++ headers
    )

    retryOnFailure(MakeApiClient(requestData), retryAfterTimeout)
  }

  override def put[ENTITY <: js.Object](apiEndpoint: String,
                                        urlParams: js.Array[(String, Any)],
                                        data: InputData,
                                        headers: Map[String, String]): Future[ENTITY] = {
    val requestData = RequestData(
      method = "PUT",
      url = urlFrom(apiEndpoint, urlParams),
      data = data,
      timeout = maxTimeout,
      withCredentials = withCredentials,
      headers = getDefaultHeaders ++ headers
    )

    retryOnFailure(MakeApiClient(requestData), retryAfterTimeout)
  }

  override def patch[ENTITY <: js.Object](apiEndpoint: String,
                                          urlParams: js.Array[(String, Any)],
                                          data: InputData,
                                          headers: Map[String, String]): Future[ENTITY] = {
    val requestData = RequestData(
      method = "PATCH",
      url = urlFrom(apiEndpoint, urlParams),
      data = data,
      timeout = maxTimeout,
      headers = getDefaultHeaders ++ headers,
      withCredentials = withCredentials
    )

    retryOnFailure(MakeApiClient(requestData), retryAfterTimeout)
  }

  override def delete[ENTITY <: js.Object](apiEndpoint: String,
                                           urlParams: js.Array[(String, Any)],
                                           data: InputData,
                                           headers: Map[String, String]): Future[ENTITY] = {
    val requestData = RequestData(
      method = "DELETE",
      url = urlFrom(apiEndpoint, urlParams),
      data = data,
      timeout = maxTimeout,
      headers = getDefaultHeaders ++ headers,
      withCredentials = withCredentials
    )

    retryOnFailure(MakeApiClient(requestData), retryAfterTimeout)
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
      data = "".paramsToString(js.Array("username" -> username, "password" -> password, "grant_type" -> "password")),
      headers = Map("Content-Type" -> MediaTypes.`application/x-www-form-urlencoded`)
    ).map(Token.apply).map { newToken =>
      MakeApiClient.setToken(Option(newToken))
      MakeApiClient.isAuthenticated
    }
  }

  def authenticateSocial(provider: String,
                         token: String,
                         country: String,
                         language: String,
                         operationId: Option[OperationId]): Future[Boolean] = {
    if (MakeApiClient.isAuthenticated) {
      Future.successful(true)
    } else {
      askForAccessTokenSocial(
        provider = provider,
        token = token,
        country = country,
        language = language,
        operationId = operationId
      )
    }
  }

  private def askForAccessTokenSocial(provider: String,
                                      token: String,
                                      country: String,
                                      language: String,
                                      operationId: Option[OperationId]): Future[Boolean] = {
    val headers = MakeApiClient.getDefaultHeaders ++ operationId.map(op => MakeApiClient.operationHeader -> op.value)

    post[TokenResponse](
      "user" / "login" / "social",
      headers = headers,
      data = JSON.stringify(
        js.Dictionary("provider" -> provider, "token" -> token, "country" -> country, "language" -> language)
      )
    ).map(Token.apply).map { newToken =>
      MakeApiClient.setToken(Option(newToken))
      MakeApiClient.isAuthenticated
    }
  }

  def logout(): Future[Unit] =
    Ajax
      .post(
        url = urlFrom("logout"),
        timeout = maxTimeout,
        headers = getDefaultHeaders,
        withCredentials = withCredentials
      )
      .map(_ => MakeApiClient.removeToken())

}
