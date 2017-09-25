package org.make.front.components.proposals.proposal

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import org.make.front.components.Components._
import org.make.front.facades.Unescape.unescape
import org.make.front.facades.{I18n, Replacements}
import org.make.front.models.{Proposal => ProposalModel}
import org.make.front.styles.{TextStyles, ThemeStyles}

import scalacss.DevDefaults._
import scalacss.internal.Length
import scalacss.internal.mutable.StyleSheet

object ShareOwnProposal {

  final case class ShareOwnProposalProps(proposal: ProposalModel)

  val reactClass: ReactClass =
    React.createClass[ShareOwnProposalProps, Unit](render = (self) => {

      <.div(^.className := ShareOwnProposalStyles.wrapper)(
        <.h4(^.className := Seq(TextStyles.smallText, ShareOwnProposalStyles.intro))("Ma proposition"),
        <.div(^.className := ShareOwnProposalStyles.shareButtonsListWrapper)(
          <.ul(^.className := ShareOwnProposalStyles.shareButtonsList)(
            <.li(^.className := ShareOwnProposalStyles.shareButtonItem)(
              <.button(
                ^.className := Seq(ShareOwnProposalStyles.shareButton, ShareOwnProposalStyles.shareWithFacebookButton)
              )()
            ),
            <.li(^.className := ShareOwnProposalStyles.shareButtonItem)(
              <.button(
                ^.className := Seq(ShareOwnProposalStyles.shareButton, ShareOwnProposalStyles.shareWithTwitterButton)
              )()
            ),
            <.li(^.className := ShareOwnProposalStyles.shareButtonItem)(
              <.button(
                ^.className := Seq(ShareOwnProposalStyles.shareButton, ShareOwnProposalStyles.shareWithGooglePlusButton)
              )()
            ),
            <.li(^.className := ShareOwnProposalStyles.shareButtonItem)(
              <.button(
                ^.className := Seq(ShareOwnProposalStyles.shareButton, ShareOwnProposalStyles.shareWithLinkedInButton)
              )()
            )
          )
        ),
        <.style()(ShareOwnProposalStyles.render[String])
      )
    })
}

object ShareOwnProposalStyles extends StyleSheet.Inline {

  import dsl._

  //TODO: globalize function
  implicit class NormalizedSize(val baseSize: Int) extends AnyVal {
    def pxToEm(browserContextSize: Int = 16): Length[Double] = {
      (baseSize.toFloat / browserContextSize.toFloat).em
    }
  }

  val wrapper: StyleA =
    style(display.table, width(100.%%))

  val intro: StyleA =
    style(display.tableCell, verticalAlign.top, color(ThemeStyles.ThemeColor.prominent))

  val shareButtonsListWrapper: StyleA = style(display.tableCell, verticalAlign.top)

  val shareButtonsList: StyleA = style(textAlign.right, margin(-2.px))

  val shareButtonItem: StyleA = style(display.inlineBlock, margin(2.px), verticalAlign.middle)

  val shareButton: StyleA = style(
    display.inlineBlock,
    verticalAlign.middle,
    width(24.pxToEm()),
    height(24.pxToEm()),
    borderRadius(50.%%),
    overflow.hidden,
    boxShadow := s"0 0 0 0 rgba(0, 0, 0, .0)",
    transition := "box-shadow .2s ease-in-out",
    (&.before)(
      display.inlineBlock,
      width(100.%%),
      fontSize(12.pxToEm()),
      lineHeight(24.pxToEm(12)),
      ThemeStyles.Font.fontAwesome,
      textAlign.center,
      color(ThemeStyles.TextColor.white)
    ),
    (&.hover)(boxShadow := s"0 ${1.pxToEm().value} ${1.pxToEm().value} 0 rgba(0, 0, 0, .5)")
  )

  val shareWithFacebookButton: StyleA =
    style(backgroundColor(ThemeStyles.SocialNetworksColor.facebook), (&.before)(content := "'\\f09a'"))
  val shareWithTwitterButton: StyleA =
    style(backgroundColor(ThemeStyles.SocialNetworksColor.twitter), (&.before)(content := "'\\f099'"))
  val shareWithGooglePlusButton: StyleA =
    style(backgroundColor(ThemeStyles.SocialNetworksColor.googlePlus), (&.before)(content := "'\\f0d5'"))
  val shareWithLinkedInButton: StyleA =
    style(backgroundColor(ThemeStyles.SocialNetworksColor.linkedIn), (&.before)(content := "'\\f0e1'"))

}
