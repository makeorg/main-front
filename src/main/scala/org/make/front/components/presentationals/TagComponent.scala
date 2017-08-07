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
  *     ^.wrapped := TagListComponentProps(tags = Seq(Tag("hello"), Tag("world")), toggleShowAll = false)
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

  case class TagListComponentProps(tags: Seq[Tag], toggleShowAll: Boolean)

  case class State(showAll: Boolean)

  lazy val reactClass: ReactClass =
    React
      .createClass[TagListComponentProps, State](
        getInitialState = (_) => State(showAll = false),
        render = (self) => {
          // slice tagList if not showing all elements
          val tagList =
            if (self.state.showAll)
              self.props.wrapped.tags
            else self.props.wrapped.tags.take(showMaxCount)

          <.div()(
            tagList.map(
              tag =>
                <.TagComponent(
                  ^.wrapped := TagComponentProps(
                    text = tag.name,
                    triggerToggle = false,
                    toggleShowAllTags = toggleShowAllTags(self)
                  )
                )()
            ),
            // if toggle show all mode add show all button
            if (self.props.wrapped.toggleShowAll)
              <.TagComponent(
                ^.wrapped := TagComponentProps(
                  text = if (!self.state.showAll) I18n.t("content.tag.showMore") else I18n.t("content.tag.showLess"),
                  triggerToggle = true,
                  toggleShowAllTags = toggleShowAllTags(self)
                )
              )(),
            <.style()(TagStyles.render[String])
          )
        }
      )

  /**
    * Toggle show all variable
    * @param self Self[TagListComponentProps, State]
    */
  private def toggleShowAllTags(self: Self[TagListComponentProps, State]): () => Unit = () => {
    self.setState(_.copy(showAll = !self.state.showAll))
  }
}

/**
  * Tag Element Component
  *
  * Example usage:
  *
  * Create a simple tag:
  *   <code>
  *      <.TagComponent(
  *               ^.wrapped := TagComponentProps(
  *                 text = "Tag Name",
  *                 isTriggerToggle = false,
  *                 toggleShowAllTags = toggleShowAllTags(self)
  *               )
  *             )()
  *   </code>
  *
  * Set ifTriggerToggle as true if the tag show function as a toggle show more trigger
  *
  */
object TagComponent {

  case class TagComponentProps(text: String, triggerToggle: Boolean, toggleShowAllTags: () => Unit)

  lazy val reactClass: ReactClass = React.createClass[TagComponentProps, Unit](render = (self) => {
    val className =
      if (self.props.wrapped.triggerToggle)
        Seq(BulmaStyles.Element.tag, TagStyles.tagContainer, BulmaStyles.Syntax.isDanger)
      else
        Seq(BulmaStyles.Element.tag, TagStyles.tagContainer, TagStyles.defaultStyle)

    <.div(^.className := className, ^.onClick := onClickTag(self))(<.span()(self.props.wrapped.text))
  })

  private def onClickTag(self: Self[TagComponentProps, Unit]) = (e: SyntheticEvent) => {
    e.preventDefault()
    if (self.props.wrapped.triggerToggle) {
      self.props.wrapped.toggleShowAllTags()
    }
  }
}

object TagStyles extends StyleSheet.Inline {

  import dsl._

  val tagContainer: StyleA =
    style(
      position.relative,
      paddingLeft(1.2.rem),
      marginLeft(1.6.rem),
      marginBottom(0.5.rem),
      borderRadius(0.15.rem),
      fontSize(1.rem),
      fontWeight.bold,
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
    backgroundColor :=! MakeStyles.Color.darkGrey,
    color :=! MakeStyles.Color.white,
    (&.before)(backgroundColor := MakeStyles.Color.darkGrey)
  )
}
