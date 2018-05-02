package org.make.front.components.sequence.contents

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM.{<, _}
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import org.make.front.Main.CssSettings._
import org.make.front.components.Components._
import org.make.front.facades.I18n
import org.make.front.facades.Unescape.unescape
import org.make.front.models.OperationIntroWording
import org.make.front.styles.ThemeStyles
import org.make.front.styles.base.{LayoutRulesStyles, TextStyles}
import org.make.front.styles.ui.CTAStyles
import org.make.front.styles.utils._
import org.make.front.styles.vendors.FontAwesomeStyles

import scala.scalajs.js

object IntroductionOfTheSequence {

  final case class IntroductionOfTheSequenceState(title: String,
                                                  explanation1: String,
                                                  explanation2: String,
                                                  cta: String,
                                                  duration: Option[String])
  final case class IntroductionOfTheSequenceProps(clickOnButtonHandler: () => Unit, introWording: OperationIntroWording)

  lazy val reactClass: ReactClass =
    React
      .createClass[IntroductionOfTheSequenceProps, IntroductionOfTheSequenceState](
        displayName = "IntroductionOfTheSequence",
        getInitialState = { self =>
          IntroductionOfTheSequenceState(
            title = self.props.wrapped.introWording.title.getOrElse(unescape(I18n.t("sequence.introduction.title"))),
            explanation1 = self.props.wrapped.introWording.explanation1
              .getOrElse(unescape(I18n.t("sequence.introduction.explanation-1"))),
            explanation2 = self.props.wrapped.introWording.explanation2
              .getOrElse(unescape(I18n.t("sequence.introduction.explanation-2"))),
            cta =
              self.props.wrapped.introWording.cta.getOrElse(unescape("&nbsp;" + I18n.t("sequence.introduction.cta"))),
            duration = self.props.wrapped.introWording.duration
          )
        },
        render = { self =>
          <.div(^.className := js.Array(LayoutRulesStyles.row))(
            <.div(^.className := IntroductionOfTheSequenceStyles.titleWrapper)(
              <.p(^.className := js.Array(TextStyles.bigIntro, IntroductionOfTheSequenceStyles.title))(self.state.title)
            ),
            <.div(^.className := IntroductionOfTheSequenceStyles.explanationWrapper)(
              <.p(^.className := js.Array(TextStyles.biggerMediumText, IntroductionOfTheSequenceStyles.explanation))(
                self.state.explanation1
              )
            ),
            <.div(^.className := IntroductionOfTheSequenceStyles.explanationWrapper)(
              <.p(^.className := js.Array(TextStyles.biggerMediumText, IntroductionOfTheSequenceStyles.explanation))(
                self.state.explanation2
              )
            ),
            self.state.duration.map { duration =>
              <.div(^.className := IntroductionOfTheSequenceStyles.explanationWrapper)(
                <.p(
                  ^.className := js.Array(
                    TextStyles.biggerMediumText,
                    TextStyles.boldText,
                    IntroductionOfTheSequenceStyles.explanation
                  )
                )(duration)
              )
            },
            <.div(^.className := IntroductionOfTheSequenceStyles.ctaWrapper)(
              <.button(
                ^.className := js.Array(CTAStyles.basic, CTAStyles.basicOnButton),
                ^.onClick := self.props.wrapped.clickOnButtonHandler
              )(<.i(^.className := js.Array(FontAwesomeStyles.play))(), self.state.cta)
            ),
            <.style()(IntroductionOfTheSequenceStyles.render[String])
          )
        }
      )

}

object IntroductionOfTheSequenceStyles extends StyleSheet.Inline {

  import dsl._

  val titleWrapper: StyleA =
    style(marginBottom(ThemeStyles.SpacingValue.medium.pxToEm()))

  val title: StyleA =
    style(textAlign.center, color(ThemeStyles.TextColor.lighter))

  val explanationWrapper: StyleA =
    style(marginTop(ThemeStyles.SpacingValue.small.pxToEm()), marginBottom(ThemeStyles.SpacingValue.small.pxToEm()))

  val explanation: StyleA =
    style(textAlign.center)

  val ctaWrapper: StyleA =
    style(textAlign.center, marginTop(ThemeStyles.SpacingValue.medium.pxToEm()))

}
