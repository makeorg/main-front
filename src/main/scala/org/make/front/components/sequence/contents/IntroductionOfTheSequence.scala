package org.make.front.components.sequence.contents

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM.{<, _}
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import org.make.front.Main.CssSettings._
import org.make.front.components.Components._
import org.make.front.facades.I18n
import org.make.front.facades.Unescape.unescape
import org.make.front.styles.ThemeStyles
import org.make.front.styles.base.{ColRulesStyles, RowRulesStyles, TextStyles}
import org.make.front.styles.ui.CTAStyles
import org.make.front.styles.utils._
import org.make.front.styles.vendors.FontAwesomeStyles

object IntroductionOfTheSequence {

  final case class IntroductionOfTheSequenceProps(
    clickOnButtonHandler: () => Unit,
    title: Option[String] = Some(unescape(I18n.t("sequence.introduction.title"))),
    explanation1: Option[String] = Some(unescape(I18n.t("sequence.introduction.explanation-1"))),
    explanation2: Option[String] = Some(unescape(I18n.t("sequence.introduction.explanation-2")))
  )

  lazy val reactClass: ReactClass =
    React
      .createClass[IntroductionOfTheSequenceProps, Unit](
        displayName = "IntroductionOfTheSequence",
        render = { self =>
          <.div(^.className := Seq(RowRulesStyles.row))(
            <.div(^.className := ColRulesStyles.col)(self.props.wrapped.title.map { title =>
              <.div(^.className := IntroductionOfTheSequenceStyles.titleWrapper)(
                <.p(^.className := Seq(TextStyles.bigIntro, IntroductionOfTheSequenceStyles.title))(title)
              )
            }, self.props.wrapped.explanation1.map { text =>
              <.div(^.className := IntroductionOfTheSequenceStyles.explanationWrapper)(
                <.p(^.className := Seq(TextStyles.biggerMediumText, IntroductionOfTheSequenceStyles.explanation))(text)
              )

            }, self.props.wrapped.explanation2.map { text =>
              <.div(^.className := IntroductionOfTheSequenceStyles.explanationWrapper)(
                <.p(^.className := Seq(TextStyles.biggerMediumText, IntroductionOfTheSequenceStyles.explanation))(text)
              )
            }, <.div(^.className := IntroductionOfTheSequenceStyles.ctaWrapper)(<.button(^.className := Seq(CTAStyles.basic, CTAStyles.basicOnButton), ^.onClick := self.props.wrapped.clickOnButtonHandler)(<.i(^.className := Seq(FontAwesomeStyles.play))(), unescape("&nbsp;" + I18n.t("sequence.introduction.cta"))))),
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
