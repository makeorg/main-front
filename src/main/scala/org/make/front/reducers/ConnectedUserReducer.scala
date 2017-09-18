package org.make.front.reducers

import org.make.front.actions.{LoggedInAction, LogoutAction}
import org.make.front.models.{User => UserModel}

object ConnectedUserReducer {
  def reduce(maybeConnectedUser: Option[UserModel], action: Any): Option[UserModel] = {
    action match {
      case LoggedInAction(user) => Some(user)
      case LogoutAction         => None
      case _                    => maybeConnectedUser
    }
  }

}
