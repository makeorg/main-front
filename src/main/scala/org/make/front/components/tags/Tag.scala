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
import io.github.shogowada.scalajs.reactjs.events.SyntheticEvent
import org.make.front.components.Components._
import org.make.front.models.{Tag => TagModel}
import org.make.front.styles.ui.TagStyles

import scala.scalajs.js

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
object Tag {

  case class TagComponentProps(tag: TagModel, isSelected: Boolean, handleSelectedTags: (TagModel) => Unit)

  case class TagComponentState(isSelected: Boolean)

  lazy val reactClass: ReactClass = React.createClass[TagComponentProps, TagComponentState](
    displayName = "Tag",
    getInitialState = (self) => TagComponentState(isSelected = self.props.wrapped.isSelected),
    componentWillReceiveProps = { (self, nextProps) =>
      self.setState(
        TagComponentState(isSelected = nextProps.wrapped.isSelected)
      )
    },
    render = (self) => {
      val tagClasses =
        if (self.state.isSelected) js.Array(TagStyles.basic, TagStyles.activated) else js.Array(TagStyles.basic)
      <("tag")()(<.a(^.className := tagClasses, ^.onClick := onClickTag(self))(self.props.wrapped.tag.label))
    }
  )

  private def onClickTag(self: Self[TagComponentProps, TagComponentState]) = (e: SyntheticEvent) => {
    e.preventDefault()
    self.setState(_.copy(isSelected = !self.state.isSelected))
    self.props.wrapped.handleSelectedTags(self.props.wrapped.tag)
  }
}
