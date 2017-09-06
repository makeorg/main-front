package org.make.front.components.presentationals

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.React.Self
import io.github.shogowada.scalajs.reactjs.VirtualDOM.{<, _}
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.events.SyntheticEvent
import org.make.front.components.presentationals.TagComponent.TagComponentProps
import org.make.front.facades.I18n
import org.make.front.models.Tag
import org.make.front.styles.{MakeStyles}

import scalacss.DevDefaults._
import scalacss.internal.mutable.StyleSheet

/**
  * Generates a list of TagComponents
  *
  * Example usage:
  *
  * - Creates a simple tag list
  *
  * <.TagListComponent(
  *    ^.wrapped := TagListComponentProps(
  *      tags = Seq(Tag(TagId("tag-hello"), "hello"), Tag(TagId("tag-world"), "world")),
  *      toggleShowAll = false
  *    )
  *  )()
  *
  * - To create show all toggle-able tag list simply set toggleShowAll = true
  *
  *  If you set the toggleShowAll option as true,
  *  the list will only show 6 tags and add a show all button at the end
  *  to toggle showing all the tags
  *
  */
object TagListComponent {

  // TODO make variable dynamic / configurable
  private val showMaxCount: Int = 6

  case class TagListComponentProps(tags: Seq[Tag], handleSelectedTags: (Tag) => Unit, withShowMoreButton: Boolean)
  case class TagListComponentState(showMore: Boolean)

  lazy val reactClass: ReactClass =
    React
      .createClass[TagListComponentProps, TagListComponentState](
        getInitialState = (_) => TagListComponentState(showMore = false),
        render = (self) => {
          // slice tagList if not showing all elements
          val tagList =
            if (self.state.showMore) {
              self.props.wrapped.tags
            } else {
              self.props.wrapped.tags.take(showMaxCount)
            }

          <.ul(^.className := Seq(TagStyles.tagList))(
            tagList.map(
              tag =>
                <.li(^.className := Seq(TagStyles.tagListItem))(
                  <.TagComponent(
                    ^.wrapped := TagComponentProps(
                      tag = tag,
                      handleSelectedTags = self.props.wrapped.handleSelectedTags
                    )
                  )()
              )
            ),
            // if toggle show more mode add show all more
            if (self.props.wrapped.withShowMoreButton) {
              <.li(^.className := TagStyles.tagListItem)(
                <.button(^.className := Seq(TagStyles.tag, TagStyles.seeAllTags), ^.onClick := onClickShowMore(self))(
                  I18n.t("content.tag.showMore")
                )
              )
            },
            <.style()(TagStyles.render[String])
          )
        }
      )

  /**
    * Toggle show all variable
    *
    * @param self Self[TagListComponentProps, State]
    */
  private def onClickShowMore(self: Self[TagListComponentProps, TagListComponentState]) = (e: SyntheticEvent) => {
    e.preventDefault()
    self.setState(_.copy(showMore = !self.state.showMore))
  }
}

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
      <.a(^.className := styles, ^.onClick := onClickTag(self))(self.props.wrapped.tag.label)
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

  val tagList: StyleA =
    style(lineHeight(0))

  val tagListItem: StyleA =
    style(display.inlineBlock, margin :=! "0rem 1rem 0.5rem 0")

  val tag: StyleA =
    style(
      position.relative,
      display.block,
      paddingLeft(1.2.rem),
      paddingRight(1.rem),
      marginLeft(0.8.rem),
      MakeStyles.TextStyles.smallerText,
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
