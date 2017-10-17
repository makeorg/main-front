package org.make.front.components.operation

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM.{<, _}
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import org.make.front.components.Components._
import org.make.front.facades.FacebookPixel
import org.make.front.facades.Unescape.unescape
import org.make.front.styles.ThemeStyles
import org.make.front.styles.base.{ColRulesStyles, RowRulesStyles, TextStyles}
import org.make.front.styles.ui.CTAStyles
import org.make.front.styles.utils._
import org.make.front.styles.vendors.FontAwesomeStyles

import scalacss.DevDefaults.{StyleA, _}
import scalacss.internal.mutable.StyleSheet

object IntroOfOperationSequence {

  final case class IntroOfOperationSequenceProps(clickOnButtonHandler: () => Unit)

  lazy val reactClass: ReactClass =
    React
      .createClass[IntroOfOperationSequenceProps, Unit](
        displayName = "IntroOfOperationSequence",
        componentDidMount = { _ =>
          FacebookPixel.fbq("trackCustom", "display-sequence-intro")
        },
        render = { self =>
          <.div(^.className := Seq(IntroOfOperationSequenceStyles.wrapper))(
            <.div(^.className := IntroOfOperationSequenceStyles.innerWrapper)(
              <.div(^.className := Seq(RowRulesStyles.row))(
                <.div(^.className := ColRulesStyles.col)(
                  <.div(^.className := IntroOfOperationSequenceStyles.introWrapper)(
                    <.h2(^.className := Seq(TextStyles.bigIntro, IntroOfOperationSequenceStyles.intro))(
                      unescape("Des milliers de citoyens proposent des&nbsp;solutions.")
                    )
                  ),
                  <.div(^.className := IntroOfOperationSequenceStyles.explanationWrapper)(
                    <.p(^.className := Seq(TextStyles.biggerMediumText, IntroOfOperationSequenceStyles.explanation))(
                      unescape("Prenez position sur ces solutions et proposez les&nbsp;vôtres.")
                    )
                  ),
                  <.div(^.className := IntroOfOperationSequenceStyles.explanationWrapper)(
                    <.p(^.className := Seq(TextStyles.biggerMediumText, IntroOfOperationSequenceStyles.explanation))(
                      unescape("Les plus soutenues détermineront nos&nbsp;actions.")
                    )
                  ),
                  <.div(^.className := IntroOfOperationSequenceStyles.ctaWrapper)(
                    <.button(
                      ^.className := Seq(CTAStyles.basic, CTAStyles.basicOnButton),
                      ^.onClick := self.props.wrapped.clickOnButtonHandler
                    )(
                      <.i(^.className := Seq(FontAwesomeStyles.fa, FontAwesomeStyles.paperPlaneTransparent))(),
                      unescape("&nbsp;" + "Démarrer")
                    )
                  )
                )
              )
            ),
            <.style()(IntroOfOperationSequenceStyles.render[String])
          )
        }
      )
}

object IntroOfOperationSequenceStyles extends StyleSheet.Inline {

  import dsl._

  val wrapper: StyleA =
    style(display.table, width(100.%%), height(100.%%))

  val innerWrapper: StyleA =
    style(display.tableCell, verticalAlign.middle)

  val introWrapper: StyleA =
    style(marginBottom(ThemeStyles.SpacingValue.medium.pxToEm()))

  val intro: StyleA =
    style(textAlign.center, color(ThemeStyles.TextColor.lighter))

  val explanationWrapper: StyleA =
    style(marginTop(ThemeStyles.SpacingValue.small.pxToEm()), marginBottom(ThemeStyles.SpacingValue.small.pxToEm()))

  val explanation: StyleA =
    style(textAlign.center)

  val ctaWrapper: StyleA =
    style(textAlign.center, marginTop(ThemeStyles.SpacingValue.medium.pxToEm()))

}
