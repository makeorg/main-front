package org.make.front.reducers

import org.make.front.actions.{LoggedInAction, LogoutAction}
import org.make.front.models.User

object ConnectedUserReducer {
  def reduce(maybeConnectedUser: Option[User], action: Any): Option[User] = {
    action match {
      case LoggedInAction(user) => Some(user)
      case LogoutAction         => None
      case _                    => maybeConnectedUser
    }
  }

}
