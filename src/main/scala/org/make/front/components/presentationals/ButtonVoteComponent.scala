package org.make.front.components.presentationals

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.React.Self
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.elements.ReactElement
import io.github.shogowada.scalajs.reactjs.events.MouseSyntheticEvent
import org.make.front.facades.I18n
import org.make.front.styles.{BulmaStyles, FontAwesomeStyles, MakeStyles}
import org.scalajs.dom.document

import scala.util.Random
import scalacss.DevDefaults._

object ButtonVoteComponent {

  final case class ButtonColor(backgroundColor: String, color: String, hoverColor: String)

  final case class ButtonVoteState(buttonUpStyle: StyleA,
                                   buttonDownStyle: StyleA,
                                   buttonNeutralStyle: StyleA,
                                   buttonQualifTop: (String) => StyleA,
                                   buttonQualifMiddle: (String) => StyleA,
                                   buttonQualifBottom: (String) => StyleA,
                                   isClickStats: Boolean,
                                   isClickQualifTop: Boolean,
                                   isClickQualifMiddle: Boolean,
                                   isClickQualifBottom: Boolean,
                                   idRandom: String
                                  )

  def onClickButtonUp(self: Self[Unit, ButtonVoteState]): (MouseSyntheticEvent) => Unit = {
    _: MouseSyntheticEvent => {
      if (!self.state.isClickStats) {
        document.getElementById(s"buttonDown-${self.state.idRandom}").setAttribute("style", "opacity: 0; display: none")
        document.getElementById(s"buttonNeutral-${self.state.idRandom}").setAttribute("style", "opacity: 0; display: none")
        document.getElementById(s"stats-${self.state.idRandom}").setAttribute("style", "display: block; color: #6eb61f")
        document.getElementById(s"qualifUp-${self.state.idRandom}").setAttribute("style", "display: block")
        self.setState(_.copy(buttonUpStyle = ButtonVoteStyle.buttonStats("#6eb61f"), isClickStats = true))
      }
      else {
        document.getElementById(s"buttonDown-${self.state.idRandom}").setAttribute("style", "opacity: 1; display: flex")
        document.getElementById(s"buttonNeutral-${self.state.idRandom}").setAttribute("style", "opacity: 1; display: flex")
        document.getElementById(s"stats-${self.state.idRandom}").setAttribute("style", "display: none")
        document.getElementById(s"qualifUp-${self.state.idRandom}").setAttribute("style", "")
        self.setState(_.copy(
          buttonUpStyle = ButtonVoteStyle.button("#6eb61f"),
          isClickStats = false,
          buttonQualifTop = (color: String) => ButtonVoteStyle.buttonQualif(color),
          buttonQualifMiddle = (color: String) => ButtonVoteStyle.buttonQualif(color),
          buttonQualifBottom = (color: String) => ButtonVoteStyle.buttonQualif(color),
          isClickQualifTop = false,
          isClickQualifMiddle = false,
          isClickQualifBottom = false
        ))
      }
    }
  }

  def onClickButtonDown(self: Self[Unit, ButtonVoteState]): (MouseSyntheticEvent) => Unit = {
    _: MouseSyntheticEvent => {
      if (!self.state.isClickStats) {
        document.getElementById(s"buttonUp-${self.state.idRandom}").setAttribute("style", "opacity: 0; display: none")
        document.getElementById(s"buttonNeutral-${self.state.idRandom}").setAttribute("style", "opacity: 0; display: none")
        document.getElementById(s"stats-${self.state.idRandom}").setAttribute("style", "display: block; color: #da001a")
        document.getElementById(s"qualifDown-${self.state.idRandom}").setAttribute("style", "display: block")
        self.setState(_.copy(buttonDownStyle = ButtonVoteStyle.buttonStats("#da001a"), isClickStats = true))
      }
      else {
        document.getElementById(s"buttonUp-${self.state.idRandom}").setAttribute("style", "opacity: 1; display: flex")
        document.getElementById(s"buttonNeutral-${self.state.idRandom}").setAttribute("style", "opacity: 1; display: flex")
        document.getElementById(s"stats-${self.state.idRandom}").setAttribute("style", "display: none")
        document.getElementById(s"qualifDown-${self.state.idRandom}").setAttribute("style", "")
        self.setState(_.copy(
          buttonDownStyle = ButtonVoteStyle.button("#da001a"),
          isClickStats = false,
          buttonQualifTop = (color: String) => ButtonVoteStyle.buttonQualif(color),
          buttonQualifMiddle = (color: String) => ButtonVoteStyle.buttonQualif(color),
          buttonQualifBottom = (color: String) => ButtonVoteStyle.buttonQualif(color),
          isClickQualifTop = false,
          isClickQualifMiddle = false,
          isClickQualifBottom = false
        ))
      }
    }
  }

  def onClickButtonNeutral(self: Self[Unit, ButtonVoteState]): (MouseSyntheticEvent) => Unit = {
    _: MouseSyntheticEvent => {
      if (!self.state.isClickStats) {
        document.getElementById(s"buttonUp-${self.state.idRandom}").setAttribute("style", "opacity: 0; display: none")
        document.getElementById(s"buttonDown-${self.state.idRandom}").setAttribute("style", "opacity: 0; display: none")
        document.getElementById(s"stats-${self.state.idRandom}").setAttribute("style", "display: block; color: #9b9b9b")
        document.getElementById(s"qualifNeutral-${self.state.idRandom}").setAttribute("style", "display: block")
        self.setState(_.copy(buttonNeutralStyle = ButtonVoteStyle.buttonStats("#9b9b9b"), isClickStats = true))
      }
      else {
        document.getElementById(s"buttonUp-${self.state.idRandom}").setAttribute("style", "opacity: 1; display: flex")
        document.getElementById(s"buttonDown-${self.state.idRandom}").setAttribute("style", "opacity: 1; display: flex")
        document.getElementById(s"stats-${self.state.idRandom}").setAttribute("style", "display: none")
        document.getElementById(s"qualifNeutral-${self.state.idRandom}").setAttribute("style", "")
        self.setState(_.copy(
          buttonNeutralStyle = ButtonVoteStyle.button("#9b9b9b"),
          isClickStats = false,
          buttonQualifTop = (color: String) => ButtonVoteStyle.buttonQualif(color),
          buttonQualifMiddle = (color: String) => ButtonVoteStyle.buttonQualif(color),
          buttonQualifBottom = (color: String) => ButtonVoteStyle.buttonQualif(color),
          isClickQualifTop = false,
          isClickQualifMiddle = false,
          isClickQualifBottom = false
        ))
      }
    }
  }

  def onClickQualifTop(self: Self[Unit, ButtonVoteState]): (MouseSyntheticEvent) => Unit = {
    _: MouseSyntheticEvent => {
      if (!self.state.isClickQualifTop)
        self.setState(_.copy(buttonQualifTop = (color: String) => ButtonVoteStyle.buttonQualifOn(color), isClickQualifTop = true))
      else
        self.setState(_.copy(buttonQualifTop = (color: String) => ButtonVoteStyle.buttonQualif(color), isClickQualifTop = false))
    }
  }

  def onClickQualifMiddle(self: Self[Unit, ButtonVoteState]): (MouseSyntheticEvent) => Unit = {
    _: MouseSyntheticEvent => {
      if (!self.state.isClickQualifMiddle)
        self.setState(_.copy(buttonQualifMiddle = (color: String) => ButtonVoteStyle.buttonQualifOn(color), isClickQualifMiddle = true))
      else
        self.setState(_.copy(buttonQualifMiddle = (color: String) => ButtonVoteStyle.buttonQualif(color), isClickQualifMiddle = false))
    }
  }

  def onClickQualifBottom(self: Self[Unit, ButtonVoteState]): (MouseSyntheticEvent) => Unit = {
    _: MouseSyntheticEvent => {
      if (!self.state.isClickQualifBottom)
        self.setState(_.copy(buttonQualifBottom = (color: String) => ButtonVoteStyle.buttonQualifOn(color), isClickQualifBottom = true))
      else
        self.setState(_.copy(buttonQualifBottom = (color: String) => ButtonVoteStyle.buttonQualif(color), isClickQualifBottom = false))
    }
  }

  lazy val reactClass: ReactClass = React.createClass[Unit, ButtonVoteState](
    getInitialState = (_) => ButtonVoteState(
      buttonUpStyle = ButtonVoteStyle.button("#6eb61f"),
      buttonDownStyle = ButtonVoteStyle.button("#da001a"),
      buttonNeutralStyle = ButtonVoteStyle.button("#9b9b9b"),
      buttonQualifTop = (color: String) => ButtonVoteStyle.buttonQualif(color),
      buttonQualifMiddle = (color: String) => ButtonVoteStyle.buttonQualif(color),
      buttonQualifBottom = (color: String) => ButtonVoteStyle.buttonQualif(color),
      isClickStats = false,
      isClickQualifTop = false,
      isClickQualifMiddle = false,
      isClickQualifBottom = false,
      idRandom = Random.alphanumeric.take(15).mkString
    ),
    render = (self) =>
      <.div(^.id := s"voteButton-${self.state.idRandom}", ^.className := ButtonVoteStyle.proposalButton)(
        <.div()(
          <.a(^.id := s"buttonUp-${self.state.idRandom}", ^.className := self.state.buttonUpStyle,
            ^.onClick := onClickButtonUp(self))(
            <.i(^.className := Seq(FontAwesomeStyles.thumbsUpTransparent, ButtonVoteStyle.thumbs))(),
            <.span(^.id := "textButton", ^.className := ButtonVoteStyle.textButton)(I18n.t("content.proposal.agree"))
          ),
          <.div(^.id := s"stats-${self.state.idRandom}", ^.className := ButtonVoteStyle.statsBox)(
            <.span()("2.5K"),
            <.span(^.className := ButtonVoteStyle.pourcentages)("75%")
          )
        ),
        <.a(^.id := s"buttonDown-${self.state.idRandom}", ^.className := self.state.buttonDownStyle,
          ^.onClick := onClickButtonDown(self))(
          <.i(^.className := Seq(FontAwesomeStyles.thumbsDownTransparent, ButtonVoteStyle.thumbs))(),
          <.span(^.id := "textButton", ^.className := ButtonVoteStyle.textButton)(I18n.t("content.proposal.disagree"))
        ),
        <.a(^.id := s"buttonNeutral-${self.state.idRandom}", ^.className := self.state.buttonNeutralStyle,
          ^.onClick := onClickButtonNeutral(self))(
          <.i(^.className := Seq(FontAwesomeStyles.thumbsUpTransparent, ButtonVoteStyle.thumbs),
            ^.style := Map("transform" -> "rotate(-90deg)"))(),
          <.span(^.id := "textButton", ^.className := ButtonVoteStyle.textButton)(I18n.t("content.proposal.blank"))
        ),
        reactElement(
          self,
          s"qualifUp-${self.state.idRandom}",
          "#6eb61f",
          <.span()(
            <.span()("Coup de "),
            <.span(^.className := Seq(BulmaStyles.Element.icon, BulmaStyles.Helpers.isSmall))(
              <.i(^.className := FontAwesomeStyles.heart, ^.style := Map("color" -> "#ed1844"))()
            )
          ),
          <.span()("Réaliste"),
          <.span()("Platitude")
        ),
        reactElement(
          self,
          s"qualifDown-${self.state.idRandom}",
          "#da001a",
          <.span()("Surtout pas !"),
          <.span()("Infaisable"),
          <.span()("Platitude")
        ),
        reactElement(
          self,
          s"qualifNeutral-${self.state.idRandom}",
          "#9b9b9b",
          <.span()("Pas compris"),
          <.span()("Pas d'avis"),
          <.span()("Je m'en moque")
        ),
        <.style()(ButtonVoteStyle.render[String])
      )
  )

  private def reactElement(self: Self[Unit, ButtonVoteState],
                           id: String,
                           color: String,
                           textUp: ReactElement,
                           textMiddle: ReactElement,
                           textDown: ReactElement): ReactElement = {
    <.div(^.id := id, ^.className := ButtonVoteStyle.proposalQualif)(
      <.div(^.className := self.state.buttonQualifTop(color), ^.onClick := onClickQualifTop(self))(
        <.div(^.className := ButtonVoteStyle.textQualif)(textUp),
        <.div(^.className := ButtonVoteStyle.plusOne)(<.span()("+1"))
      ),
      <.div(^.className := self.state.buttonQualifMiddle(color), ^.style := Map("top" -> "3.5rem"), ^.onClick := onClickQualifMiddle(self))(
        <.div(^.className := ButtonVoteStyle.textQualif)(textMiddle),
        <.div(^.className := ButtonVoteStyle.plusOne)(<.span()("+1"))
      ),
      <.div(^.className := self.state.buttonQualifBottom(color), ^.style := Map("top" -> "7rem"), ^.onClick := onClickQualifBottom(self))(
        <.div(^.className := ButtonVoteStyle.textQualif)(textDown),
        <.div(^.className := ButtonVoteStyle.plusOne)(<.span()("+1"))
      )
    )
  }

}

object ButtonVoteStyle extends StyleSheet.Inline {
  import dsl._

  val proposalButton: StyleA = style(
    position.relative,
    display.flex,
    justifyContent.center,
    alignItems.flexStart,
    minHeight(12.rem)
  )

  def button(buttonColor: String): StyleA = style(
    position.relative,
    border :=! s"0.2rem solid $buttonColor",
    borderRadius(5.rem),
    fontSize(2.4.rem),
    textAlign.center,
    width(4.8.rem),
    height(4.8.rem),
    margin :=! "0 0.5rem",
    color :=! buttonColor,
    display.flex,
    alignItems.center,
    justifyContent.center,
    backgroundColor :=! "#fff",
    (&.hover)(
      backgroundColor :=! buttonColor,
      color :=! "#fff",
      boxShadow := "0 -0.1rem 0.6rem 0 rgba(0, 0, 0, 0.3)",
      unsafeChild("span")(
        visibility.visible,
        position.absolute,
        padding :=! "0 0.5rem",
        backgroundColor :=! "black",
        color :=! "#fff",
        textAlign.center,
        borderRadius(0.6.rem),
        fontSize(1.4.rem),
        zIndex(1),
        top(111.%%)
      )
    )
  )

  def buttonStats(buttonColor: String): StyleA = style(
    position.relative,
    border :=! s"0.2rem solid $buttonColor",
    borderRadius(5.rem),
    fontSize(2.4.rem),
    backgroundColor :=! buttonColor,
    color :=! "#fff",
    textAlign.center,
    width(4.8.rem),
    height(4.8.rem),
    display.flex,
    alignItems.center,
    justifyContent.center,
    (&.hover)(
      boxShadow := "0 -0.1rem 0.6rem 0 rgba(0, 0, 0, 0.3)",
      color :=! "#fff"
    )
  )

  val thumbs: StyleA = style(
    position.absolute
  )

  val textButton: StyleA = style(
    visibility.hidden,
    whiteSpace.nowrap,
    (&.after)(
      content := "''",
      position.absolute,
      bottom(100.%%),
      left(50.%%),
      marginLeft((-0.5).rem),
      borderWidth(0.5.rem),
      borderStyle.solid,
      borderColor :=! "transparent transparent black transparent"
    )
  )

  def statsBox: StyleA = style(
    position.absolute,
    display.none,
    top(5.rem),
    width(4.8.rem),
    lineHeight(2.rem),
    textAlign.center,
    fontSize(1.6.rem),
    MakeStyles.Font.circularStdBold
  )

  val pourcentages: StyleA = style(
    marginTop(-0.4.rem),
    display.block,
    MakeStyles.Font.circularStdBook,
    fontSize(1.4.rem),
    fontWeight :=! "300",
    color :=! "rgba(0, 0, 0, 0.3)"
  )

  val proposalQualif: StyleA = style(
    position.relative,
    display.none,
    width(15.rem),
    marginLeft(1.rem),
    flex := "0 0 auto"
  )

  def buttonQualif(buttonColor: String): StyleA = style(
    display.flex,
    position.absolute,
    width(100.%%),
    border :=! s"0.2rem solid $buttonColor",
    color :=! buttonColor,
    borderRadius(3.rem),
    padding :=! "0 1rem",
    marginBottom(0.5.rem),
    justifyContent.spaceBetween,
    alignItems.center,
    backgroundColor :=! "#fff",
    cursor.pointer,
    (&.hover)(
      color :=! "#fff",
      backgroundColor :=! buttonColor
    )
  )

  def buttonQualifOn(buttonColor: String): StyleA = style(
    display.flex,
    position.absolute,
    width(100.%%),
    border :=! s"0.2rem solid $buttonColor",
    color :=! "#fff",
    borderRadius(3.rem),
    padding :=! "0 1rem",
    marginBottom(0.5.rem),
    justifyContent.spaceBetween,
    alignItems.center,
    backgroundColor :=! buttonColor,
    cursor.pointer
  )

  val textQualif: StyleA = style(
    display.block,
    fontSize(1.3.rem),
    MakeStyles.Font.circularStdBook
  )

  val plusOne: StyleA = style(
    fontSize(1.8.rem),
    fontWeight :=! "900"
  )
}