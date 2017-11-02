package org.make.front.components

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.router.WithRouter
import org.make.front.components.Components.{RichVirtualDOMElements, _}
import org.make.front.styles.base.{ColRulesStyles, RWDHideRulesStyles, RowRulesStyles, TextStyles, _}
import org.make.front.styles.ui._

import scalacss.DevDefaults._
import scalacss.internal.StyleA
import scalacss.internal.mutable.StyleSheet

object App {

  lazy val reactClass = WithRouter(
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
