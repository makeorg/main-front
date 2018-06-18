package org.make.front.components

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.router.WithRouter
import org.make.front.Main.CssSettings._
import org.make.front.components.Components.{RichVirtualDOMElements, _}
import org.make.front.components.users.authenticate.TriggerSignUp.TriggerSignUpProps
import org.make.front.styles.base.{ColRulesStyles, LayoutRulesStyles, RWDHideRulesStyles, TextStyles, _}
import org.make.front.styles.ui._

object App {

  final case class AppProps(language: String, country: String, nVotesTriggerConnexion: Int)

  final case class AppState(language: String, country: String)

  lazy val reactClass = WithRouter(
    React
      .createClass[AppProps, AppState](
        displayName = "App",
        getInitialState = { self =>
          AppState(self.props.wrapped.language, self.props.wrapped.country)
        },
        componentWillReceiveProps = { (self, props) =>
          self.setState(_.copy(language = props.wrapped.language, country = props.wrapped.country))
        },
        render = (self) => {
          <("app-container")(^.className := "App")(
            <.style()(
              Basic.render[String],
              LayoutRulesStyles.render[String],
              ColRulesStyles.render[String],
              TableLayoutStyles.render[String],
              TableLayoutBeyondSmallStyles.render[String],
              TableLayoutBeyondMediumStyles.render[String],
              TextStyles.render[String],
              CTAStyles.render[String],
              InputStyles.render[String],
              TagStyles.render[String],
              TooltipStyles.render[String],
              AccordionStyles.render[String]
            ),
            <.ContainerComponent.empty,
            <.MainFooterComponent.empty,
            <.TriggerSignUpComponent(^.wrapped := TriggerSignUpProps(self.props.wrapped.nVotesTriggerConnexion))(),
            <.style()(RWDHideRulesStyles.render[String])
          )
        }
      )
  )
}
