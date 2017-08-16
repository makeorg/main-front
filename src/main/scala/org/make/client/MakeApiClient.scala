package org.make.client

import io.circe.Decoder
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

    //TODO: load these from configuration file
    override def baseUrl: String = "http://localhost:9000"

    private def XHRResponseTo[ENTITY](responseTry: Try[XMLHttpRequest],
                                      promise: Promise[ENTITY])(implicit decoder: Decoder[ENTITY]): Promise[ENTITY] = {
      responseTry match {
        case Success(response) =>
          parse(response.responseText).flatMap(_.as[ENTITY]) match {
            case Left(error)           => promise.failure(error)
            case Right(parsedResponse) => promise.success(parsedResponse)
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
    )(implicit decoder: Decoder[ENTITY]): Future[ENTITY] = {
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

    override def post[ENTITY](
      apiEndpoint: String = "",
      urlParams: Seq[(String, Any)] = Seq.empty,
      data: InputData = "",
      headers: Map[String, String] = Map.empty
    )(implicit decoder: Decoder[ENTITY]): Future[ENTITY] = {
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

    override def put[ENTITY](apiEndpoint: String,
                             urlParams: Seq[(String, Any)],
                             data: InputData,
                             headers: Map[String, String])(implicit decoder: Decoder[ENTITY]): Future[ENTITY] = {
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

    override def patch[ENTITY](apiEndpoint: String,
                               urlParams: Seq[(String, Any)],
                               data: InputData,
                               headers: Map[String, String])(implicit decoder: Decoder[ENTITY]): Future[ENTITY] = {
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

    override def delete[ENTITY](apiEndpoint: String,
                                urlParams: Seq[(String, Any)],
                                data: InputData,
                                headers: Map[String, String])(implicit decoder: Decoder[ENTITY]): Future[ENTITY] = {
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

    def authenticate(username: String, password: String)(implicit decoder: Decoder[Token]): Future[Boolean] = {
      if (MakeApiClient.isAuthenticated) {
        Future.successful(true)
      } else {
        askForAccessToken(username, password)
      }
    }

    private def askForAccessToken(username: String,
                                  password: String)(implicit decoder: Decoder[Token]): Future[Boolean] = {
      val promiseReturn: Promise[Token] = Promise[Token]()
      Ajax
        .post(
          url = urlFrom("oauth" / "make_access_token"),
          data = "".paramsToString(Seq("username" -> username, "password" -> password, "grant_type" -> "password")),
          timeout = maxTimeout,
          headers = defaultHeaders ++ Map("Content-Type" -> MediaTypes.`application/x-www-form-urlencoded`),
          withCredentials = withCredentials
        )
        .onComplete(responseTry => XHRResponseTo(responseTry, promiseReturn))
      promiseReturn.future.map { newToken =>
        MakeApiClient.setToken(newToken)
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
      val promiseReturn: Promise[Token] = Promise[Token]()
      Ajax
        .post(
          url = urlFrom("user" / "login" / "social"),
          data = Map("provider" -> provider, "token" -> token).asJson.toString,
          timeout = maxTimeout,
          headers = defaultHeaders,
          withCredentials = withCredentials
        )
        .onComplete(responseTry => XHRResponseTo(responseTry, promiseReturn))
      promiseReturn.future.map { newToken =>
        MakeApiClient.setToken(newToken)
        MakeApiClient.isAuthenticated
      }
    }
  }
}

object MakeApiClient {
  private var token: Option[Token] = None

  def getToken: Option[Token] = token
  def setToken(newToken: Token): Unit = token = Some(newToken)
  def removeToken(): Unit = token = None
  def isAuthenticated: Boolean = token.isDefined
}
