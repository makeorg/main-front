package org.make.front.components.Tags

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.React.Self
import io.github.shogowada.scalajs.reactjs.VirtualDOM.{<, _}
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.events.SyntheticEvent

import org.make.front.components.presentationals._

import org.make.front.models.Tag
import org.make.front.styles.MakeStyles

import scalacss.DevDefaults._
import scalacss.internal.mutable.StyleSheet

/**
  * Tag Element Component
  *
  * Example usage:
  *
  * Create a simple tag:
  * <code>
  * <.TagComponent(^.wrapped := TagComponentProps(tag = Tag(TagId("tag-tag-name), "Tag Name")))()
  * </code>
  *
  * Set ifTriggerToggle as true if the tag show function as a toggle show more trigger
  *
  */
object TagComponent {

  case class TagComponentProps(tag: Tag, handleSelectedTags: (Tag) => Unit)

  case class TagComponentState(isSelected: Boolean)

  lazy val reactClass: ReactClass = React.createClass[TagComponentProps, TagComponentState](
    getInitialState = (_) => TagComponentState(isSelected = false),
    render = (self) => {
      val styles = if (self.state.isSelected) Seq(TagStyles.active, TagStyles.tag) else Seq(TagStyles.tag)
      <("tag")()(
        <.a(^.className := styles, ^.onClick := onClickTag(self))(self.props.wrapped.tag.label),
        <.style()(TagStyles.render[String])
      )
    }
  )

  private def onClickTag(self: Self[TagComponentProps, TagComponentState]) = (e: SyntheticEvent) => {
    e.preventDefault()
    self.setState(_.copy(isSelected = !self.state.isSelected))
    self.props.wrapped.handleSelectedTags(self.props.wrapped.tag)
  }
}

object TagStyles extends StyleSheet.Inline {

  import dsl._

  val tag: StyleA =
    style(
      position.relative,
      display.block,
      paddingLeft(1.2.rem),
      paddingRight(1.rem),
      marginLeft(0.8.rem),
      lineHeight(2.4.rem),
      whiteSpace.nowrap,
      color :=! MakeStyles.Color.white,
      backgroundColor :=! MakeStyles.Color.lightGrey,
      (&.before)(
        content := "''",
        position.absolute,
        top(0.%%),
        right(100.%%),
        width(0.rem),
        height(0.rem),
        borderTop :=! "1.2rem solid transparent",
        borderBottom :=! "1.2rem solid transparent",
        borderRight :=! s"0.8rem solid ${MakeStyles.Color.lightGrey.value}"
      ),
      (&.after)(
        content := "''",
        position.absolute,
        top(50.%%),
        left(0.%%),
        width(0.6.rem),
        height(0.6.rem),
        marginTop(-0.3.rem),
        borderRadius(50.%%),
        backgroundColor :=! MakeStyles.Color.white
      )
    )

  val active: StyleA = style(
    color :=! MakeStyles.Color.white,
    backgroundColor :=! MakeStyles.Color.black,
    (&.before)(borderRightColor :=! MakeStyles.Color.black)
  )

  val seeAllTags: StyleA = style(
    color :=! MakeStyles.Color.white,
    backgroundColor :=! MakeStyles.Color.pink,
    (&.before)(borderRightColor :=! MakeStyles.Color.pink)
  )
}
