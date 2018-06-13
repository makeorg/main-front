package org.make.front.components.showcase

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM.{<, ^, _}
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import org.make.front.Main.CssSettings._
import org.make.front.components.Components._
import org.make.front.components.modals.FullscreenModal
import org.make.front.components.operation.SubmitProposalInRelationToOperation.SubmitProposalInRelationToOperationProps
import org.make.front.facades.I18n
import org.make.front.facades.Unescape.unescape
import org.make.front.models.{OperationExpanded => OperationModel}
import org.make.front.styles.OperationStyles
import org.make.front.styles.base.{TableLayoutStyles, TextStyles}
import org.make.front.styles.ui.InputStyles
import org.make.front.styles.utils._
import org.make.front.styles.vendors.FontAwesomeStyles
import org.make.services.tracking.TrackingService.TrackingContext
import org.make.services.tracking.{TrackingLocation, TrackingService}

import scala.scalajs.js

object PromptingToProposeInRelationToOperationTile {

  final case class PromptingToProposeInRelationToOperationTileProps(operation: OperationModel, language: String)

  case class PromptingToProposeInRelationToOperationTileState(isProposalModalOpened: Boolean)

  lazy val reactClass: ReactClass =
    React
      .createClass[PromptingToProposeInRelationToOperationTileProps, PromptingToProposeInRelationToOperationTileState](
        displayName = "PromptingToProposeInRelationToOperationTile",
        getInitialState = { _ =>
          PromptingToProposeInRelationToOperationTileState(isProposalModalOpened = false)
        },
        render = { self =>
          def closeProposalModal(): () => Unit = () => {
            self.setState(state => state.copy(isProposalModalOpened = false))
          }

          def openProposalModalFromInput(): () => Unit = () => {
            self.setState(state => state.copy(isProposalModalOpened = true))
            TrackingService.track(
              "click-proposal-submit-form-open",
              TrackingContext(TrackingLocation.showcaseHomepage),
              Map("operationId" -> self.props.wrapped.operation.operationId.value)
            )
          }

          <.article(
            ^.className := js.Array(PromptingToProposeInRelationToOperationTileStyles.wrapper),
            ^.onClick := openProposalModalFromInput
          )(
            <.div(^.className := TableLayoutStyles.fullHeightWrapper)(
              <.div(
                ^.className := js.Array(TableLayoutStyles.row, PromptingToProposeInRelationToOperationTileStyles.row)
              )(
                <.div(
                  ^.className := js.Array(
                    TableLayoutStyles.cell,
                    TableLayoutStyles.cellVerticalAlignBottom,
                    PromptingToProposeInRelationToOperationTileStyles.cell,
                    PromptingToProposeInRelationToOperationTileStyles.introWrapper
                  )
                )(
                  <.div(^.className := TableLayoutStyles.wrapper)(
                    <.div(^.className := js.Array(TableLayoutStyles.cell, TableLayoutStyles.cellVerticalAlignBottom))(
                      <.h3(^.className := js.Array(TextStyles.mediumText, TextStyles.boldText))(
                        unescape(I18n.t("operation-showcase.prompting-to-propose-tile.intro"))
                      )
                    ),
                    <.div(^.className := js.Array(TableLayoutStyles.cell, TableLayoutStyles.cellVerticalAlignBottom))(
                      <.span(
                        ^.className := js.Array(
                          FontAwesomeStyles.lightbulbTransparent,
                          PromptingToProposeInRelationToOperationTileStyles.picto
                        )
                      )()
                    )
                  )
                )
              ),
              <.div(
                ^.className := js.Array(TableLayoutStyles.row, PromptingToProposeInRelationToOperationTileStyles.row)
              )(
                <.div(
                  ^.className := js.Array(
                    TableLayoutStyles.cell,
                    PromptingToProposeInRelationToOperationTileStyles.cell,
                    PromptingToProposeInRelationToOperationTileStyles.inputWrapper
                  )
                )(
                  <.p(
                    ^.className := js.Array(
                      InputStyles.wrapper,
                      PromptingToProposeInRelationToOperationTileStyles.proposalInputWithIconWrapper
                    )
                  )(
                    <.input(
                      ^.`type`.text,
                      ^.value := I18n.t("operation-showcase.prompting-to-propose-tile.bait"),
                      ^.readOnly := true
                    )()
                  )
                )
              ),
              <.FullscreenModalComponent(
                ^.wrapped := FullscreenModal.FullscreenModalProps(
                  isModalOpened = self.state.isProposalModalOpened,
                  closeCallback = closeProposalModal()
                )
              )(
                <.SubmitProposalInRelationToOperationComponent(
                  ^.wrapped := SubmitProposalInRelationToOperationProps(
                    operation = self.props.wrapped.operation,
                    onProposalProposed = () => {
                      self.setState(_.copy(isProposalModalOpened = false))
                    },
                    maybeLocation = None,
                    maybeSequence = None,
                    language = self.props.wrapped.language
                  )
                )()
              )
            ),
            <.style()(PromptingToProposeInRelationToOperationTileStyles.render[String])
          )
        }
      )
}

object PromptingToProposeInRelationToOperationTileStyles extends StyleSheet.Inline {

  import dsl._

  val wrapper: StyleA =
    style(
      height(100.%%),
      minHeight(360.pxToEm()),
      OperationStyles.MediaQueries.belowMedium(minHeight.inherit),
      minWidth(240.pxToEm()),
      backgroundColor(OperationStyles.BackgroundColor.white),
      boxShadow := "0 1px 1px 0 rgba(0,0,0,0.50)",
      cursor.pointer
    )

  val row: StyleA =
    style(height(50.%%))

  val cell: StyleA =
    style(padding(`0`, OperationStyles.SpacingValue.small.pxToEm()))

  val introWrapper: StyleA = style(
    paddingTop(OperationStyles.SpacingValue.medium.pxToEm()),
    paddingBottom((OperationStyles.SpacingValue.small / 2).pxToEm())
  )

  val inputWrapper: StyleA = style(
    paddingTop((OperationStyles.SpacingValue.small / 2).pxToEm()),
    paddingBottom(OperationStyles.SpacingValue.medium.pxToEm())
  )

  val picto: StyleA =
    style(fontSize(115.pxToEm()), lineHeight(100.0 / 115.0), color(OperationStyles.TextColor.veryLight))

  val proposalInputWithIconWrapper: StyleA =
    style(
      backgroundColor(OperationStyles.BackgroundColor.lightGrey),
      unsafeChild("input")(color(OperationStyles.TextColor.lighter), cursor.pointer)
    )
}
