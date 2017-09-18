package org.make.front.components.Theme

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import org.make.front.components.Modals.FullscreenModalComponent.FullscreenModalProps
import org.make.front.components.SubmitProposal.SubmitProposalInRelationToThemeComponent.SubmitProposalInRelationToThemeProps
import org.make.front.components.presentationals._
import org.make.front.facades._
import org.make.front.models.{GradientColor, Theme, ThemeId}
import org.make.front.styles.{InputStyles, LayoutRulesStyles, TextStyles, ThemeStyles}
import org.scalajs.dom.raw.HTMLElement

import scalacss.DevDefaults._
import scalacss.internal.Length
import scalacss.internal.mutable.StyleSheet

object ThemeHeaderComponent {

  case class ThemeHeaderProps(maybeTheme: Option[Theme])

  case class ThemeHeaderState(isProposalModalOpened: Boolean)

  lazy val reactClass: ReactClass =
    React.createClass[ThemeHeaderProps, ThemeHeaderState](
      displayName = getClass.toString,
      getInitialState = { _ =>
        ThemeHeaderState(isProposalModalOpened = false)
      },
      render = (self) => {

        var proposalInput: Option[HTMLElement] = None

        def toggleProposalModal() = () => {
          self.setState(state => state.copy(isProposalModalOpened = !self.state.isProposalModalOpened))

        }

        def openProposalModalFromInput() = () => {
          self.setState(state => state.copy(isProposalModalOpened = true))
          proposalInput.foreach(_.blur())
        }

        val theme: Theme = self.props.wrapped.maybeTheme.getOrElse(Theme(ThemeId("asdf"), "-", "", 0, 0, "#FFF"))
        val gradient: GradientColor = theme.gradient.getOrElse(GradientColor("#FFF", "#FFF"))

        <.header(
          ^.className := Seq(
            ThemeHeaderStyles.wrapper,
            ThemeHeaderStyles.gradientBackground(gradient.from, gradient.to)
          )
        )(
          <.div(^.className := ThemeHeaderStyles.innerWrapper)(
            <.img(^.className := ThemeHeaderStyles.illustration, ^.src := imageShutterstock.toString)(),
            <.div(^.className := LayoutRulesStyles.centeredRow)(
              <.div(^.className := LayoutRulesStyles.col)(
                <.h1(^.className := Seq(TextStyles.veryBigTitle, ThemeHeaderStyles.title))(theme.title),
                <.p(
                  ^.className := Seq(
                    InputStyles.wrapper,
                    InputStyles.withIcon,
                    InputStyles.biggerWithIcon,
                    ThemeHeaderStyles.proposalInputWithIconWrapper
                  )
                )(
                  <.span(^.className := ThemeHeaderStyles.innerWapper)(
                    <.span(^.className := ThemeHeaderStyles.inputWapper)(
                      <.input(
                        ^.`type`.text,
                        ^.value := "Il faut ",
                        ^.ref := ((input: HTMLElement) => proposalInput = Some(input)),
                        ^.onFocus := openProposalModalFromInput()
                      )()
                    ),
                    <.span(^.className := ThemeHeaderStyles.textLimitInfoWapper)(
                      <.span(^.className := Seq(TextStyles.smallText, ThemeHeaderStyles.textLimitInfo))("8/140")
                    )
                  )
                ),
                <.FullscreenModalComponent(
                  ^.wrapped := FullscreenModalProps(self.state.isProposalModalOpened, toggleProposalModal())
                )(
                  <.SubmitProposalInRelationToThemeComponent(
                    ^.wrapped := SubmitProposalInRelationToThemeProps(Some(theme))
                  )()
                )
              )
            )
          ),
          <.style()(ThemeHeaderStyles.render[String])
        )
      }
    )

}

object ThemeHeaderStyles extends StyleSheet.Inline {

  import dsl._

  //TODO: globalize function
  implicit class NormalizedSize(val baseSize: Int) extends AnyVal {
    def pxToEm(browserContextSize: Int = 16): Length[Double] = {
      (baseSize.toFloat / browserContextSize.toFloat).em
    }
  }

  val wrapper: StyleA =
    style(
      position.relative,
      display.table,
      width(100.%%),
      height(500.pxToEm()),
      height :=! s"calc(100% - ${200.pxToEm().value})",
      backgroundColor(ThemeStyles.BackgroundColor.black), //TODO:gradient
      overflow.hidden
    )

  def gradientBackground(from: String, to: String): StyleA =
    style(background := s"linear-gradient(130deg, $from, $to)")

  val innerWrapper: StyleA =
    style(
      position.relative,
      display.tableCell,
      verticalAlign.middle,
      paddingTop((60 + 50).pxToEm()), // TODO: dynamise calcul, if main intro is first child of page
      ThemeStyles.MediaQueries.beyondSmall(paddingTop((60 + 80).pxToEm())),
      paddingBottom(ThemeStyles.SpacingValue.larger.pxToEm()),
      textAlign.center
    )

  val illustration: StyleA =
    style(
      position.absolute,
      top(50.%%),
      left(50.%%),
      height.auto,
      maxHeight.none,
      minHeight(100.%%),
      width.auto,
      maxWidth.none,
      minWidth(100.%%),
      transform := s"translate(-50%, -50%)",
      opacity(0.5),
      filter := s"grayscale(100%)",
      mixBlendMode := "multiply"
    )

  val title: StyleA =
    style(
      display.inlineBlock,
      marginBottom(15.pxToEm(30)),
      lineHeight(41.pxToEm(30)),
      ThemeStyles.MediaQueries.beyondMedium(marginBottom(10.pxToEm(60)), lineHeight(83.pxToEm(60))),
      color(ThemeStyles.TextColor.white),
      textShadow := s"1px 1px 1px rgb(0, 0, 0)"
    )

  val proposalInputWithIconWrapper: StyleA =
    style(
      boxShadow := "0 2px 5px 0 rgba(0,0,0,0.50)",
      (&.before)(content := "'\\F0EB'"),
      unsafeChild("input")(ThemeStyles.Font.circularStdBold)
    )

  val innerWapper: StyleA = style(display.table, width(100.%%))

  val inputWapper: StyleA =
    style(display.tableCell, width(100.%%))

  val textLimitInfoWapper: StyleA = style(display.tableCell, verticalAlign.middle)

  val textLimitInfo: StyleA =
    style(padding(1.em), lineHeight.initial, color(ThemeStyles.TextColor.lighter), whiteSpace.nowrap)

}
