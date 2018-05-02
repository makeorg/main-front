package org.make.front.components.tags

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.React.Self
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.events.SyntheticEvent
import org.make.front.Main.CssSettings._
import org.make.front.components.Components._
import org.make.front.components.tags.Tag.TagComponentProps
import org.make.front.facades.I18n
import org.make.front.facades.Unescape.unescape
import org.make.front.models.{Tag => TagModel}
import org.make.front.styles._
import org.make.front.styles.ui.TagStyles
import org.make.front.styles.utils._

import scala.scalajs.js

/**
  * Generates a list of TagComponents
  *
  * Example usage:
  *
  * - Creates a simple tag list
  *
  * <.TagListComponent(
  * ^.wrapped := TagListComponentProps(
  * tags = js.Array(Tag(TagId("tag-hello"), "hello"), Tag(TagId("tag-world"), "world")),
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
object TagsList {

  // TODO make variable dynamic / configurable
  private val showMaxCount: Int = 6

  case class TagsListComponentProps(tags: js.Array[TagModel],
                                    handleSelectedTags: (TagModel) => Unit,
                                    withShowMoreTagsButton: Boolean)

  case class TagsListComponentState(showMore: Boolean)

  lazy val reactClass: ReactClass =
    React
      .createClass[TagsListComponentProps, TagsListComponentState](
        displayName = "TagsList",
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

          <.ul(^.className := TagsListStyles.tagList)(
            tagsList
              .map(
                tag =>
                  <.li(^.className := TagsListStyles.tagListItem)(
                    <.TagComponent(
                      ^.wrapped := TagComponentProps(
                        tag = tag,
                        handleSelectedTags = self.props.wrapped.handleSelectedTags
                      )
                    )()
                )
              )
              .toSeq,
            if (self.props.wrapped.withShowMoreTagsButton) {
              <.li(^.className := TagsListStyles.tagListItem)(
                <.button(
                  ^.className := js.Array(TagStyles.basic, TagsListStyles.showMoreTags),
                  ^.onClick := onClickShowMoreTags(self)
                )(unescape(I18n.t(if (self.state.showMore) "tags.list.show-less" else "tags.list.show-all")))
              )
            },
            <.style()(TagsListStyles.render[String])
          )
        }
      )

  /**
    * Toggle show all variable
    *
    * @param self Self[TagsListComponentProps, State]
    */
  private def onClickShowMoreTags(self: Self[TagsListComponentProps, TagsListComponentState]) = (e: SyntheticEvent) => {
    e.preventDefault()
    self.setState(_.copy(showMore = !self.state.showMore))
  }
}

object TagsListStyles extends StyleSheet.Inline {

  import dsl._

  val tagList: StyleA =
    style(
      lineHeight(0),
      margin(
        ((ThemeStyles.SpacingValue.smaller / 2) * -1).pxToEm(),
        ((ThemeStyles.SpacingValue.smaller / 2) * -1).pxToEm(),
        `0`
      )
    )

  val tagListItem: StyleA =
    style(display.inlineBlock, verticalAlign.middle, margin((ThemeStyles.SpacingValue.smaller / 2).pxToEm()))

  val showMoreTags: StyleA = style(
    color(ThemeStyles.TextColor.white),
    backgroundColor(ThemeStyles.ThemeColor.primary),
    &.before(borderRightColor(ThemeStyles.ThemeColor.primary))
  )
}
