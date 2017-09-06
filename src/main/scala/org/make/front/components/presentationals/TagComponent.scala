package org.make.front.components.presentationals

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.React.Self
import io.github.shogowada.scalajs.reactjs.VirtualDOM.{<, _}
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.events.SyntheticEvent
import org.make.front.components.presentationals.TagComponent.TagComponentProps
import org.make.front.facades.I18n
import org.make.front.models.Tag
import org.make.front.styles.{BulmaStyles, MakeStyles}

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

          <.div()(
            tagList.map(
              tag =>
                <.TagComponent(
                  ^.wrapped := TagComponentProps(tag = tag, handleSelectedTags = self.props.wrapped.handleSelectedTags)
                )()
            ),
            // if toggle show more mode add show all more
            if (self.props.wrapped.withShowMoreButton) {
              <.div(
                ^.className := Seq(BulmaStyles.Element.tag, BulmaStyles.Syntax.isDanger, TagStyles.tagContainer),
                ^.onClick := onClickShowMore(self)
              )(<.span()(I18n.t("content.tag.showMore")))
            },
            <.style()(TagStyles.render[String])
          )
        }
      )

  /**
    * Toggle show all variable
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
  *   <code>
  *      <.TagComponent(^.wrapped := TagComponentProps(tag = Tag(TagId("tag-tag-name), "Tag Name")))()
  *   </code>
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
      <.div(
        ^.className := Seq(
          BulmaStyles.Element.tag,
          TagStyles.tagContainer,
          if (self.state.isSelected) BulmaStyles.Syntax.isBlack else TagStyles.defaultStyle
        ),
        ^.onClick := onClickTag(self)
      )(<.span()(self.props.wrapped.tag.label))
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

  val tagContainer: StyleA =
    style(
      position.relative,
      paddingLeft(1.2.rem),
      marginRight(1.6.rem),
      borderRadius(0.15.rem),
      fontSize(1.rem),
      fontWeight.bold,
      cursor.pointer,
      (&.before)(
        position.absolute,
        transform := "translateY(-50%) translateX(50%) rotate(-45deg)",
        top(50.%%),
        right(99.%%),
        content := "''",
        backgroundColor :=! "inherit",
        width(1.36.rem),
        height(1.36.rem)
      ),
      (&.after)(
        position.absolute,
        content := "''",
        top(50.%%),
        left(-0.20.rem),
        marginTop(-0.20.rem),
        backgroundColor :=! MakeStyles.Color.white,
        width(0.45.rem),
        height(0.45.rem),
        borderRadius(500.rem)
      )
    )
  val defaultStyle: StyleA = style(
    backgroundColor :=! MakeStyles.Color.lightGrey,
    color :=! MakeStyles.Color.white,
    (&.before)(backgroundColor :=! MakeStyles.Color.lightGrey)
  )
}
