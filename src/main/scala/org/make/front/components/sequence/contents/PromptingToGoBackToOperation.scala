package org.make.front.components.sequence.contents
import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM.{<, ^, _}
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.router.dom.RouterDOM._
import org.make.front.Main.CssSettings._
import org.make.front.components.Components._
import org.make.front.facades.Unescape.unescape
import org.make.front.facades.{FacebookPixel, I18n}
import org.make.front.models.{GradientColor => GradientColorModel, Operation => OperationModel}
import org.make.front.styles.ThemeStyles
import org.make.front.styles.base.{LayoutRulesStyles, TextStyles, _}
import org.make.front.styles.ui.CTAStyles
import org.make.front.styles.utils._
object PromptingToGoBackToOperation {

  final case class PromptingToGoBackToOperationProps(operation: OperationModel, clickOnButtonHandler: () => Unit)

  final case class PromptingToGoBackToOperationState()

  lazy val reactClass: ReactClass =
    React
      .createClass[PromptingToGoBackToOperationProps, PromptingToGoBackToOperationState](
        displayName = "PromptingToGoBackToOperation",
        componentDidMount = { _ =>
          FacebookPixel.fbq("trackCustom", "display-finale-card")
        },
        getInitialState = { _ =>
          PromptingToGoBackToOperationState()
        },
        render = { self =>
          val gradientValues: GradientColorModel =
            self.props.wrapped.operation.gradient.getOrElse(GradientColorModel("#FFF", "#FFF"))

          object DynamicPromptingToGoBackToOperationStyles extends StyleSheet.Inline {
            import dsl._

            val gradient = style(background := s"linear-gradient(130deg, ${gradientValues.from}, ${gradientValues.to})")
          }

          <.div(^.className := TableLayoutStyles.fullHeightWrapper)(
            <.div(^.className := TableLayoutStyles.row)(
              <.div(^.className := TableLayoutStyles.cell)(
                <.div(^.className := Seq(LayoutRulesStyles.row, PromptingToGoBackToOperationStyles.introWrapper))(
                  <.p(
                    ^.className := Seq(
                      PromptingToGoBackToOperationStyles.intro,
                      TextStyles.bigText,
                      TextStyles.boldText
                    )
                  )(unescape(I18n.t("sequence.prompting-to-continue.intro")))
                )
              )
            ),
            <.div(^.className := TableLayoutStyles.fullHeightRow)(
              <.div(^.className := Seq(TableLayoutStyles.cell, LayoutRulesStyles.row))(
                <.div(
                  ^.className := Seq(
                    PromptingToGoBackToOperationStyles.contentWrapper,
                    TableLayoutStyles.fullHeightWrapper
                  )
                )(
                  <.div(^.className := TableLayoutStyles.cell)(
                    <.div(
                      ^.className := Seq(
                        TableLayoutStyles.fullHeightWrapper,
                        PromptingToGoBackToOperationStyles.learnMoreAccessWrapper
                      )
                    )(
                      <.div(^.className := Seq(TableLayoutStyles.cellVerticalAlignMiddle, LayoutRulesStyles.row))(
                        <.div(^.className := PromptingToGoBackToOperationStyles.learnMoreAccessContent)(
                          <.p(
                            ^.className := Seq(
                              PromptingToGoBackToOperationStyles.learnMoreAccessIntro,
                              TextStyles.mediumText
                            )
                          )(unescape(I18n.t("sequence.prompting-to-continue.learn-more.intro"))),
                          <.p(^.className := PromptingToGoBackToOperationStyles.learnMoreAccessLogoWrapper)(
                            <.img(
                              ^.src := self.props.wrapped.operation.darkerLogoUrl.getOrElse(""),
                              ^.alt := self.props.wrapped.operation.title
                            )()
                          ),
                          <.p(^.className := PromptingToGoBackToOperationStyles.ctaWrapper)(
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
              )
            ),
            <.style()(
              PromptingToGoBackToOperationStyles.render[String],
              DynamicPromptingToGoBackToOperationStyles.render[String]
            )
          )
        }
      )
}

object PromptingToGoBackToOperationStyles extends StyleSheet.Inline {
  import dsl._

  val intro: StyleA =
    style(textAlign.center)

  val introWrapper: StyleA =
    style(marginBottom(ThemeStyles.SpacingValue.medium.pxToEm()))

  val contentWrapper: StyleA = style(maxWidth(1030.pxToEm()), margin(`0`, auto))

  val ctaWrapper: StyleA =
    style(marginTop(ThemeStyles.SpacingValue.small.pxToEm()))

  val learnMoreAccessWrapper: StyleA =
    style(
      padding(ThemeStyles.SpacingValue.small.pxToEm(), `0`),
      ThemeStyles.MediaQueries.beyondSmall(padding(ThemeStyles.SpacingValue.medium.pxToEm(), `0`)),
      textAlign.center,
      backgroundColor(ThemeStyles.BackgroundColor.white),
      backgroundImage := "linear-gradient(155deg, #FFFFFF 0%, #ECECEC 100%)",
      boxShadow := "0 1px 1px 0 rgba(0,0,0,0.50)"
    )

  val learnMoreAccessContent: StyleA = style(display.inlineBlock)

  val learnMoreAccessIntro: StyleA =
    style(color(ThemeStyles.TextColor.lighter))

  val learnMoreAccessLogoWrapper: StyleA =
    style(margin(ThemeStyles.SpacingValue.smaller.pxToEm(), auto), maxWidth(240.pxToEm()))
}
