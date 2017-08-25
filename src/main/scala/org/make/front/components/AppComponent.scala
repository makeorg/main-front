package org.make.front.components

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.router.WithRouter
import org.make.front.components.presentationals.RichVirtualDOMElements
import org.make.front.styles.{BulmaStyles, MakeStyles}

import scalacss.DevDefaults._
import scalacss.internal.mutable.StyleSheet

object AppComponent {
  def apply(): ReactClass = WithRouter(reactClass)

  private lazy val reactClass = React.createClass[Unit, Unit](
    render = (_) =>
      <.div(^.className := "App")(
        <.HeaderComponent.empty,
        <.ContainerComponent.empty,
        <.FooterContainerComponent.empty,
        <.NotificationContainerComponent.empty,
        <.ConnectUserContainerComponent.empty,
        <.style()(AppComponentStyles.render[String])
    )
  )
}

object AppComponentStyles extends StyleSheet.Inline {
  import dsl._

  val container: StyleA = style(maxWidth(114.rem), marginRight.auto, marginLeft.auto, width(100.%%), height(100.%%))

  val wrapper: StyleA = style(
    backgroundColor(MakeStyles.Background.wrapper),
    position.relative
  )

  val title2: StyleA = style(
    MakeStyles.title2,
    MakeStyles.Font.tradeGothicLTStd,
    marginTop(3.rem)
  )

  val icon: StyleA = style(
    addClassName(BulmaStyles.Element.icon.htmlClass),
    marginRight(0.6.rem)
  )
}