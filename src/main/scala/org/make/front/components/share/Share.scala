package org.make.front.components.share

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM.{<, ^, _}
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import org.make.front.Main.CssSettings._
import org.make.front.components.Components._
import org.make.front.facades.ReactShare._
import org.make.front.styles.ThemeStyles
import org.make.front.styles.base.TextStyles
import org.make.front.styles.utils._
import org.scalajs.dom

object ShareProposal {

  final case class ShareProps(url: String, intro: Option[String] = None)

  val reactClass: ReactClass =
    React
      .createClass[ShareProps, Unit](
        displayName = "ShareProposal",
        render = (self) => {
          val shareUrl: String = s"${dom.window.location.origin}${self.props.wrapped.url}"

          <.div(^.className := ShareStyles.wrapper)(if (self.props.wrapped.intro.isDefined) {
            <.div(^.className := ShareStyles.intro)(
              <.p(^.className := TextStyles.smallerTitle)(self.props.wrapped.intro.getOrElse(""))
            )
          },
          <.ul(^.className := ShareStyles.list)(
            <.li(^.className := ShareStyles.item)(
              <.FacebookShareButton(
                ^.url := shareUrl
              )(<.button(^.className := Seq(ShareStyles.button, ShareStyles.shareWithFacebookButton))())
            ),
            <.li(^.className := ShareStyles.item)(
              <.TwitterShareButton(
                ^.url := shareUrl
              )(<.button(^.className := Seq(ShareStyles.button, ShareStyles.shareWithTwitterButton))())
            ),
            <.li(^.className := ShareStyles.item)(
              <.GooglePlusShareButton(
                ^.url := shareUrl
              )(<.button(^.className := Seq(ShareStyles.button, ShareStyles.shareWithGooglePlusButton))())
            ),
            <.li(^.className := ShareStyles.item)(
              <.LinkedinShareButton(
                ^.url := shareUrl
              )(<.button(^.className := Seq(ShareStyles.button, ShareStyles.shareWithLinkedInButton))())
            )
          ),
          <.style()(ShareStyles.render[String]))
        }
      )
}

object ShareStyles extends StyleSheet.Inline {

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
    width(30.pxToEm()),
    height(30.pxToEm()),
    borderRadius(50.%%),
    overflow.hidden,
    boxShadow := s"0 0 0 0 rgba(0, 0, 0, .0)",
    transition := "box-shadow .2s ease-in-out",
    (&.before)(
      display.inlineBlock,
      width(100.%%),
      fontSize(13.pxToEm()),
      lineHeight(30.pxToEm(13)),
      ThemeStyles.Font.fontAwesome,
      textAlign.center,
      color(ThemeStyles.TextColor.white)
    ),
    (&.hover)(boxShadow := s"0 1px 1px 0 rgba(0, 0, 0, .5)"),
    ThemeStyles.MediaQueries.beyondSmall(
      width(40.pxToEm()),
      height(40.pxToEm()),
      (&.before)(fontSize(18.pxToEm()), lineHeight(40.pxToEm(18)))
    )
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
