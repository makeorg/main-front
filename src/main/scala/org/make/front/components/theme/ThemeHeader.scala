package org.make.front.components.theme

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import org.make.front.components.Components._
import org.make.front.components.modals.FullscreenModal.FullscreenModalProps
import org.make.front.components.theme.SubmitProposalInRelationToTheme.SubmitProposalInRelationToThemeProps
import org.make.front.facades._
import org.make.front.models.{GradientColor => GradientColorModel, Theme => ThemeModel, ThemeId => ThemeIdModel}
import org.make.front.styles.ThemeStyles
import org.make.front.styles.base.{ColRulesStyles, RowRulesStyles, TextStyles}
import org.make.front.styles.ui.InputStyles
import org.make.front.styles.utils._
import org.scalajs.dom.raw.HTMLElement

import scalacss.DevDefaults._
import scalacss.internal.StyleA
import scalacss.internal.mutable.StyleSheet
import scalacss.internal.mutable.StyleSheet.Inline

object ThemeHeader {

  case class ThemeHeaderProps(theme: ThemeModel)

  case class ThemeHeaderState(isProposalModalOpened: Boolean)

  lazy val reactClass: ReactClass =
    React.createClass[ThemeHeaderProps, ThemeHeaderState](
      displayName = "ThemeHeader",
      getInitialState = { _ =>
        ThemeHeaderState(isProposalModalOpened = false)
      },
      render = { self =>
        var proposalInput: Option[HTMLElement] = None

        def toggleProposalModal() = () => {
          self.setState(state => state.copy(isProposalModalOpened = !self.state.isProposalModalOpened))
        }

        def openProposalModalFromInput() = () => {
          self.setState(state => state.copy(isProposalModalOpened = true))
          proposalInput.foreach(_.blur())
        }

        val theme: ThemeModel = self.props.wrapped.theme
        val gradient: GradientColorModel = theme.gradient.getOrElse(GradientColorModel("#FFF", "#FFF"))

        object ThemeSheet extends Inline {
          import dsl._

          val gradientStyle = style(background := s"linear-gradient(130deg, ${gradient.from}, ${gradient.to})")
        }

        <.header(^.className := Seq(ThemeHeaderStyles.wrapper, ThemeSheet.gradientStyle))(
          <.div(^.className := ThemeHeaderStyles.innerWrapper)(
            <.img(
              ^.className := ThemeHeaderStyles.illustration,
              ^.src := imageShutterstock.toString,
              ^("data-pin-no-hover") := "true"
            )(),
            <.div(^.className := RowRulesStyles.centeredRow)(
              <.div(^.className := ColRulesStyles.col)(
                <.h1(^.className := Seq(TextStyles.veryBigTitle, ThemeHeaderStyles.title))(theme.title),
                <.p(
                  ^.className := Seq(
                    InputStyles.wrapper,
                    InputStyles.withIcon,
                    InputStyles.biggerWithIcon,
                    ThemeHeaderStyles.proposalInputWithIconWrapper
                  )
                )(
                  <.span(^.className := ThemeHeaderStyles.inputInnerWrapper)(
                    <.span(^.className := ThemeHeaderStyles.inputSubInnerWrapper)(
                      <.input(
                        ^.`type`.text,
                        ^.value := "Il faut ",
                        ^.readOnly := true,
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
                    ^.wrapped := SubmitProposalInRelationToThemeProps(theme = theme, onProposalProposed = () => {
                      self.setState(_.copy(isProposalModalOpened = false))
                    })
                  )()
                )
              )
            )
          ),
          <.style()(ThemeHeaderStyles.render[String]),
          <.style()(ThemeSheet.render[String])
        )
      }
    )

}

object ThemeHeaderStyles extends StyleSheet.Inline {

  import dsl._

  val wrapper: StyleA =
    style(
      position.relative,
      display.table,
      width(100.%%),
      height(300.pxToEm()),
      /*height :=! s"calc(100% - ${200.pxToEm().value})",*/
      backgroundColor(ThemeStyles.BackgroundColor.black), //TODO:gradient
      overflow.hidden
    )

  val innerWrapper: StyleA =
    style(
      position.relative,
      display.tableCell,
      verticalAlign.middle,
      paddingTop((ThemeStyles.SpacingValue.larger + 50).pxToEm()), // TODO: dynamise calcul, if main intro is first child of page
      ThemeStyles.MediaQueries.beyondSmall(paddingTop((ThemeStyles.SpacingValue.larger + 80).pxToEm())),
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
      ThemeStyles.MediaQueries.beyondSmall(marginBottom(10.pxToEm(40)), lineHeight(56.pxToEm(40))),
      ThemeStyles.MediaQueries.beyondMedium(marginBottom(10.pxToEm(60)), lineHeight(83.pxToEm(60))),
      color(ThemeStyles.TextColor.white),
      textShadow := s"1px 1px 1px rgb(0, 0, 0)"
    )

  val proposalInputWithIconWrapper: StyleA =
    style(
      boxShadow := "0 2px 5px 0 rgba(0,0,0,0.50)",
      (&.before)(content := "'\\F0EB'"),
      unsafeChild("input")(ThemeStyles.Font.circularStdBold, cursor.text)
    )

  val inputInnerWrapper: StyleA = style(display.table, width(100.%%))

  val inputSubInnerWrapper: StyleA =
    style(display.tableCell, width(100.%%))

  val textLimitInfoWapper: StyleA = style(display.tableCell, verticalAlign.middle)

  val textLimitInfo: StyleA =
    style(
      display.inlineBlock,
      lineHeight :=! s"${28.pxToEm(13).value}",
      paddingLeft(ThemeStyles.SpacingValue.small.pxToEm(13)),
      ThemeStyles.MediaQueries
        .beyondSmall(lineHeight :=! s"${38.pxToEm(16).value}", paddingLeft(ThemeStyles.SpacingValue.small.pxToEm(16))),
      ThemeStyles.MediaQueries.beyondMedium(lineHeight :=! s"${48.pxToEm(16).value}"),
      color(ThemeStyles.TextColor.lighter),
      whiteSpace.nowrap
    )

}
