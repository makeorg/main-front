# API Client


## Presentation

The [Client.scala](src/main/scala/org/make/client/Client.scala) file defines the interface of a HTTP client.

In the purpose of accepting any type of entity, we use [generics](https://docs.scala-lang.org/tour/generic-classes.html)
with [upper type bound](https://docs.scala-lang.org/tour/upper-type-bounds.html) to `js.Object`

Our implementation of the HTTP client is in [MakeApiClient.scala](src/main/scala/org/make/client/MakeApiClient.scala)

## Headers & Security

#### Default Headers

Some headers are sent at every API request:
> "Accept: application/json"
>
> "Content-Type: application/json;charset=UTF-8"

Some headers are sent at every API request if the user is connected:
> "Authorization: Bearer <TOKEN>"
>
> "x-session-id: <TOKEN_SESSION>"

Those are default headers and they are managed by two public methods: `getDefaultHeaders` and `addHeaders`.

The `getDefaultHeaders` method retrieves all default headers at a given time.

The `addHeaders` method will add headers passed as argument to the default header.
:warning: these added headers will be sent at every API request. E.g. country, language, hostname ...

#### Authentication

The authentication is defined by an OAuth2 variable `token` in the client. Self-explicit methods handle this token authentication:

`getToken`, `setToken`, `removeToken` and `isAuthenticated`

This token is then used in default headers whether it is defined or not.

For further information, please refer to [authentication.md](doc/authentication.md)

#### Make Custom Headers

The Make API defines several custom headers such as aforementioned `"x-session-id"`. To mention some of them: `"x-make-location"`, `"x-make-question"`, `"x-make-language"`, `"x-make-country"` ...

The full list is defined in the Make API client implementation.

The source custom header is used to set the source of one request, e.g. "core".

The location custom header is used to set the main origin of one request, including but not limited to: "homepage", "search_results", "proposal_page <ID>", "unknown_location".

## Error Handling

#### Retries system

If a request fails, a retries system is set to prevent faulty errors to be brought up to the UI.

The request is retried whenever it either timeout OR a `502 Bad gateway` error code is returned.

A [tail recursive](https://en.wikipedia.org/wiki/Tail_call) with `n` retries helps the retries to work properly. `n` is defined through `retryAfterTimeout` and here equals 4.

#### HttpErrors

If errors are risen by a request (except `502` as demonstrated above), the proper exception is returned to the caller.

In case of `400 Bad request`, the Make API provides a `Seq[ValidationError]` which is parsed and returned to the caller.

## Configuration

`baseUrl` is the only configuration parameter and defines the API base URL.

## Add new service

The [service package](src/main/scala/org/make/services) provides services for each resources needed.
Every service extends the [ApiService](src/main/scala/org/make/services/ApiService.scala) trait hence must define a `resourceName`.

#### New service example

```scala
object BaseService extends ApiService {
  override val resourceName = "foobar"

  def foo(bar: String): Future[Unit] = {
    var headers = Map[String, String](MakeApiClient.countryHeader -> "BAZ")

    val params = js.Dictionary("foo" -> "bar", "bar" -> "baz")

    MakeApiClient
        .post[js.Object](resourceName / "baz", data = JSON.stringify(params), headers = headers)
        .map { _ =>
        }
    }
  
    def getFoo(fooId: String): Future[Option[Foo]] = {
        MakeApiClient
            .get[UserResponse](resourceName / fooId)
            .map(foo => Option(Foo(foo)))
            .recover { case _: Exception => None }
    }
}

```