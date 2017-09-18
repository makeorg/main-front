package org.make.front.components.Tags

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.React.Self
import io.github.shogowada.scalajs.reactjs.VirtualDOM.{<, _}
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.events.SyntheticEvent
import org.make.front.components.presentationals._
import org.make.front.models.Tag
import org.make.front.styles.TagStyles

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
      val tagClasses = if (self.state.isSelected) Seq(TagStyles.basic, TagStyles.activated) else Seq(TagStyles.basic)
      <("tag")()(<.a(^.className := tagClasses, ^.onClick := onClickTag(self))(self.props.wrapped.tag.label))
    }
  )

  private def onClickTag(self: Self[TagComponentProps, TagComponentState]) = (e: SyntheticEvent) => {
    e.preventDefault()
    self.setState(_.copy(isSelected = !self.state.isSelected))
    self.props.wrapped.handleSelectedTags(self.props.wrapped.tag)
  }
}
