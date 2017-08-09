package org.make.front.components.presentationals

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.elements.ReactElement
import org.make.front.facades.Translate.TranslateVirtualDOMElements
import org.make.front.facades.{imageAvatar, I18n}
import org.make.front.styles.{BulmaStyles, FontAwesomeStyles, MakeStyles}

import scalacss.DevDefaults._
import scalacss.internal.StyleA
import scalacss.internal.mutable.StyleSheet

object UserHeaderComponent {
  case class WrappedProps(isConnected: Boolean,
                          userFirstName: Option[String],
                          avatarUrl: Option[String],
                          login: ()  => Unit,
                          logout: () => Unit)
  case class State(avatarUrl: String)

  lazy val reactClass: ReactClass =
    React.createClass[WrappedProps, State](
      componentWillReceiveProps = { (self, props) =>
        self.setState(State(avatarUrl = props.wrapped.avatarUrl.getOrElse(imageAvatar.toString)))
      },
      render = self =>
        <.nav(^.className := BulmaStyles.Components.Navbar.navbarEnd)(
          if (self.props.wrapped.isConnected) {
            ConnectedUserHeaderElement(
              self.props.wrapped.userFirstName.get,
              self.state.avatarUrl,
              self.props.wrapped.logout
            )
          } else {
            UnconnectedUserHeaderElement(self.props.wrapped.login)
          },
          <.style()(UserHeaderComponentStyles.render[String])
      )
    )
}

object ConnectedUserHeaderElement {
  def apply(userFirstName: String, avatarUrl: String, logout: () => Unit): ReactElement =
    <.div(
      ^.className := Seq(
        BulmaStyles.Components.Navbar.navbarItem,
        BulmaStyles.Helpers.hasDropdown,
        BulmaStyles.Helpers.isHoverable
      )
    )(
      <.a(^.className := Seq(BulmaStyles.Components.Navbar.navbarLink, UserHeaderComponentStyles.pinkAfter))(
        <.img(^.className := UserHeaderComponentStyles.avatarImage, ^.src := avatarUrl)(),
        <.span(^.className := UserHeaderComponentStyles.displayName)(userFirstName)
      ),
      <.div(^.className := BulmaStyles.Components.Navbar.navbarDropdown)(
        <.a(
          ^.className := Seq(BulmaStyles.Components.Navbar.navbarItem, UserHeaderComponentStyles.listItem),
          ^.href := "/me"
        )(I18n.t("content.header.profile")),
        <.a(
          ^.className := Seq(BulmaStyles.Components.Navbar.navbarItem, UserHeaderComponentStyles.listItem),
          ^.href := "/me/settings"
        )(I18n.t("content.header.settings")),
        <.hr(^.className := BulmaStyles.Components.Navbar.navbarDivider)(),
        <.a(
          ^.className := Seq(BulmaStyles.Components.Navbar.navbarItem, UserHeaderComponentStyles.listItem),
          ^.onClick := logout
        )(I18n.t("content.header.logout"))
      )
    )

}

object UnconnectedUserHeaderElement {
  def apply(login: () => Unit): ReactElement =
    <.div(^.className := Seq(BulmaStyles.Components.Navbar.navbarItem, BulmaStyles.Helpers.isHoverable))(
      <.a(^.className := UserHeaderComponentStyles.title, ^.onClick := login)(
        <.span(^.className := Seq(BulmaStyles.Element.icon, UserHeaderComponentStyles.spacing))(
          <.i(^.className := FontAwesomeStyles.user)()
        ),
        <.Translate(
          ^.className := Seq(UserHeaderComponentStyles.colored, UserHeaderComponentStyles.spacing),
          ^.value := "content.header.connect"
        )(),
        <.span(^.className := UserHeaderComponentStyles.spacing)("/"),
        <.Translate(
          ^.className := Seq(UserHeaderComponentStyles.colored, UserHeaderComponentStyles.spacing),
          ^.value := "content.header.createAccount"
        )()
      )
    )
}

object UserHeaderComponentStyles extends StyleSheet.Inline {
  import dsl._

  val title: StyleA =
    style(height(2.rem), MakeStyles.Font.tradeGothicLTStd, color(MakeStyles.Color.grey), textTransform.uppercase)

  val pinkAfter: StyleA = style((&.after)(borderColor(MakeStyles.Color.pink)))

  val colored: StyleA = style(color(MakeStyles.Color.pink))

  val spacing: StyleA = style(margin(0.rem, .3.rem))

  val displayName: StyleA = style(
    MakeStyles.Font.tradeGothicLTStd,
    paddingLeft(1.2.rem),
    color(MakeStyles.Color.black),
    textTransform.uppercase
  )

  //TODO: implement list item's CSS when the design is set
  val listItem: StyleA = style()

  val avatarImage: StyleA = style(maxHeight(100.%%).important, borderRadius(50.%%), width(3.6.rem), height(3.6.rem))
}
