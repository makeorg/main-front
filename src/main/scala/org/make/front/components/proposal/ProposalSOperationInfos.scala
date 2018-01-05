package org.make.front.components.proposal

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM.{<, ^, _}
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.router.dom.RouterDOM._
import org.make.front.Main.CssSettings._
import org.make.front.components.Components._
import org.make.front.facades.I18n
import org.make.front.facades.Unescape.unescape
import org.make.front.models.{OperationExpanded => OperationModel}
import org.make.front.styles.ThemeStyles
import org.make.front.styles.base.{RWDHideRulesStyles, TableLayoutBeyondMediumStyles, TextStyles}
import org.make.front.styles.ui.CTAStyles
import org.make.front.styles.utils._

object ProposalSOperationInfos {

  final case class ProposalSOperationInfosProps(operation: OperationModel)

  lazy val reactClass: ReactClass =
    React
      .createClass[ProposalSOperationInfosProps, Unit](
        displayName = "ProposalInRelationToOperationFooter",
        render = (self) => {
          object DynamicProposalSOperationInfosStyles extends StyleSheet.Inline {

            import dsl._

            val operationName =
              style(color :=! self.props.wrapped.operation.theme.color)
          }

          <.div(^.className := ProposalSOperationInfosStyles.wrapper)(
            <.div(^.className := TableLayoutBeyondMediumStyles.wrapper)(
              <.p(
                ^.className := Seq(
                  ProposalSOperationInfosStyles.infosWrapper,
                  TableLayoutBeyondMediumStyles.cellVerticalAlignMiddle
                )
              )(
                <.span(^.className := Seq(ProposalSOperationInfosStyles.intro, TextStyles.smallText))(
                  unescape(I18n.t("proposal.proposal-s-operation-infos.intro"))
                ),
                <.span(^.className := RWDHideRulesStyles.hideBeyondMedium)(" "),
                <.span(
                  ^.className := Seq(
                    TextStyles.verySmallTitle,
                    ProposalSOperationInfosStyles.operationName,
                    DynamicProposalSOperationInfosStyles.operationName
                  )
                )(unescape(self.props.wrapped.operation.wording.title))
              ),
              <.div(
                ^.className := Seq(
                  ProposalSOperationInfosStyles.CTAsWrapper,
                  TableLayoutBeyondMediumStyles.cellVerticalAlignMiddle
                )
              )(
                <.p(^.className := ProposalSOperationInfosStyles.CTA)(
                  <.Link(
                    ^.to := s"/consultation/${self.props.wrapped.operation.slug}",
                    ^.className := Seq(CTAStyles.basic, CTAStyles.basicOnA)
                  )(unescape(I18n.t("proposal.proposal-s-operation-infos.participate")))
                ),
                <.p(^.className := ProposalSOperationInfosStyles.CTA)(
                  <.a(
                    ^.href := I18n.t("proposal.proposal-s-operation-infos.see-more-link"),
                    ^.className := Seq(CTAStyles.basic, CTAStyles.basicOnA),
                    ^.target := "_blank"
                  )(unescape(I18n.t("proposal.proposal-s-operation-infos.see-more")))
                )
              )
            ),
            <.style()(ProposalSOperationInfosStyles.render[String], DynamicProposalSOperationInfosStyles.render[String])
          )
        }
      )
}

object ProposalSOperationInfosStyles extends StyleSheet.Inline {

  import dsl._

  val wrapper: StyleA =
    style(
      borderTop(1.px, solid, ThemeStyles.BorderColor.veryLight),
      paddingTop(ThemeStyles.SpacingValue.small.pxToEm())
    )

  val infosWrapper: StyleA =
    style(
      ThemeStyles.MediaQueries.beyondMedium(
        paddingRight(ThemeStyles.SpacingValue.medium.pxToEm()),
        borderRight(1.px, solid, ThemeStyles.BorderColor.veryLight),
        textAlign.right
      )
    )

  val intro: StyleA =
    style(color(ThemeStyles.TextColor.lighter))

  val operationName: StyleA =
    style(
      ThemeStyles.MediaQueries
        .beyondMedium(display.block, marginTop(ThemeStyles.SpacingValue.smaller.pxToEm()))
    )

  val CTAsWrapper: StyleA =
    style(
      marginTop((ThemeStyles.SpacingValue.small - (ThemeStyles.SpacingValue.smaller / 2)).pxToEm()),
      marginBottom((-1 * (ThemeStyles.SpacingValue.smaller / 2)).pxToEm()),
      ThemeStyles.MediaQueries
        .beyondMedium(margin(`0`), paddingLeft(ThemeStyles.SpacingValue.medium.pxToEm()), whiteSpace.nowrap)
    )

  val CTA: StyleA =
    style(
      display.inlineBlock,
      margin(
        (ThemeStyles.SpacingValue.smaller / 2).pxToEm(),
        ThemeStyles.SpacingValue.small.pxToEm(),
        (ThemeStyles.SpacingValue.smaller / 2).pxToEm(),
        `0`
      )
    )

}
