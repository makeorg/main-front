package org.make.front.components.submitProposal

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import org.make.front.components.Components._
import org.make.front.components.submitProposal.SubmitProposalAndLoginContainer.SubmitProposalAndLoginContainerProps
import org.make.front.facades.I18n
import org.make.front.facades.Unescape.unescape
import org.make.front.styles._
import org.make.front.styles.base.TextStyles
import org.make.front.styles.utils._

import scalacss.DevDefaults._
import scalacss.internal.mutable.StyleSheet

object SubmitProposal {

  case class SubmitProposalProps(onProposalProposed: () => Unit)

  case class SubmitProposalState()

  lazy val reactClass: ReactClass =
    React.createClass[SubmitProposalProps, SubmitProposalState](
      displayName = "SubmitProposal",
      getInitialState = { _ =>
        SubmitProposalState()
      },
      render = { self =>
        <.article()(
          <.h2(^.className := Seq(TextStyles.mediumText, TextStyles.intro, SubmitProposalStyles.intro))(
            unescape(I18n.t("submit-proposal.intro"))
          ),
          <.SubmitProposalAndLoginComponent(
            ^.wrapped :=
              SubmitProposalAndLoginContainerProps(
                maybeTheme = None,
                maybeOperation = None,
                onProposalProposed = self.props.wrapped.onProposalProposed
              )
          )(),
          <.style()(SubmitProposalStyles.render[String])
        )
      }
    )
}

object SubmitProposalStyles extends StyleSheet.Inline {

  import dsl._

  val intro: StyleA =
    style(
      textAlign.center,
      marginBottom(ThemeStyles.SpacingValue.medium.pxToEm(15)),
      ThemeStyles.MediaQueries.beyondSmall(marginBottom(ThemeStyles.SpacingValue.medium.pxToEm(18)))
    )
}
