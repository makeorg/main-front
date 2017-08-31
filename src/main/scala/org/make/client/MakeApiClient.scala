package org.make.client

import io.circe.{Decoder, Printer}
import io.circe.java8.time.TimeInstances
import io.circe.parser._
import io.circe.syntax._
import io.github.shogowada.statictags.MediaTypes
import org.make.core.URI._
import org.make.front.models.Token
import org.scalajs.dom.XMLHttpRequest
import org.scalajs.dom.ext.Ajax
import org.scalajs.dom.ext.Ajax.InputData

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.{Future, Promise}
import scala.util.{Failure, Success, Try}

trait MakeApiClientComponent {
  def apiBaseUrl: String
  def client: Client
  def maxTimeout: Int = 5000
  def defaultHeaders: Map[String, String] = Map.empty
  def withCredentials: Boolean = false
}

//TODO: add error handler. throw custom exception in the promise.future
trait DefaultMakeApiClientComponent extends MakeApiClientComponent with TimeInstances {
  override def client: DefaultMakeApiClient = new DefaultMakeApiClient()

  override def defaultHeaders: Map[String, String] = {
    Map(
      //TODO: X-Forwarded-For Header is set for dev only. Remove in prod.
      "X-Forwarded-For" -> "0.0.0.0",
      "Accept" -> MediaTypes.`application/json`,
      "Content-Type" -> "application/json;charset=UTF-8"
    ) ++ MakeApiClient.getToken.map { token =>
      Map("Authorization" -> s"${token.token_type} ${token.access_token}")
    }.getOrElse(Map.empty)
  }

  final class DefaultMakeApiClient extends Client {

    override def baseUrl: String = apiBaseUrl

    private def XHRResponseTo[ENTITY](responseTry: Try[XMLHttpRequest],
                                      promise: Promise[Option[ENTITY]])(implicit decoder: Decoder[ENTITY]): Promise[Option[ENTITY]] = {
      responseTry match {
        case Success(response) if response.status == 204 => promise.success(None)
        case Success(response) =>
          parse(response.responseText).flatMap(_.as[ENTITY]) match {
            case Left(error) => promise.failure(error)
            case Right(parsedResponse) => promise.success(Some(parsedResponse))
          }
        case Failure(error) => promise.failure(error)
      }
    }

    private def urlFrom(apiEndpoint: String, urlParams: Seq[(String, Any)] = Seq.empty): String =
      (baseUrl / apiEndpoint).addParams(urlParams)

    override def get[ENTITY](
      apiEndpoint: String = "",
      urlParams: Seq[(String, Any)] = Seq.empty,
      headers: Map[String, String] = Map.empty
    )(implicit decoder: Decoder[ENTITY]): Future[Option[ENTITY]] = {
      val promiseReturn = Promise[Option[ENTITY]]()
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

    override def post[ENTITY](
      apiEndpoint: String = "",
      urlParams: Seq[(String, Any)] = Seq.empty,
      data: InputData = "",
      headers: Map[String, String] = Map.empty
    )(implicit decoder: Decoder[ENTITY]): Future[Option[ENTITY]] = {
      val promiseReturn = Promise[Option[ENTITY]]()
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

    override def put[ENTITY](apiEndpoint: String,
                             urlParams: Seq[(String, Any)],
                             data: InputData,
                             headers: Map[String, String])(implicit decoder: Decoder[ENTITY]): Future[Option[ENTITY]] = {
      val promiseReturn = Promise[Option[ENTITY]]()
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

    override def patch[ENTITY](apiEndpoint: String,
                               urlParams: Seq[(String, Any)],
                               data: InputData,
                               headers: Map[String, String])(implicit decoder: Decoder[ENTITY]): Future[Option[ENTITY]] = {
      val promiseReturn = Promise[Option[ENTITY]]()
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

    override def delete[ENTITY](apiEndpoint: String,
                                urlParams: Seq[(String, Any)],
                                data: InputData,
                                headers: Map[String, String])(implicit decoder: Decoder[ENTITY]): Future[Option[ENTITY]] = {
      val promiseReturn = Promise[Option[ENTITY]]()
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

    def authenticate(username: String, password: String)(implicit decoder: Decoder[Token]): Future[Boolean] = {
      if (MakeApiClient.isAuthenticated) {
        Future.successful(true)
      } else {
        askForAccessToken(username, password)
      }
    }

    private def askForAccessToken(username: String,
                                  password: String)(implicit decoder: Decoder[Token]): Future[Boolean] = {
      post[Token](
        "oauth" / "make_access_token",
        data = "".paramsToString(Seq("username" -> username, "password" -> password, "grant_type" -> "password")),
        headers = Map("Content-Type" -> MediaTypes.`application/x-www-form-urlencoded`)
      ).map { newToken =>
        MakeApiClient.setToken(newToken.get)
        MakeApiClient.isAuthenticated
      }
    }

    def authenticateSocial(provider: String, token: String)(implicit decoder: Decoder[Token]): Future[Boolean] = {
      if (MakeApiClient.isAuthenticated) {
        Future.successful(true)
      } else {
        askForAccessTokenSocial(provider, token)
      }
    }

    private def askForAccessTokenSocial(provider: String,
                                        token: String)(implicit decoder: Decoder[Token]): Future[Boolean] = {
      post[Token](
        "user" / "login" / "social",
        data = Map("provider" -> provider, "token" -> token).asJson.pretty(MakeApiClient.printer)
      ).map { newToken =>
        MakeApiClient.setToken(newToken.get)
        MakeApiClient.isAuthenticated
      }
    }

    def logout(): Future[Unit] =
      Ajax
        .post(
          url = urlFrom("logout"),
          timeout = maxTimeout,
          headers = defaultHeaders,
          withCredentials = withCredentials
        )
        .map(_ => MakeApiClient.removeToken())

  }
}

object MakeApiClient {
  private var token: Option[Token] = None
  val printer: Printer = Printer.noSpaces

  def getToken: Option[Token] = token
  def setToken(newToken: Token): Unit = token = Some(newToken)
  def removeToken(): Unit = token = None
  def isAuthenticated: Boolean = token.isDefined
}
