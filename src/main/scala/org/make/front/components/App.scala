package org.make.front.components

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.router.WithRouter
import org.make.front.components.Components.RichVirtualDOMElements
import org.make.front.styles._
import org.make.front.styles.base.{Basic, TextStyles}
import org.make.front.styles.ui.{CTAStyles, InputStyles, ModalStyles}

import scalacss.DevDefaults._

object App {

  lazy val reactClass = WithRouter(
    React.createClass[Unit, Unit](
      render = (_) =>
        <("app-container")(^.className := "App")(
          <.style()(
            Basic.render[String],
            LayoutRulesStyles.render[String],
            TextStyles.render[String],
            CTAStyles.render[String],
            InputStyles.render[String],
            TagStyles.render[String],
            ModalStyles.render[String]
          ),
          <.MainHeaderComponent.empty,
          <.NotificationsComponent.empty,
          <.ContainerComponent.empty,
          <.MainFooterComponent.empty,
          <.style()(RWDHideRulesStyles.render[String])
      )
    )
  )
}
