package org.make.front.components

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.router.RouterProps._
import io.github.shogowada.scalajs.reactjs.router.WithRouter
import org.make.front.components.Components.RichVirtualDOMElements
import org.make.front.styles.base.{ColRulesStyles, RowRulesStyles, TextStyles, _}
import org.make.front.styles.ui._

import scalacss.DevDefaults._

object App {

  lazy val reactClass = WithRouter(
    WithRouter(
      React
        .createClass[Unit, Unit](
          displayName = "App",
          render = (self) => {
            <("app-container")(^.className := "App")(
              <.style()(
                Basic.render[String],
                RowRulesStyles.render[String],
                ColRulesStyles.render[String],
                TextStyles.render[String],
                CTAStyles.render[String],
                InputStyles.render[String],
                TagStyles.render[String],
                ModalStyles.render[String],
                TooltipStyles.render[String]
              ),
              if (self.props.location.pathname == "/consultation/comment-lutter-contre-les-violences-faites-aux-femmes") {
                <.AboutMakeMainHeaderComponent.empty
              } else {
                <.MainHeaderComponent.empty
              },
              <.NotificationsComponent.empty,
              <.ContainerComponent.empty,
              <.MainFooterComponent.empty,
              <.style()(RWDHideRulesStyles.render[String])
            )
          }
        )
    )
  )
}
