package org.make.front.components.presentationals

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.React.Self
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.elements.ReactElement
import io.github.shogowada.scalajs.reactjs.events.MouseSyntheticEvent
import org.make.front.components.AppComponentStyles
import org.make.front.facades.Translate.TranslateVirtualDOMElements
import org.make.front.facades.I18n
import org.make.front.models.PoliticalAction
import org.make.front.styles.{BulmaStyles, FontAwesomeStyles, MakeStyles}
import org.scalajs.dom

import scalacss.DevDefaults._

object PoliticalActionComponent {

  final case class WrappedProps(politicalActions: Seq[PoliticalAction])

  final case class PoliticalActionState(translationOffset: Int)

  def onClickLeft(self: Self[WrappedProps, PoliticalActionState]): (MouseSyntheticEvent) => Unit = {
    _: MouseSyntheticEvent =>
      {
        val width = dom.document.getElementById("slide").clientWidth
        val oldTranslationOffset = self.state.translationOffset
        if (oldTranslationOffset != 0)
          self.setState(_.copy(translationOffset = oldTranslationOffset + width))
      }
  }

  def onClickRight(self: Self[WrappedProps, PoliticalActionState]): (MouseSyntheticEvent) => Unit = {
    _: MouseSyntheticEvent =>
      {
        val width = dom.document.getElementById("slide").clientWidth
        val oldTranslationOffset = self.state.translationOffset
        if (oldTranslationOffset != -width * (self.props.wrapped.politicalActions.length - 1))
          self.setState(_.copy(translationOffset = oldTranslationOffset - width))
      }
  }

  // toDo: responsive design
  lazy val reactClass: ReactClass = React.createClass[WrappedProps, PoliticalActionState](
    getInitialState = (_) => PoliticalActionState(translationOffset = 0),
    render = (self) =>
      <.div(^.className := BulmaStyles.Helpers.isFullWidth)(
        <.h2(^.className := AppComponentStyles.title2)(
          <.Translate(
            ^.value := "content.theme.actionsCount",
            ^("actions") := self.props.wrapped.politicalActions.length.toString
          )()
        ),
        <.div(^.className := Seq(PoliticalActionStyles.boxWrapper))(
          <.div(^.className := PoliticalActionStyles.themeSliderActions)(
            <.div(^.className := PoliticalActionStyles.blockActionMask, ^.id := "slide")(
              self.props.wrapped.politicalActions.map { politicalAction =>
                textBox(politicalAction, self.state.translationOffset)
              }
            ),
            <.div(^.className := PoliticalActionStyles.arrows)(
              <.span(^.className := PoliticalActionStyles.arrowSpan, ^.onClick := onClickLeft(self))(
                <.i(^.className := FontAwesomeStyles.angleLeft)()
              ),
              <.span(^.className := PoliticalActionStyles.arrowSpan, ^.onClick := onClickRight(self))(
                <.i(^.className := FontAwesomeStyles.angleRight)()
              )
            )
          )
        ),
        <.style()(PoliticalActionStyles.render[String])
    )
  )

  private def textBox(action: PoliticalAction, translationOffset: Int): ReactElement = {
    <.div(
      ^.className := PoliticalActionStyles.blocActionSlide,
      ^.style := Map("transform" -> s"translateX(${translationOffset}px)", "transition" -> "transform 500ms")
    )(
      <.img(^.className := Seq(PoliticalActionStyles.actionImage), ^.src := action.imageUrl)(),
      <.div()(
        <.div(^.className := PoliticalActionStyles.actionInfos)(
          <.div(^.className := Seq(BulmaStyles.Helpers.isPulledLeft, PoliticalActionStyles.actionDate))(
            <.span(^.className := Seq(AppComponentStyles.icon, BulmaStyles.Helpers.isSmall))(
              <.i(^.className := FontAwesomeStyles.calendarOpen)()
            ),
            <.span()(action.date)
          ),
          <.div(^.className := PoliticalActionStyles.actionLocation)(
            <.span(^.className := Seq(AppComponentStyles.icon, BulmaStyles.Helpers.isSmall))(
              <.i(^.className := FontAwesomeStyles.mapMarker)()
            ),
            <.span()(action.location)
          )
        ),
        <.div(^.className := PoliticalActionStyles.textBloc)(
          action.text,
          <.span(^.className := PoliticalActionStyles.pinkLink)(I18n.t("content.theme.moreInfos"))
        )
      )
    )
  }
}

object PoliticalActionStyles extends StyleSheet.Inline {
  import dsl._

  val boxWrapper: StyleA = style(
    backgroundColor(c"#ffffff"),
    fontSize(1.8.rem),
    boxShadow := "1px 1px 1px 0 rgba(0, 0, 0, 0.5)",
    (&.hover)(boxShadow := "1px 1px 25px 0 rgba(0, 0, 0, 0.3)")
  )

  val themeSliderActions: StyleA = style(position.relative, height(8.5.rem), clear.both)

  val blockActionMask: StyleA =
    style(position.static, display.block, overflow.hidden, height(8.5.rem), whiteSpace.nowrap)

  val blocActionSlide: StyleA = style(
    position.relative,
    display.inlineBlock,
    verticalAlign.top,
    height(100.%%),
    width(92.%%),
    marginRight(8.%%),
    zIndex(1),
    whiteSpace.normal
  )

  val actionImage: StyleA = style(margin.auto, float.left)

  val actionInfos: StyleA =
    style(color(rgba(0, 0, 0, 0.5)), fontSize(1.4.rem), MakeStyles.Font.circularStdBook, paddingTop(0.5.rem))

  val actionInfo: StyleA = style(float.left)

  val actionDate: StyleA = style(marginRight(1.rem))

  val actionLocation: StyleA = style()

  val textBloc: StyleA =
    style(lineHeight(2.4.rem), fontWeight._700, marginTop(0.5.rem), MakeStyles.Font.circularStdBold)

  val arrows: StyleA = style(
    borderLeft :=! "1px solid rgba(0, 0, 0, 0.1)",
    width(8.%%),
    position.absolute,
    top.`0`,
    right.`0`,
    height(100.%%),
    zIndex(4),
    backgroundColor(c"#ffffff")
  )

  val arrowSpan: StyleA = style(
    height(100.%%),
    width(50.%%),
    fontSize(5.4.rem),
    alignItems.center,
    display.inlineFlex,
    justifyContent.center,
    color(rgba(0, 0, 0, 0.1)),
    (&.hover)(color(c"#000000"))
  )

  val pinkLink: StyleA = style(color(MakeStyles.Color.pink))
}
