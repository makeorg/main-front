package org.make.front.components.proposal

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM.{<, ^, _}
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import org.make.front.components.Components._
import org.make.front.facades.I18n
import org.make.front.facades.Unescape.unescape
import org.make.front.models.{Proposal => ProposalModel}
import org.make.front.styles.ThemeStyles
import org.make.front.styles.base.TextStyles
import org.make.front.styles.utils._

import scalacss.DevDefaults.{StyleA, _}
import scalacss.internal.mutable.StyleSheet
object ShareProposal {

  final case class ShareProposalProps(proposal: ProposalModel)

  val reactClass: ReactClass =
    React
      .createClass[ShareProposalProps, Unit](
        displayName = "ShareProposal",
        render = (self) => {

          <.div(^.className := ShareProposalStyles.wrapper)(
            <.div(^.className := ShareProposalStyles.intro)(
              <.p(^.className := TextStyles.smallerTitle)(unescape(I18n.t("proposal.share-intro")))
            ),
            <.ul(^.className := ShareProposalStyles.list)(
              <.li(^.className := ShareProposalStyles.item)(
                <.button(^.className := Seq(ShareProposalStyles.button, ShareProposalStyles.shareWithFacebookButton))()
              ),
              <.li(^.className := ShareProposalStyles.item)(
                <.button(^.className := Seq(ShareProposalStyles.button, ShareProposalStyles.shareWithTwitterButton))()
              ),
              <.li(^.className := ShareProposalStyles.item)(
                <.button(
                  ^.className := Seq(ShareProposalStyles.button, ShareProposalStyles.shareWithGooglePlusButton)
                )()
              ),
              <.li(^.className := ShareProposalStyles.item)(
                <.button(^.className := Seq(ShareProposalStyles.button, ShareProposalStyles.shareWithLinkedInButton))()
              )
            ),
            <.style()(ShareProposalStyles.render[String])
          )
        }
      )
}

object ShareProposalStyles extends StyleSheet.Inline {

  import dsl._

  val wrapper: StyleA =
    style(textAlign.center)

  val intro: StyleA = style(
    display.inlineBlock,
    color(ThemeStyles.TextColor.lighter),
    marginRight(ThemeStyles.SpacingValue.small.pxToEm()),
    paddingTop(10.pxToEm()),
    paddingBottom(10.pxToEm())
  )

  val list: StyleA = style(display.inlineBlock, margin(-5.pxToEm()))

  val item: StyleA = style(display.inlineBlock, margin(5.pxToEm()), verticalAlign.middle)

  val button: StyleA = style(
    display.inlineBlock,
    verticalAlign.middle,
    width(40.pxToEm()),
    height(40.pxToEm()),
    borderRadius(50.%%),
    overflow.hidden,
    boxShadow := s"0 0 0 0 rgba(0, 0, 0, .0)",
    transition := "box-shadow .2s ease-in-out",
    (&.before)(
      display.inlineBlock,
      width(100.%%),
      fontSize(18.pxToEm()),
      lineHeight(40.pxToEm(18)),
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
