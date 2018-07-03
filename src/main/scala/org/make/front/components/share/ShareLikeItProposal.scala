package org.make.front.components.share

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import org.make.front.Main.CssSettings._
import org.make.front.components.Components._
import org.make.front.facades.ReactShare._
import org.make.front.facades.I18n
import org.make.front.facades.Unescape.unescape
import org.make.front.models.{OperationExpanded, Proposal => ProposalModel}
import org.make.front.styles._
import org.make.front.styles.utils._
import org.scalajs.dom

import scala.scalajs.js

object ShareLikeItProposal {

  final case class ShareLikeItProposalProps(proposal: ProposalModel,
                                            operation: OperationExpanded,
                                            country: String)

  val reactClass: ReactClass =
    React
      .createClass[ShareLikeItProposalProps, Unit](
      displayName = "ShareLikeItProposal",
      render = (self) => {

        def url(network: String): String =
          s"${dom.window.location.origin}/${self.props.wrapped.country}/proposal/${self.props.wrapped.proposal.slug}?utm_source=$network&utm_medium=user-share&utm_campaign=${self.props.wrapped.operation.slug}&utm_content=${self.props.wrapped.proposal.id.value}&shared_proposal=voted-proposal#/${self.props.wrapped.country}/proposal/${self.props.wrapped.proposal.slug}"

        <.div()(
          <.p(^.className := ShareLikeItProposalStyles.title)(
            unescape(I18n.t("proposal.share-likeit"))
          ),
          <.ul(^.className := ShareLikeItProposalStyles.shareButtonsList)(
            <.li(^.className := ShareLikeItProposalStyles.shareButtonsItems)(
              <.FacebookShareButton(^.url := url("Facebook"))(
                <.button(
                  ^.className := js.Array(
                    ShareLikeItProposalStyles.shareButton,
                    ShareLikeItProposalStyles.shareWithFacebookButton)
                )()
              )
            ),
            <.li(^.className := ShareLikeItProposalStyles.shareButtonsItems)(
              <.TwitterShareButton(^.url := url("Twitter"))(
                <.button(
                  ^.className := js.Array(
                    ShareLikeItProposalStyles.shareButton,
                    ShareLikeItProposalStyles.shareWithTwitterButton)
                )()
              )
            ),
            <.li(^.className := ShareLikeItProposalStyles.shareButtonsItems)(
              <.GooglePlusShareButton(^.url := url("Google"))(
                <.button(
                  ^.className := js.Array(
                    ShareLikeItProposalStyles.shareButton,
                    ShareLikeItProposalStyles.shareWithGooglePlusButton)
                )()
              )
            ),
            <.li(^.className := ShareLikeItProposalStyles.shareButtonsItems)(
              <.LinkedinShareButton(^.url := url("Linkedin"))(
                <.button(
                  ^.className := js.Array(
                    ShareLikeItProposalStyles.shareButton,
                    ShareLikeItProposalStyles.shareWithLinkedInButton)
                )()
              )
            )
          ),
          <.style()(ShareLikeItProposalStyles.render[String])
        )
      }
    )
}

object ShareLikeItProposalStyles extends StyleSheet.Inline {

  import dsl._


  val title: StyleA =
    style(
      ThemeStyles.Font.circularStdBook,
      fontSize(11.pxToEm()),
      textAlign.left,
      lineHeight(1),
      color(ThemeStyles.TextColor.white),
      textAlign.center,
      ThemeStyles.MediaQueries.beyondMedium(
        textAlign.left,
        fontSize(14.pxToEm())
      )
    )

  val shareButtonsList: StyleA =
    style(
      display.block,
      marginLeft(auto),
      marginRight(auto),
      marginTop(ThemeStyles.SpacingValue.smaller.pxToEm()),
      ThemeStyles.MediaQueries.beyondMedium(
        display.flex,
        justifyContent.spaceBetween,
      )
    )

  val shareButtonsItems: StyleA =
    style(
      display.inlineBlock,
      marginLeft(ThemeStyles.SpacingValue.smaller.pxToEm()),
      marginRight(ThemeStyles.SpacingValue.smaller.pxToEm()),
      ThemeStyles.MediaQueries.beyondMedium(
        marginLeft(`0`),
        marginRight(`0`)
      )
    )

  val shareButton: StyleA = style(
    width(30.pxToEm()),
    height(30.pxToEm()),
    borderRadius(50.%%),
    overflow.hidden,
    boxShadow := s"0 0 0 0 rgba(0, 0, 0, .0)",
    transition := "box-shadow .2s ease-in-out",
    ThemeStyles.MediaQueries.beyondMedium(
      width(40.pxToEm()),
      height(40.pxToEm()),
    ),
    &.before(
      display.inlineBlock,
      width(100.%%),
      fontSize(12.pxToEm()),
      lineHeight(24.pxToEm(12)),
      ThemeStyles.Font.fontAwesome,
      textAlign.center,
      color(ThemeStyles.TextColor.white),
      ThemeStyles.MediaQueries.beyondMedium(
        fontSize(18.pxToEm()),
        lineHeight(24.pxToEm(18))
      )
    ),
    &.hover(boxShadow := s"0 ${1.pxToEm().value} ${1.pxToEm().value} 0 rgba(0, 0, 0, .5)")
  )

  val shareWithFacebookButton: StyleA =
    style(backgroundColor(ThemeStyles.SocialNetworksColor.facebook), &.before(content := "'\\f09a'"))
  val shareWithTwitterButton: StyleA =
    style(backgroundColor(ThemeStyles.SocialNetworksColor.twitter), &.before(content := "'\\f099'"))
  val shareWithGooglePlusButton: StyleA =
    style(backgroundColor(ThemeStyles.SocialNetworksColor.googlePlus), &.before(content := "'\\f0d5'"))
  val shareWithLinkedInButton: StyleA =
    style(backgroundColor(ThemeStyles.SocialNetworksColor.linkedIn), &.before(content := "'\\f0e1'"))

}
