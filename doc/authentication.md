# Authentication

Three modes of authentication are available for the user:
- using a login / password
- using a Google account
- using a Facebook account

The React components relative to authentication are under [src/main/scala/org/make/front/components/authenticate](/src/main/scala/org/make/front/components/authenticate) directory.

A [UserService](/src/main/scala/org/make/services/user/UserService.scala) manage authentication through [MakeApiClient](/src/main/scala/org/make/client/MakeApiClient.scala).

The service calls `/oauth` API endpoints that return an access token.


> Note:
> When a user registers on the site, he is automatically authenticated.


## API authentication

Make API Authentication is based on Oauth2 with an access token.


When authentication is successful, a secure cookie, named `make-secure` on API domain, is set and contains the access token.

Make API uses this cookie for authentication but also supports Authorization header.

By default MakeApiClient stores the access token in a variable
and adds the authorization header to all API requests.


> Note:
> Another cookie for session tracking exist: `make-session-id`

## Google and Facebook authentication

Components used for social authentication:
* [react-google-login](https://github.com/anthonyjgrove/react-google-login)
* [react-facebook-login](https://github.com/keppelen/react-facebook-login)


## User data and redux

The user data is stored in AppState under `connectedUser` (see also: [User model](/src/main/scala/org/make/front/models/User.scala))


[ConnectedUserReducer](/src/main/scala/org/make/front/reducers/ConnectedUserReducer.scala)
and [ConnectedUserMiddleware](/src/main/scala/org/make/front/middlewares/ConnectedUserMiddleware.scala)
manage updates of user state with the `LoggedInAction`, `LogoutAction` and `ReloadUserAction`.






