package org.make.front.components.SubmitProposal

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import org.make.front.components.SubmitProposal.SubmitProposalFormContainerComponent.SubmitProposalFormContainerProps
import org.make.front.components.presentationals._
import org.make.front.facades.I18n
import org.make.front.facades.Unescape.unescape
import org.make.front.models.{GradientColor, Theme, ThemeId}
import org.make.front.styles.{TextStyles, ThemeStyles}

import scalacss.DevDefaults._
import scalacss.internal.{Attr, Length}
import scalacss.internal.mutable.StyleSheet

object SubmitProposalInRelationToThemeComponent {

  case class SubmitProposalInRelationToThemeProps(maybeTheme: Option[Theme])

  case class SubmitProposalInRelationToThemeState(theme: Theme)

  lazy val reactClass: ReactClass =
    React.createClass[SubmitProposalInRelationToThemeProps, SubmitProposalInRelationToThemeState](
      displayName = getClass.toString,
      getInitialState = { self =>
        val theme: Theme = self.props.wrapped.maybeTheme.getOrElse(Theme(ThemeId("asdf"), "-", "", 0, 0, "#FFF"))
        SubmitProposalInRelationToThemeState(theme = theme)
      },
      render = (self) => {

        def nextStep() = () => {}

        val gradientColor: GradientColor = self.state.theme.gradient.getOrElse(GradientColor("#FFF", "#FFF"))

        <.article()(
          <.h2(^.className := SubmitProposalInRelationToThemeStyles.title)(
            <.span(^.className := SubmitProposalInRelationToThemeStyles.introOfTitle)(
              unescape(I18n.t("content.proposal.titleIntro"))
            ),
            <.br()(),
            <.strong(
              ^.className := Seq(
                TextStyles.veryBigTitle,
                SubmitProposalInRelationToThemeStyles.theme,
                SubmitProposalInRelationToThemeStyles.gradientColor(gradientColor.from, gradientColor.to)
              )
            )(unescape(self.state.theme.title))
          ),
          <.SubmitProposalFormContainerComponent(
            ^.wrapped :=
              SubmitProposalFormContainerProps(Some(self.state.theme), nextStep())
          )(),
          <.style()(SubmitProposalInRelationToThemeStyles.render[String])
        )
      }
    )

}

object SubmitProposalInRelationToThemeStyles extends StyleSheet.Inline {

  import dsl._

  //TODO: globalize function
  implicit class NormalizedSize(val baseSize: Int) extends AnyVal {
    def pxToEm(browserContextSize: Int = 18): Length[Double] = {
      (baseSize.toFloat / browserContextSize.toFloat).em
    }
  }

  val title: StyleA =
    style(textAlign.center)

  val introOfTitle: StyleA =
    style(
      display.inlineBlock,
      marginBottom(ThemeStyles.Spacing.small),
      ThemeStyles.Font.playfairDisplayItalic,
      fontStyle.italic
    )

  val theme: StyleA =
    style(
      display.inlineBlock,
      marginBottom(15.pxToEm(30)),
      lineHeight(41.pxToEm(30)),
      ThemeStyles.MediaQueries.beyondMedium(marginBottom(10.pxToEm(60)), lineHeight(83.pxToEm(60)))
    )

  /** TODO: pseudo class to customise placeholder */
  def gradientColor(from: String, to: String): StyleA =
    style(
      background := s"-webkit-linear-gradient(94deg, $from, $to)",
      Attr.real("-webkit-background-clip") := "text",
      Attr.real("-webkit-text-fill-color") := "transparent"
    )

}
