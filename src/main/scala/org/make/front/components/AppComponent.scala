package org.make.front.components

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.router.WithRouter
import org.make.front.components.presentationals.RichVirtualDOMElements
import org.make.front.styles.{BulmaStyles, CTAStyles, InputStyles, LayoutRulesStyles, MakeStyles, TextStyles}

import scalacss.DevDefaults._
import scalacss.internal.mutable.StyleSheet

object AppComponent {
  def apply(): ReactClass = WithRouter(reactClass)

  private lazy val reactClass = React.createClass[Unit, Unit](
    render = (_) =>
      <("app-container")(^.className := "App")(
        <.HeaderComponent.empty,
        <.ContainerComponent.empty,
        <.NavigationInThemesContainerComponent.empty,
        <.FooterComponent.empty,
        <.NotificationContainerComponent.empty,
        <.ConnectUserContainerComponent.empty,
        <.PasswordRecoveryContainerComponent.empty,
        <.style()(
          AppComponentStyles.render[String],
          LayoutRulesStyles.render[String],
          TextStyles.render[String],
          CTAStyles.render[String],
          InputStyles.render[String]
        )
    )
  )
}

object AppComponentStyles extends StyleSheet.Inline {

  import dsl._

  val container: StyleA = style(maxWidth(114.rem), marginRight.auto, marginLeft.auto, width(100.%%), height(100.%%))

  val title2: StyleA = style(MakeStyles.title2, MakeStyles.Font.tradeGothicLTStd, marginTop(3.rem))

  val link: StyleA = style(color(MakeStyles.Color.pink), fontWeight.bold)

  val icon: StyleA = style(addClassName(BulmaStyles.Element.icon.htmlClass), marginRight(0.6.rem))

  val buttonIcon: StyleA = style(paddingBottom(0.5F.rem), paddingRight(0.9.rem))

  val submitButton: StyleA = style(marginBottom(1.7F.rem))

  val errorInput: StyleA =
    style(backgroundColor(MakeStyles.Color.lightPink), border :=! s"0.1rem solid ${MakeStyles.Color.error}")

  val errorMessage: StyleA = style(
    display.block,
    margin.auto,
    MakeStyles.Font.circularStdBook,
    fontSize(1.4F.rem),
    color(MakeStyles.Color.error),
    lineHeight(1.8F.rem),
    paddingBottom(1.rem)
  )

}
