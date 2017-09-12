package org.make.front.components.Tags

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.React.Self
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.events.SyntheticEvent
import org.make.front.components.Tags.TagComponent.TagComponentProps
import org.make.front.components.presentationals._
import org.make.front.facades.I18n
import org.make.front.models.Tag

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
  * ^.wrapped := TagListComponentProps(
  * tags = Seq(Tag(TagId("tag-hello"), "hello"), Tag(TagId("tag-world"), "world")),
  * toggleShowAll = false
  * )
  * )()
  *
  * - To create show all toggle-able tag list simply set toggleShowAll = true
  *
  * If you set the toggleShowAll option as true,
  * the list will only show 6 tags and add a show all button at the end
  * to toggle showing all the tags
  *
  */
object TagsListComponent {

  // TODO make variable dynamic / configurable
  private val showMaxCount: Int = 6

  case class TagsListComponentProps(tags: Seq[Tag], handleSelectedTags: (Tag) => Unit, withShowMoreButton: Boolean)

  case class TagsListComponentState(showMore: Boolean)

  lazy val reactClass: ReactClass =
    React
      .createClass[TagsListComponentProps, TagsListComponentState](
        getInitialState = { _ =>
          TagsListComponentState(showMore = false)
        },
        render = { self =>
          // slice tagList if not showing all elements

          val tagsList =
            if (self.state.showMore) {
              self.props.wrapped.tags
            } else {
              self.props.wrapped.tags.take(showMaxCount)
            }

          <.div()(
            <.ul()(
              tagsList.map(
                tag =>
                  <.li()(
                    <.TagComponent(
                      ^.wrapped := TagComponentProps(
                        tag = tag,
                        handleSelectedTags = self.props.wrapped.handleSelectedTags
                      )
                    )()
                )
              )
            ),
            if (self.props.wrapped.withShowMoreButton) {
              <.button(^.onClick := onClickShowMore(self))(I18n.t("content.tag.showMore"))
            },
            <.style()(TagStyles.render[String])
          )
        }
      )

  /**
    * Toggle show all variable
    *
    * @param self Self[TagsListComponentProps, State]
    */
  private def onClickShowMore(self: Self[TagsListComponentProps, TagsListComponentState]) = (e: SyntheticEvent) => {
    e.preventDefault()
    self.setState(_.copy(showMore = !self.state.showMore))
  }
}

object TagsListStyles extends StyleSheet.Inline {

  import dsl._

  val tagList: StyleA =
    style(lineHeight(0))

  val tagListItem: StyleA =
    style(display.inlineBlock, margin :=! "0rem 1rem 0.5rem 0")
}
