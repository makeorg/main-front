package org.make.front.components.mainHeader

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.router.RouterProps._
import io.github.shogowada.scalajs.reactjs.router.WithRouter
import org.make.front.components.Components._

object MainHeader {
  lazy val reactClass: ReactClass = WithRouter(
    React
      .createClass[Unit, Unit](displayName = "MainHeader", render = self => {
        if (self.props.location.pathname == "/consultation/comment-lutter-contre-les-violences-faites-aux-femmes") {
          <.MainHeaderWithStaticLinksComponent.empty
        } else {
          <.CoreMainHeaderComponent.empty
        }
      })
  )
}
