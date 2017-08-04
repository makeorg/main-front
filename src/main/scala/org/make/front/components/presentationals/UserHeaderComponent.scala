package org.make.front.components.presentationals

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.elements.ReactElement
import org.make.front.facades.{imageAvatar, I18n}
import org.make.front.styles.{BulmaStyles, FontAwesomeStyles, MakeStyles}

import scalacss.DevDefaults._
import scalacss.internal.StyleA
import scalacss.internal.mutable.StyleSheet

object UserHeaderComponent {
  case class WrappedProps(isConnected: Boolean, userFirstName: Option[String], avatarUrl: Option[String])
  case class State(avatarUrl: String)

  lazy val reactClass: ReactClass =
    React.createClass[WrappedProps, State](
      getInitialState = { self =>
        State(avatarUrl = self.props.wrapped.avatarUrl.getOrElse(imageAvatar.toString))
      },
      render = self =>
        <.nav(^.className := BulmaStyles.Components.Navbar.navbarEnd)(if (self.props.wrapped.isConnected) {
          ConnectedUserHeaderElement(self.props.wrapped.userFirstName.get, self.state.avatarUrl)
        } else {
          UnconnectedUserHeaderElement()
        }, <.style()(UserHeaderComponentStyles.render[String]))
    )
}

object ConnectedUserHeaderElement {
  def apply(userFirstName: String, avatarUrl: String): ReactElement =
    <.div(
      ^.className := Seq(
        BulmaStyles.Components.Navbar.navbarItem,
        BulmaStyles.Helpers.hasDropdown,
        BulmaStyles.Helpers.isHoverable
      )
    )(
      <.a(^.className := Seq(BulmaStyles.Components.Navbar.navbarLink, UserHeaderComponentStyles.pinkAfter))(
        <.img(^.src := avatarUrl, ^.style := Map("maxHeight" -> "100%"))(),
        <.span(^.className := UserHeaderComponentStyles.displayName)(userFirstName)
      ),
      <.div(^.className := BulmaStyles.Components.Navbar.navbarDropdown)(
        <.a(
          ^.className := Seq(BulmaStyles.Components.Navbar.navbarItem, UserHeaderComponentStyles.listItem),
          ^.href := "/me"
        )(I18n.t("profile")),
        <.a(
          ^.className := Seq(BulmaStyles.Components.Navbar.navbarItem, UserHeaderComponentStyles.listItem),
          ^.href := "/me/settings"
        )(I18n.t("settings")),
        <.hr(^.className := BulmaStyles.Components.Navbar.navbarDivider)(),
        <.a(
          ^.className := Seq(BulmaStyles.Components.Navbar.navbarItem, UserHeaderComponentStyles.listItem),
          ^.href := "/logout"
        )(I18n.t("logout"))
      )
    )

}

object UnconnectedUserHeaderElement {
  def apply(): ReactElement =
    <.div(^.className := Seq(BulmaStyles.Components.Navbar.navbarItem, BulmaStyles.Helpers.isHoverable))(
      <.a(^.className := UserHeaderComponentStyles.title, ^.href := "/login")(
        <.span(^.className := Seq(BulmaStyles.Element.icon, UserHeaderComponentStyles.spacing))(
          <.i(^.className := FontAwesomeStyles.user)()
        ),
        <.span(^.className := Seq(UserHeaderComponentStyles.colored, UserHeaderComponentStyles.spacing))(
          I18n.t("connect")
        ),
        <.span(^.className := UserHeaderComponentStyles.spacing)("/"),
        <.span(^.className := Seq(UserHeaderComponentStyles.colored, UserHeaderComponentStyles.spacing))(
          I18n.t("createAccount")
        )
      )
    )
}

object UserHeaderComponentStyles extends StyleSheet.Inline {
  import dsl._

  val title: StyleA =
    style(
      height(2.rem),
//      fontFamily(MakeStyles.Font.tradeGothicLTStd),
      color(MakeStyles.Color.grey),
      textTransform.uppercase
    )

  val pinkAfter: StyleA = style((&.after)(borderColor(MakeStyles.Color.pink)))

  val colored: StyleA = style(color(MakeStyles.Color.pink))

  val spacing: StyleA = style(margin(0.rem, .3.rem))

  val displayName: StyleA = style(
    //      fontFamily(MakeStyles.Font.tradeGothicLTStd),
    paddingLeft(1.2.rem),
    color(MakeStyles.Color.black),
    textTransform.uppercase
  )

  //TODO: implement list item's CSS when the design is set
  val listItem: StyleA = style()
}
