package org.make.front.components.consultation

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.ReactClass

object CommunityPartners {

  case class CommunityPartnersState()
  case class CommunityPartnersProps()

  lazy val reactClass: ReactClass =
    React
      .createClass[CommunityPartnersProps, CommunityPartnersState](displayName = "CommunityPartners", getInitialState = {
      _ =>
        CommunityPartnersState()
    }, render = { self =>
      <.div()("toDo: Partners")
    })
}
