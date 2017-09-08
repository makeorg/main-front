package org.make.front.components

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.router.WithRouter
import org.make.front.components.presentationals.RichVirtualDOMElements
import org.make.front.styles.{BulmaStyles, MakeStyles}

import scalacss.DevDefaults._
import scalacss.internal.mutable.StyleSheet
import scalacss.internal.{Length, ValueT}

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
        <.style()(AppComponentStyles.render[String], LayoutStyleSheet.render[String], TextStyleSheet.render[String])
    )
  )
}

object LayoutStyleSheet extends StyleSheet.Inline {

  import dsl._

  val gutter: ValueT[ValueT.LenPct] = MakeStyles.Spacing.small

  val col: StyleA =
    style(display.inlineBlock, verticalAlign.top, width(100.%%), paddingRight(gutter), paddingLeft(gutter))

  val colHalf: StyleA = style(width(50.%%))
  val colThird: StyleA = style(width(33.3333.%%))
  val colQuarter: StyleA = style(width(25.%%))

  val colHalfBeyondSmall: StyleA = style(MakeStyles.MediaQueries.beyondSmall(width(50.%%)))
  val colThirdBeyondSmall: StyleA = style(MakeStyles.MediaQueries.beyondSmall(width(33.3333.%%)))
  val colQuarterBeyondSmall: StyleA = style(MakeStyles.MediaQueries.beyondSmall(width(25.%%)))

  val colHalfBeyondMedium: StyleA = style(MakeStyles.MediaQueries.beyondMedium(width(50.%%)))
  val colThirdBeyondMedium: StyleA = style(MakeStyles.MediaQueries.beyondMedium(width(33.3333.%%)))
  val colQuarterBeyondMedium: StyleA = style(MakeStyles.MediaQueries.beyondMedium(width(25.%%)))

  val colHalfBeyondLarge: StyleA = style(MakeStyles.MediaQueries.beyondLarge(width(50.%%)))
  val colThirdBeyondLarge: StyleA = style(MakeStyles.MediaQueries.beyondLarge(width(33.3333.%%)))
  val colQuarterBeyondLarge: StyleA = style(MakeStyles.MediaQueries.beyondLarge(width(25.%%)))

  val row: StyleA = style(
    display.block,
    position.relative,
    MakeStyles.MediaQueries.beyondSmall(paddingRight(gutter), paddingLeft(gutter))
  )

  val centeredRow: StyleA = style(
    row,
    MakeStyles.MediaQueries.beyondLarge(maxWidth(MakeStyles.ContainerMaxWidth.main), marginRight.auto, marginLeft.auto)
  )

  val showBlockBeyondSmall: StyleA = style(display.none, MakeStyles.MediaQueries.beyondSmall(display.block))
  val showBlockBeyondMedium: StyleA = style(display.none, MakeStyles.MediaQueries.beyondMedium(display.block))
  val showBlockBeyondLarge: StyleA = style(display.none, MakeStyles.MediaQueries.beyondLarge(display.block))

  val showInlineBlockBeyondSmall: StyleA = style(display.none, MakeStyles.MediaQueries.beyondSmall(display.inlineBlock))
  val showInlineBlockBeyondMedium: StyleA =
    style(display.none, MakeStyles.MediaQueries.beyondMedium(display.inlineBlock))
  val showInlineBlockBeyondLarge: StyleA = style(display.none, MakeStyles.MediaQueries.beyondLarge(display.inlineBlock))

  val hideBeyondSmall: StyleA = style(MakeStyles.MediaQueries.beyondSmall(display.none))
  val hideBeyondMedium: StyleA = style(MakeStyles.MediaQueries.beyondMedium(display.none))
  val hideBeyondLarge: StyleA = style(MakeStyles.MediaQueries.beyondLarge(display.none))

}

object TextStyleSheet extends StyleSheet.Inline {

  import dsl._

  //TODO: globalize function
  implicit class NormalizedSize(val baseSize: Int) extends AnyVal {
    def pxToEm(browserContextSize: Int = 18): Length[Double] = {
      (baseSize.toFloat / browserContextSize.toFloat).em
    }
  }

  val smallerText: StyleA = style(fontSize(14.pxToEm()))
  val smallText: StyleA = style(fontSize(13.pxToEm()), MakeStyles.MediaQueries.beyondSmall(fontSize(16.pxToEm())))
  val baseText: StyleA = style(fontSize(18.pxToEm()))
  val veryBigText: StyleA = style(fontSize(44.pxToEm()))
  val boldText: StyleA = style(MakeStyles.Font.circularStdBold)

  val title: StyleA = style(MakeStyles.Font.tradeGothicLTStd, textTransform.uppercase)
  val smallerTitle: StyleA =
    style(title, fontSize(15.pxToEm()), MakeStyles.MediaQueries.beyondSmall(fontSize(20.pxToEm())))
  val smallTitle: StyleA = style(title, fontSize(22.pxToEm()))
  val mediumTitle: StyleA =
    style(title, fontSize(20.pxToEm()), MakeStyles.MediaQueries.beyondSmall(fontSize(34.pxToEm())))
  val bigTitle: StyleA = style(title, fontSize(46.pxToEm()))
  val veryBigTitle: StyleA =
    style(title, fontSize(30.pxToEm()), MakeStyles.MediaQueries.beyondMedium(fontSize(60.pxToEm())))

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
