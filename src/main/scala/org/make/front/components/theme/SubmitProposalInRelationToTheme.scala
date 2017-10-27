package org.make.front.components.theme

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.elements.ReactElement
import org.make.front.components.Components._
import org.make.front.components.submitProposal.SubmitProposalAndLoginContainer.SubmitProposalAndLoginContainerProps
import org.make.front.facades.I18n
import org.make.front.facades.Unescape.unescape
import org.make.front.models.{GradientColor => GradientColorModel, Theme => ThemeModel}
import org.make.front.styles.ThemeStyles
import org.make.front.styles.base.TextStyles
import org.make.front.styles.utils._

import scalacss.DevDefaults._
import scalacss.internal.Attr
import scalacss.internal.mutable.StyleSheet
import scalacss.internal.mutable.StyleSheet.Inline

object SubmitProposalInRelationToTheme {

  case class SubmitProposalInRelationToThemeProps(theme: ThemeModel, onProposalProposed: () => Unit)

  case class SubmitProposalInRelationToThemeState(theme: ThemeModel)

  lazy val reactClass: ReactClass =
    React.createClass[SubmitProposalInRelationToThemeProps, SubmitProposalInRelationToThemeState](
      displayName = "SubmitProposalInRelationToTheme",
      getInitialState = { self =>
        SubmitProposalInRelationToThemeState(theme = self.props.wrapped.theme)
      },
      render = { self =>
        val gradientValues: GradientColorModel = self.state.theme.gradient.getOrElse(GradientColorModel("#FFF", "#FFF"))

        object DynamicSubmitProposalInRelationToThemeStyles extends Inline {
          import dsl._

          val titleBackground = style(
            background := s"-webkit-linear-gradient(94deg, ${gradientValues.from}, ${gradientValues.to})",
            Attr.real("-webkit-background-clip") := "text",
            Attr.real("-webkit-text-fill-color") := "transparent"
          )
        }

        val intro: (ReactElement) => ReactElement = {
          element =>
            <.div()(
              <.h2(^.className := SubmitProposalInRelationToThemeStyles.title)(
                <.span(
                  ^.className := Seq(
                    TextStyles.mediumText,
                    TextStyles.intro,
                    SubmitProposalInRelationToThemeStyles.intro
                  )
                )(unescape(I18n.t("theme.submit-proposal.intro"))),
                <.br()(),
                <.strong(
                  ^.className := Seq(
                    TextStyles.veryBigTitle,
                    SubmitProposalInRelationToThemeStyles.theme,
                    DynamicSubmitProposalInRelationToThemeStyles.titleBackground
                  )
                )(unescape(self.state.theme.title))
              ),
              element,
              <.style()(
                SubmitProposalInRelationToThemeStyles.render[String],
                DynamicSubmitProposalInRelationToThemeStyles.render[String]
              )
            )
        }

        <.SubmitProposalAndLoginComponent(
          ^.wrapped :=
            SubmitProposalAndLoginContainerProps(
              intro = intro,
              maybeTheme = Some(self.props.wrapped.theme),
              maybeOperation = None,
              onProposalProposed = self.props.wrapped.onProposalProposed
            )
        )()
      }
    )

}

object SubmitProposalInRelationToThemeStyles extends StyleSheet.Inline {

  import dsl._

  val title: StyleA =
    style(textAlign.center)

  val intro: StyleA =
    style(
      display.inlineBlock,
      marginBottom(ThemeStyles.SpacingValue.smaller.pxToEm(15)),
      ThemeStyles.MediaQueries.beyondSmall(marginBottom(ThemeStyles.SpacingValue.smaller.pxToEm(18)))
    )

  val theme: StyleA =
    style(
      display.inlineBlock,
      marginBottom(15.pxToEm(30)),
      lineHeight(41.pxToEm(30)),
      ThemeStyles.MediaQueries.beyondSmall(marginBottom(10.pxToEm(40)), lineHeight(56.pxToEm(40))),
      ThemeStyles.MediaQueries.beyondMedium(marginBottom(10.pxToEm(60)), lineHeight(83.pxToEm(60)))
    )
}
