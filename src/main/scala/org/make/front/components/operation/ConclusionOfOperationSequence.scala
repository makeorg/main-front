package org.make.front.components.operation

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM.{<, _}
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import org.make.front.components.Components._
import org.make.front.components.subscribeToNewsletter.SubscribeToNewsletterFormContainer.SubscribeToNewsletterFormContainerProps
import org.make.front.facades.{FacebookPixel, I18n}
import org.make.front.facades.Unescape.unescape
import org.make.front.styles.ThemeStyles
import org.make.front.styles.base.{ColRulesStyles, RowRulesStyles, TextStyles}
import org.make.front.styles.utils._

import scalacss.DevDefaults.{StyleA, _}
import scalacss.internal.mutable.StyleSheet

object ConclusionOfOperationSequence {

  final case class ConclusionOfOperationSequenceProps(isConnected: Boolean)

  final case class ConclusionOfOperationSequenceState(subscriptionToNewsletterHasSucceed: Boolean)

  lazy val reactClass: ReactClass =
    React.createClass[ConclusionOfOperationSequenceProps, ConclusionOfOperationSequenceState](
      displayName = "IntroOfOperationSequence",
      getInitialState = { self =>
        ConclusionOfOperationSequenceState(subscriptionToNewsletterHasSucceed = false)
      },
      render = { self =>
        def onSubscribeToNewsletterSuccess(): () => Unit = { () =>
          FacebookPixel.fbq("trackCustom", "click-email-submit")
          self.setState(_.copy(subscriptionToNewsletterHasSucceed = true))
        }

        <.div()(
          <.div(^.className := Seq(RowRulesStyles.row))(
            <.div(^.className := ColRulesStyles.col)(
              <.div(^.className := ConclusionOfOperationSequenceStyles.introWrapper)(
                <.p(
                  ^.className := Seq(ConclusionOfOperationSequenceStyles.intro, TextStyles.bigText, TextStyles.boldText)
                )(unescape(I18n.t("operation.sequence.conclusion.title")))
              ),
              <.div(^.className := ConclusionOfOperationSequenceStyles.messageWrapper)(
                <.p(^.className := Seq(ConclusionOfOperationSequenceStyles.message, TextStyles.smallText))(
                  if (self.props.wrapped.isConnected || self.state.subscriptionToNewsletterHasSucceed) {
                    unescape(I18n.t("operation.sequence.conclusion.info"))
                  } else {
                    unescape(I18n.t("operation.sequence.conclusion.prompting-to-subscribe-to-newsletter"))
                  }
                )
              )
            )
          ),
          if (!self.props.wrapped.isConnected && !self.state.subscriptionToNewsletterHasSucceed) {
            <.SubscribeToNewsletterFormContainerComponent(
              ^.wrapped := SubscribeToNewsletterFormContainerProps(
                onSubscribeToNewsletterSuccess = onSubscribeToNewsletterSuccess()
              )
            )()
          },
          <.style()(ConclusionOfOperationSequenceStyles.render[String])
        )
      }
    )
}

object ConclusionOfOperationSequenceStyles extends StyleSheet.Inline {
  import dsl._

  val intro: StyleA =
    style(textAlign.center)

  val introWrapper: StyleA =
    style(marginBottom(ThemeStyles.SpacingValue.medium.pxToEm()))

  val message: StyleA =
    style(textAlign.center, color(ThemeStyles.TextColor.lighter))

  val messageWrapper: StyleA =
    style(marginTop(ThemeStyles.SpacingValue.small.pxToEm()), marginBottom(ThemeStyles.SpacingValue.small.pxToEm()))

}
