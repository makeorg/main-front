package org.make.front.components.operation

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM.{<, _}
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import org.make.front.components.Components._
import org.make.front.facades.{FacebookPixel, I18n}
import org.make.front.facades.Unescape.unescape
import org.make.front.styles.ThemeStyles
import org.make.front.styles.base.{ColRulesStyles, RowRulesStyles, TextStyles}
import org.make.front.styles.ui.CTAStyles
import org.make.front.styles.utils._
import org.make.front.styles.vendors.FontAwesomeStyles

import scalacss.DevDefaults.{StyleA, _}
import scalacss.internal.mutable.StyleSheet

object IntroOfOperationSequence {

  final case class IntroOfOperationSequenceProps(
    clickOnButtonHandler: () => Unit,
    title: Option[String] = Some(unescape(I18n.t("operation.sequence.introduction.title"))),
    explanation1: Option[String] = Some(unescape(I18n.t("operation.sequence.introduction.explanation-1"))),
    explanation2: Option[String] = Some(unescape(I18n.t("operation.sequence.introduction.explanation-2")))
  )

  lazy val reactClass: ReactClass =
    React
      .createClass[IntroOfOperationSequenceProps, Unit](
        displayName = "IntroOfOperationSequence",
        render = { self =>
          <.div(^.className := Seq(RowRulesStyles.row))(
            <.div(^.className := ColRulesStyles.col)(self.props.wrapped.title.map { title =>
              <.div(^.className := IntroOfOperationSequenceStyles.titleWrapper)(
                <.p(^.className := Seq(TextStyles.bigIntro, IntroOfOperationSequenceStyles.title))(title)
              )
            }, self.props.wrapped.explanation1.map { text =>
              <.div(^.className := IntroOfOperationSequenceStyles.explanationWrapper)(
                <.p(^.className := Seq(TextStyles.biggerMediumText, IntroOfOperationSequenceStyles.explanation))(text)
              )

            }, self.props.wrapped.explanation2.map { text =>
              <.div(^.className := IntroOfOperationSequenceStyles.explanationWrapper)(
                <.p(^.className := Seq(TextStyles.biggerMediumText, IntroOfOperationSequenceStyles.explanation))(text)
              )
            }, <.div(^.className := IntroOfOperationSequenceStyles.ctaWrapper)(<.button(^.className := Seq(CTAStyles.basic, CTAStyles.basicOnButton), ^.onClick := self.props.wrapped.clickOnButtonHandler)(<.i(^.className := Seq(FontAwesomeStyles.paperPlaneTransparent))(), unescape("&nbsp;" + I18n.t("operation.sequence.introduction.cta"))))),
            <.style()(IntroOfOperationSequenceStyles.render[String])
          )
        }
      )

}

object IntroOfOperationSequenceStyles extends StyleSheet.Inline {

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
