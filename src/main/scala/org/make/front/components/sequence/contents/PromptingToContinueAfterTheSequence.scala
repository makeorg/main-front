package org.make.front.components.sequence.contents
import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM.{<, ^, _}
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.router.dom.RouterDOM._
import org.make.front.Main.CssSettings._
import org.make.front.components.Components.{RichVirtualDOMElements, _}
import org.make.front.components.share.ShareProposal.ShareProps
import org.make.front.facades.I18n
import org.make.front.facades.Unescape.unescape
import org.make.front.models.{GradientColor => GradientColorModel, Operation => OperationModel}
import org.make.front.styles.ThemeStyles
import org.make.front.styles.base.{ColRulesStyles, LayoutRulesStyles, TextStyles, _}
import org.make.front.styles.ui.CTAStyles
import org.make.front.styles.utils._
import org.make.front.styles.vendors.FontAwesomeStyles
object PromptingToContinueAfterTheSequence {

  final case class PromptingToContinueAfterTheSequenceProps(operation: OperationModel, clickOnButtonHandler: () => Unit)

  final case class PromptingToContinueAfterTheSequenceState()

  lazy val reactClass: ReactClass =
    React
      .createClass[PromptingToContinueAfterTheSequenceProps, PromptingToContinueAfterTheSequenceState](
        displayName = "PromptingToContinueAfterTheSequence",
        getInitialState = { _ =>
          PromptingToContinueAfterTheSequenceState()
        },
        render = { self =>
          val gradientValues: GradientColorModel =
            self.props.wrapped.operation.gradient.getOrElse(GradientColorModel("#FFF", "#FFF"))

          object DynamicPromptingToContinueAfterTheSequenceStyles extends StyleSheet.Inline {
            import dsl._

            val gradient = style(background := s"linear-gradient(130deg, ${gradientValues.from}, ${gradientValues.to})")
          }

          <.div(^.className := TableLayoutStyles.fullHeightWrapper)(
            <.div(^.className := TableLayoutStyles.row)(
              <.div(^.className := Seq(TableLayoutStyles.cell))(
                <.div(
                  ^.className := Seq(LayoutRulesStyles.row, PromptingToContinueAfterTheSequenceStyles.introWrapper)
                )(
                  <.p(
                    ^.className := Seq(
                      PromptingToContinueAfterTheSequenceStyles.intro,
                      TextStyles.bigText,
                      TextStyles.boldText
                    )
                  )(unescape(I18n.t("sequence.prompting-to-continue.intro")))
                )
              )
            ),
            <.div(^.className := TableLayoutStyles.fullHeightRow)(
              <.div(^.className := Seq(TableLayoutStyles.cell, LayoutRulesStyles.rowWithCols))(
                <.div(
                  ^.className := Seq(
                    PromptingToContinueAfterTheSequenceStyles.contentWrapper,
                    TableLayoutBeyondMediumStyles.fullHeightWrapper
                  )
                )(
                  <.div(
                    ^.className := Seq(
                      TableLayoutBeyondMediumStyles.cell,
                      ColRulesStyles.col,
                      ColRulesStyles.colTwoThirdsBeyondMedium
                    )
                  )(
                    <.div(
                      ^.className := Seq(
                        PromptingToContinueAfterTheSequenceStyles.nextSequenceAccessWrapper,
                        TableLayoutBeyondMediumStyles.fullHeightWrapper,
                        DynamicPromptingToContinueAfterTheSequenceStyles.gradient
                      )
                    )(
                      <.div(
                        ^.className := Seq(TableLayoutBeyondMediumStyles.cellVerticalAlignMiddle, LayoutRulesStyles.row)
                      )(
                        <.p(
                          ^.className := Seq(
                            PromptingToContinueAfterTheSequenceStyles.nextSequenceAccessIntro,
                            TextStyles.mediumText
                          )
                        )(unescape(I18n.t("sequence.prompting-to-continue.continue.intro"))),
                        <.div(^.className := PromptingToContinueAfterTheSequenceStyles.nextSequenceAccessTitleWrapper)(
                          <.p(
                            ^.className := Seq(
                              PromptingToContinueAfterTheSequenceStyles.nextSequenceAccessTitle,
                              TextStyles.biggerMediumText,
                              TextStyles.boldText
                            )
                          )(unescape("Comment lutter contre les violences faites aux&nbsp;femmes&nbsp;?"))
                        ),
                        <.div(^.className := PromptingToContinueAfterTheSequenceStyles.ctaWrapper)(
                          <.button(
                            ^.className := Seq(CTAStyles.basic, CTAStyles.basicOnButton),
                            ^.onClick := self.props.wrapped.clickOnButtonHandler
                          )(
                            <.i(^.className := Seq(FontAwesomeStyles.play))(),
                            unescape("&nbsp;" + I18n.t("sequence.prompting-to-continue.continue.cta"))
                          )
                        )
                      )
                    )
                  ),
                  <.div(
                    ^.className := Seq(
                      TableLayoutBeyondMediumStyles.cell,
                      ColRulesStyles.col,
                      ColRulesStyles.colThirdBeyondMedium
                    )
                  )(
                    <.div(
                      ^.className := Seq(
                        TableLayoutBeyondMediumStyles.fullHeightWrapper,
                        PromptingToContinueAfterTheSequenceStyles.learnMoreAccessWrapper
                      )
                    )(
                      <.div(
                        ^.className := Seq(TableLayoutBeyondMediumStyles.cellVerticalAlignMiddle, LayoutRulesStyles.row)
                      )(
                        <.p(
                          ^.className := Seq(
                            PromptingToContinueAfterTheSequenceStyles.learnMoreAccessIntro,
                            TextStyles.mediumText
                          )
                        )(unescape(I18n.t("sequence.prompting-to-continue.learn-more.intro"))),
                        <.p(^.className := PromptingToContinueAfterTheSequenceStyles.learnMoreAccessLogoWrapper)(
                          <.img(
                            ^.src := self.props.wrapped.operation.darkerLogoUrl.getOrElse(""),
                            ^.alt := self.props.wrapped.operation.title
                          )()
                        ),
                        <.p(
                          ^.className := Seq(
                            PromptingToContinueAfterTheSequenceStyles.learnMoreAccessIntro,
                            TextStyles.mediumText
                          )
                        )(unescape(I18n.t("sequence.prompting-to-continue.learn-more.continuation"))),
                        <.p(^.className := PromptingToContinueAfterTheSequenceStyles.ctaWrapper)(
                          <.Link(
                            ^.to := s"/consultation/${self.props.wrapped.operation.slug}",
                            ^.className := Seq(CTAStyles.basic, CTAStyles.basicOnA)
                          )(unescape(I18n.t("sequence.prompting-to-continue.learn-more.cta")))
                        )
                      )
                    )
                  )
                )
              )
            ),
            <.div(^.className := TableLayoutStyles.row)(
              <.div(^.className := Seq(TableLayoutStyles.cell))(
                <.div(
                  ^.className := Seq(LayoutRulesStyles.row, PromptingToContinueAfterTheSequenceStyles.shareWrapper)
                )(
                  <.ShareComponent(
                    ^.wrapped := ShareProps(
                      intro = Some(unescape(I18n.t("sequence.prompting-to-continue.share.intro")))
                    )
                  )()
                )
              )
            ),
            <.style()(
              PromptingToContinueAfterTheSequenceStyles.render[String],
              DynamicPromptingToContinueAfterTheSequenceStyles.render[String]
            )
          )
        }
      )
}

object PromptingToContinueAfterTheSequenceStyles extends StyleSheet.Inline {
  import dsl._

  val intro: StyleA =
    style(textAlign.center)

  val introWrapper: StyleA =
    style(marginBottom(ThemeStyles.SpacingValue.medium.pxToEm()))

  val shareWrapper: StyleA =
    style(marginTop(ThemeStyles.SpacingValue.medium.pxToEm()))

  val contentWrapper: StyleA = style(maxWidth(1030.pxToEm()), margin(`0`, auto))

  val nextSequenceAccessWrapper: StyleA =
    style(
      padding(ThemeStyles.SpacingValue.small.pxToEm(), `0`),
      marginBottom(ThemeStyles.SpacingValue.smaller.pxToEm()),
      ThemeStyles.MediaQueries.beyondSmall(padding(ThemeStyles.SpacingValue.medium.pxToEm(), `0`)),
      ThemeStyles.MediaQueries.beyondMedium(textAlign.center),
      boxShadow := "0 1px 1px 0 rgba(0,0,0,0.50)"
    )

  val nextSequenceAccessIntro: StyleA =
    style(color(ThemeStyles.TextColor.white))

  val nextSequenceAccessTitle: StyleA =
    style(color(ThemeStyles.TextColor.white))

  val nextSequenceAccessTitleWrapper: StyleA = style(margin(ThemeStyles.SpacingValue.small.pxToEm(), `0`))

  val ctaWrapper: StyleA =
    style(marginTop(ThemeStyles.SpacingValue.small.pxToEm()))

  val learnMoreAccessWrapper: StyleA =
    style(
      padding(ThemeStyles.SpacingValue.small.pxToEm(), `0`),
      ThemeStyles.MediaQueries.beyondSmall(padding(ThemeStyles.SpacingValue.medium.pxToEm(), `0`)),
      backgroundColor(ThemeStyles.BackgroundColor.white),
      backgroundImage := "linear-gradient(155deg, #FFFFFF 0%, #ECECEC 100%)",
      boxShadow := "0 1px 1px 0 rgba(0,0,0,0.50)"
    )

  val learnMoreAccessIntro: StyleA =
    style(color(ThemeStyles.TextColor.lighter))

  val learnMoreAccessLogoWrapper: StyleA =
    style(margin(ThemeStyles.SpacingValue.smaller.pxToEm(), `0`), maxWidth(240.pxToEm()))
}
