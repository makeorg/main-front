package org.make.front.components.sequence.contents

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM.{<, _}
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import org.make.front.Main.CssSettings._
import org.make.front.components.Components._
import org.make.front.components.subscribeToNewsletter.SubscribeToNewsletterFormContainer.SubscribeToNewsletterFormContainerProps
import org.make.front.facades.Unescape.unescape
import org.make.front.facades.{FacebookPixel, I18n}
import org.make.front.styles.ThemeStyles
import org.make.front.styles.base.{LayoutRulesStyles, TextStyles}
import org.make.front.styles.utils._

object ConclusionOfTheSequence {

  final case class ConclusionOfTheSequenceProps(isConnected: Boolean)

  final case class ConclusionOfTheSequenceState(subscriptionToNewsletterHasSucceed: Boolean)

  lazy val reactClass: ReactClass =
    React.createClass[ConclusionOfTheSequenceProps, ConclusionOfTheSequenceState](
      displayName = "ConclusionOfTheSequence",
      getInitialState = { self =>
        ConclusionOfTheSequenceState(subscriptionToNewsletterHasSucceed = false)
      },
      render = { self =>
        def onSubscribeToNewsletterSuccess(): () => Unit = { () =>
          FacebookPixel.fbq("trackCustom", "click-email-submit")
          self.setState(_.copy(subscriptionToNewsletterHasSucceed = true))
        }

        <.div()(
          <.div(^.className := Seq(LayoutRulesStyles.row))(
            <.div(^.className := ConclusionOfTheSequenceStyles.introWrapper)(
              <.p(^.className := Seq(ConclusionOfTheSequenceStyles.intro, TextStyles.bigText, TextStyles.boldText))(
                unescape(I18n.t("sequence.conclusion.title"))
              )
            ),
            <.div(^.className := ConclusionOfTheSequenceStyles.messageWrapper)(
              <.p(^.className := Seq(ConclusionOfTheSequenceStyles.message, TextStyles.smallText))(
                if (self.props.wrapped.isConnected || self.state.subscriptionToNewsletterHasSucceed) {
                  unescape(I18n.t("sequence.conclusion.info"))
                } else {
                  unescape(I18n.t("sequence.conclusion.prompting-to-subscribe-to-newsletter"))
                }
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
          <.style()(ConclusionOfTheSequenceStyles.render[String])
        )
      }
    )
}

object ConclusionOfTheSequenceStyles extends StyleSheet.Inline {
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
