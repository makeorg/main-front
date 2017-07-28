package org.make.front.reducers

import org.make.front.models.User

object ConnectedUserReducer {
  def reduce(maybeState: Option[User], action: Any): Option[User] =
    Some(User("42", "John", "Doe", None))
//    None

}
