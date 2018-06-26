/*
 *
 * Make.org Main Front
 * Copyright (C) 2018 Make.org
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 *
 */

package org.make.front.components.tags

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.React.Self
import io.github.shogowada.scalajs.reactjs.VirtualDOM.{<, _}
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import org.make.front.Main.CssSettings._
import org.make.front.components.Components._
import org.make.front.components.tags.TagsList.TagsListComponentProps
import org.make.front.facades.I18n
import org.make.front.facades.Unescape.unescape
import org.make.front.models.{Tag => TagModel}
import org.make.front.styles._
import org.make.front.styles.base.TextStyles
import org.make.front.styles.utils._
import org.make.front.styles.vendors.FontAwesomeStyles

import scala.scalajs.js

object FilterByTags {

  type FilterByTagsSelf = Self[FilterByTagsProps, FilterByTagsState]

  case class FilterByTagsProps(tags: js.Array[TagModel], onTagSelectionChange: js.Array[TagModel] => Unit)

  case class FilterByTagsState(showAll: Boolean, selectedTags: js.Array[TagModel])

  lazy val reactClass: ReactClass =
    React.createClass[FilterByTagsProps, FilterByTagsState](
      displayName = "FilterByTags",
      getInitialState = (_) => FilterByTagsState(showAll = false, selectedTags = js.Array()),
      render = { self =>
        def handleSelectedTags(tag: TagModel): Unit = {
          val previouslySelectedTags = self.state.selectedTags
          val selectedTags = if (previouslySelectedTags.contains(tag)) {
            previouslySelectedTags.filterNot(_ == tag)
          } else {
            previouslySelectedTags ++ js.Array(tag)
          }
          self.setState(_.copy(selectedTags = selectedTags))
          self.props.wrapped.onTagSelectionChange(selectedTags)
        }

        val tagsListProps = TagsListComponentProps(
          tags = self.props.wrapped.tags,
          withShowMoreTagsButton = self.props.wrapped.tags.size > 6,
          handleSelectedTags = handleSelectedTags
        )

        <.nav(^.className := FilterByTagsStyles.wrapper)(
          <.div(^.className := FilterByTagsStyles.introWrapper)(
            <.p(^.className := js.Array(FilterByTagsStyles.intro, TextStyles.smallText))(
              unescape(I18n.t("tags.filter.intro"))
            )
          ),
          <.TagsListComponent(^.wrapped := tagsListProps)(),
          <.style()(FilterByTagsStyles.render[String])
        )
      }
    )
}

object FilterByTagsStyles extends StyleSheet.Inline {

  import dsl._

  val wrapper: StyleA =
    style(margin(((ThemeStyles.SpacingValue.smaller / 2) * -1).pxToEm(), `0`))

  val introWrapper: StyleA =
    style(
      float.left,
      margin((ThemeStyles.SpacingValue.smaller / 2).pxToEm(), ThemeStyles.SpacingValue.smaller.pxToEm(), `0`, `0`)
    )

  val intro: StyleA =
    style(color(ThemeStyles.TextColor.light))

}
