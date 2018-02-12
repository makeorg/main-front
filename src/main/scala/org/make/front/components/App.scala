package org.make.front.components

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.router.WithRouter
import org.make.front.Main.CssSettings._
import org.make.front.components.Components.{RichVirtualDOMElements, _}
import org.make.front.styles.base.{ColRulesStyles, RWDHideRulesStyles, LayoutRulesStyles, TextStyles, _}
import org.make.front.styles.ui._

object App {

  final case class AppProps(language: String, country: String)

  lazy val reactClass = WithRouter(
    React
      .createClass[AppProps, Unit](
        displayName = "App",
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
              TooltipStyles.render[String]
            ),
            <.div(^.className := AppStyles.fixedMainHeader)(<.MainHeaderComponent.empty),
            <.ContainerComponent.empty,
            <.MainFooterComponent.empty,
            <.style()(RWDHideRulesStyles.render[String], AppStyles.render[String])
          )
        }
      )
  )
}

object AppStyles extends StyleSheet.Inline {

  import dsl._

  val fixedMainHeader: StyleA =
    style(position.fixed, top(`0`), left(`0`), width(100.%%), zIndex(10), boxShadow := s"0 2px 4px 0 rgba(0,0,0,0.50)")
}
