package org.make.front.components

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.router.WithRouter
import org.make.front.components.Components.RichVirtualDOMElements
import org.make.front.styles.base._
import org.make.front.styles.ui.{CTAStyles, InputStyles, ModalStyles, TagStyles}

import scalacss.DevDefaults._

object App {

  lazy val reactClass = WithRouter(
    React.createClass[Unit, Unit](
      displayName = "App",
      render = (_) =>
        <("app-container")(^.className := "App")(
          <.style()(
            Basic.render[String],
            RowRulesStyles.render[String],
            ColRulesStyles.render[String],
            TextStyles.render[String],
            CTAStyles.render[String],
            InputStyles.render[String],
            TagStyles.render[String],
            ModalStyles.render[String]
          ),
          //<.AboutMakeMainHeaderComponent.empty,
          <.MainHeaderComponent.empty,
          <.NotificationsComponent.empty,
          <.ContainerComponent.empty,
          <.MainFooterComponent.empty,
          <.style()(RWDHideRulesStyles.render[String])
      )
    )
  )
}
