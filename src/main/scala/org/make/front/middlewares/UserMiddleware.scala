package org.make.front.middlewares

import java.time.{LocalDate, ZonedDateTime}

import io.github.shogowada.scalajs.reactjs.redux.Redux.Dispatch
import io.github.shogowada.scalajs.reactjs.redux.Store
import org.make.front.actions._
import org.make.front.facades.I18n
import org.make.front.models.{AppState, Profile, User, UserId}
import org.make.services.user.UserServiceComponent

import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.{Failure, Random, Success}

object UserMiddleware extends UserServiceComponent {

  def defaultUser: User = {
    val name: String = Random.alphanumeric.take(10).mkString.toLowerCase
    User(
      userId = UserId("42"),
      email = s"default_user-email-$name@example.com",
      firstName = Some(s"FIRSTNAME $name"),
      lastName = Some("Default User's lastname"),
      lastIp = None,
      hashedPassword = Some("DEFAULT_USER-password"),
      salt = None,
      enabled = false,
      verified = false,
      lastConnection = ZonedDateTime.now,
      verificationToken = None,
      verificationTokenExpiresAt = None,
      resetToken = None,
      resetTokenExpiresAt = None,
      roles = Seq(),
      profile = Some(
        Profile(
          dateOfBirth = Some(LocalDate.now),
          avatarUrl = None,
          profession = None,
          phoneNumber = None,
          twitterId = None,
          facebookId = None,
          googleId = None,
          gender = None,
          genderName = None,
          departmentNumber = None,
          karmaLevel = None,
          locale = None
        )
      ),
      createdAt = None,
      updatedAt = None
    )
  }

  val handle: (Store[AppState]) => (Dispatch) => (Any) => Any = (store: Store[AppState]) => {
    def login(plainUser: User, user: User): Unit = {
      userService.login(plainUser).onComplete {
        case Success(true)  => store.dispatch(LoggedInAction(user))
        case Success(false) => store.dispatch(NotifyAlert(I18n.t("errors.noToken"), Some(I18n.t("errors.loginFailed"))))
        case Failure(_)     => store.dispatch(NotifyError(I18n.t("errors.main"), Some(I18n.t("errors.loginFailed"))))
      }
    }

    (dispatch: Dispatch) =>
      {
        // TODO: define the user from RegisterAction's form's values
        case RegisterAction =>
          val plainUser = defaultUser
          userService.register(plainUser).onComplete {
            case Success(user) =>
              login(plainUser, user)
              dispatch(RegisteredAction(user))
            case Failure(_) => store.dispatch(NotifyError(I18n.t("errors.main"), Some("Register failed")))
          }

        // TODO: define the user from LoginAction's form's values
        case LoginAction(user) =>
          login(user, user)

        case action => dispatch(action)
      }
  }
}
