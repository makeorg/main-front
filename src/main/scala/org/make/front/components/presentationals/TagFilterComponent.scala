package org.make.front.components.presentationals

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.React.Self
import io.github.shogowada.scalajs.reactjs.VirtualDOM.{<, _}
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import org.make.front.components.AppComponentStyles
import org.make.front.components.presentationals.TagListComponent.TagListComponentProps
import org.make.front.facades.Translate.TranslateVirtualDOMElements
import org.make.front.models.Tag
import org.make.front.styles.{BulmaStyles, FontAwesomeStyles, MakeStyles}

import scalacss.DevDefaults._
import scalacss.internal.mutable.StyleSheet

object TagFilterComponent {

  type TagFilterSelf = Self[TagFilterProps, TagFilterState]

  case class TagFilterProps(tags: Seq[Tag], handleSelectedTags: Seq[Tag] => Unit)

  case class TagFilterState(showAll: Boolean, selectedTags: Seq[Tag])

  lazy val reactClass: ReactClass =
    React.createClass[TagFilterProps, TagFilterState](
      getInitialState = (_) => TagFilterState(showAll = false, selectedTags = Seq.empty),
      render = (self) => {
        def handleSelectedTags(tag: Tag): Unit = {
          val previouslySelectedTags = self.state.selectedTags
          val selectedTags = if (previouslySelectedTags.contains(tag)) {
            previouslySelectedTags.filterNot(_ == tag)
          } else {
            previouslySelectedTags ++ Seq(tag)
          }
          self.setState(_.copy(selectedTags = selectedTags))
          self.props.wrapped.handleSelectedTags(selectedTags)
        }

        val tagListProps = TagListComponentProps(
          tags = self.props.wrapped.tags,
          withShowMoreButton = self.props.wrapped.tags.size > 6,
          handleSelectedTags = handleSelectedTags
        )

        <.div(^.className := BulmaStyles.Element.isClipped)(
          <.div(^.className := BulmaStyles.Helpers.isPulledLeft)(
            <.span(^.className := Seq(AppComponentStyles.icon, BulmaStyles.Helpers.isSmall))(
              <.i(^.className := FontAwesomeStyles.lineChart)()
            ),
            <.Translate(^.value := "content.theme.matrix.filter.tag.title")()
          ),
          <.div(^.className := Seq(BulmaStyles.Helpers.isPulledLeft, TagFilterStyles.tagsList))(
            <.TagListComponent(^.wrapped := tagListProps)()
          ),
          <.style()(TagFilterStyles.render[String])
        )
      }
    )
}

object TagFilterStyles extends StyleSheet.Inline {

  import dsl._

  val tagsList: StyleA = style(marginLeft(2.rem))

  val intro: StyleA =
    style(
      float.left,
      margin :=! "0 1rem 0.5rem 0",
      MakeStyles.Font.circularStdBook,
      lineHeight(2.4.rem),
      color :=! MakeStyles.Color.darkGrey
    )

  val introIll: StyleA =
    style(verticalAlign.baseline, marginRight(0.5.rem))

}
