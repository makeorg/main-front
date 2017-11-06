package org.make.front.components.operation

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import org.make.front.components.Components._
import org.make.front.components.modals.FullscreenModal.FullscreenModalProps
import org.make.front.components.operation.SubmitProposalInRelationToOperation.SubmitProposalInRelationToOperationProps
import org.make.front.facades.I18n
import org.make.front.facades.Unescape.unescape
import org.make.front.models.{Operation => OperationModel}
import org.make.front.styles._
import org.make.front.styles.base.{ColRulesStyles, RowRulesStyles, TextStyles}
import org.make.front.styles.ui.InputStyles
import org.make.front.styles.utils._
import org.scalajs.dom.raw.HTMLElement

import scalacss.DevDefaults._
import scalacss.internal.mutable.StyleSheet

object OperationHeader {

  case class OperationHeaderProps(operation: OperationModel)

  case class OperationHeaderState(isProposalModalOpened: Boolean)

  lazy val reactClass: ReactClass =
    React.createClass[OperationHeaderProps, OperationHeaderState](
      displayName = "OperationHeader",
      getInitialState = { _ =>
        OperationHeaderState(isProposalModalOpened = false)
      },
      render = (self) => {

        var proposalInput: Option[HTMLElement] = None

        val closeProposalModal: () => Unit = () => {
          self.setState(state => state.copy(isProposalModalOpened = false))
        }

        def openProposalModalFromInput() = () => {
          self.setState(state => state.copy(isProposalModalOpened = true))
          proposalInput.foreach(_.blur())
        }

        val operation: OperationModel = self.props.wrapped.operation

        <.header(^.className := OperationHeaderStyles.wrapper)(
          <.div(^.className := RowRulesStyles.centeredRow)(
            <.div(^.className := ColRulesStyles.col)(
              <.h1(^.className := Seq(TextStyles.mediumTitle, OperationHeaderStyles.title))(unescape(operation.title)),
              <.p(
                ^.className := Seq(
                  TextStyles.biggerMediumText,
                  TextStyles.intro,
                  OperationHeaderStyles.proposalInputIntro,
                  OperationHeaderStyles.coloredProposalInputIntro(operation.color)
                )
              )(unescape(I18n.t("operation.proposal-form-in-header.intro"))),
              <.p(
                ^.className := Seq(
                  InputStyles.wrapper,
                  InputStyles.withIcon,
                  InputStyles.biggerWithIcon,
                  OperationHeaderStyles.proposalInputWithIconWrapper
                )
              )(
                <.span(^.className := OperationHeaderStyles.inputInnerWrapper)(
                  <.span(^.className := OperationHeaderStyles.inputSubInnerWrapper)(
                    <.input(
                      ^.`type`.text,
                      ^.value := I18n.t("operation.proposal-form-in-header.bait"),
                      ^.ref := ((input: HTMLElement) => proposalInput = Some(input)),
                      ^.onFocus := openProposalModalFromInput()
                    )()
                  ),
                  <.span(^.className := OperationHeaderStyles.textLimitInfoWapper)(
                    <.span(^.className := Seq(TextStyles.smallText, OperationHeaderStyles.textLimitInfo))(
                      I18n.t("operation.proposal-form-in-header.limit-of-chars-info")
                    )
                  )
                )
              ),
              <.FullscreenModalComponent(
                ^.wrapped := FullscreenModalProps(
                  isModalOpened = self.state.isProposalModalOpened,
                  closeCallback = closeProposalModal
                )
              )(
                <.SubmitProposalInRelationToOperationComponent(
                  ^.wrapped := SubmitProposalInRelationToOperationProps(
                    operation = operation,
                    onProposalProposed = closeProposalModal
                  )
                )()
              )
            )
          ),
          <.style()(OperationHeaderStyles.render[String])
        )
      }
    )

}

object OperationHeaderStyles extends StyleSheet.Inline {

  import dsl._

  val wrapper: StyleA =
    style(
      display.block,
      padding :=! s"${ThemeStyles.SpacingValue.medium.pxToEm().value} 0",
      ThemeStyles.MediaQueries.beyondSmall(padding :=! s"${ThemeStyles.SpacingValue.larger.pxToEm().value} 0"),
      backgroundColor(ThemeStyles.BackgroundColor.blackMoreTransparent)
    )

  val title: StyleA =
    style(textAlign.center)

  val proposalInputIntro: StyleA =
    style(marginTop(10.pxToEm(15)), ThemeStyles.MediaQueries.beyondSmall(marginTop(-5.pxToEm(24))), textAlign.center)

  def coloredProposalInputIntro(textColor: String): StyleA =
    style(color :=! textColor)

  val proposalInputWithIconWrapper: StyleA =
    style(
      marginTop(ThemeStyles.SpacingValue.small.pxToEm()),
      boxShadow := "0 2px 5px 0 rgba(0,0,0,0.50)",
      (&.before)(content := "'\\F0EB'"),
      unsafeChild("input")(ThemeStyles.Font.circularStdBold, cursor.text)
    )

  val inputInnerWrapper: StyleA = style(display.table, width(100.%%))

  val inputSubInnerWrapper: StyleA =
    style(display.tableCell, width(100.%%))

  val textLimitInfoWapper: StyleA = style(display.tableCell, verticalAlign.middle)

  val textLimitInfo: StyleA =
    style(padding(1.em), lineHeight.initial, color(ThemeStyles.TextColor.lighter), whiteSpace.nowrap)
}
