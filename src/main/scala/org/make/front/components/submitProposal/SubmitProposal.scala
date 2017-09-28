package org.make.front.components.submitProposal

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import org.make.front.components.Components._
import org.make.front.components.submitProposal.SubmitProposalAndLoginContainer.SubmitProposalAndLoginContainerProps
import org.make.front.facades.I18n
import org.make.front.facades.Unescape.unescape
import org.make.front.models.{GradientColor => GradientColorModel, Theme => ThemeModel, ThemeId => ThemeIdModel}
import org.make.front.styles.{TextStyles, ThemeStyles}

import scalacss.DevDefaults._
import scalacss.internal.mutable.StyleSheet
import scalacss.internal.{Attr, Length}

object SubmitProposal {

  case class SubmitProposalProps(onProposalProposed: () => Unit)

  case class SubmitProposalState()

  lazy val reactClass: ReactClass =
    React.createClass[SubmitProposalProps, SubmitProposalState](
      displayName = getClass.toString,
      getInitialState = { _ =>
        SubmitProposalState()
      },
      render = { self =>
        <.article()(
          <.h2(^.className := Seq(TextStyles.mediumText, TextStyles.intro, SubmitProposalStyles.intro))(
            unescape(I18n.t("content.proposal.titleIntroNoTheme"))
          ),
          <.SubmitProposalAndLoginComponent(
            ^.wrapped :=
              SubmitProposalAndLoginContainerProps(
                bait = "il faut",
                proposalContentMaxLength = 140,
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

  //TODO: globalize function
  implicit class NormalizedSize(val baseSize: Int) extends AnyVal {
    def pxToEm(browserContextSize: Int = 16): Length[Double] = {
      (baseSize.toFloat / browserContextSize.toFloat).em
    }
  }

  val intro: StyleA =
    style(
      textAlign.center,
      marginBottom(ThemeStyles.SpacingValue.medium.pxToEm(15)),
      ThemeStyles.MediaQueries.beyondSmall(marginBottom(ThemeStyles.SpacingValue.medium.pxToEm(18)))
    )
}
