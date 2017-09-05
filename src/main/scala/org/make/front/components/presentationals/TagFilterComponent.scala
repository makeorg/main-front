package org.make.front.components.presentationals

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM.{<, _}
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.statictags.Element
import org.make.front.components.AppComponentStyles
import org.make.front.components.presentationals.TagListComponent.TagListComponentProps
import org.make.front.facades.Translate.TranslateVirtualDOMElements
import org.make.front.models.{Tag, TagId}
import org.make.front.styles.{BulmaStyles, FontAwesomeStyles}

import scalacss.DevDefaults._
import scalacss.internal.mutable.StyleSheet

/**
  * Generates the Tag filter
  *
  * Example usage:
  *
  * - Creates a tag filter
  *
  * <.TagListComponent(
  *    ^.wrapped := TagListComponentProps(
  *      tags = Seq(Tag(TagId("tag-hello"), "hello"), Tag(TagId("tag-world"), "world")),
  *      toggleShowAll = false
  *    )
  *  )()
  *
  */
object TagFilterComponent {

  case class TagFilterComponentProps(tags: Seq[Tag])
  case class TagFilterComponentState(showAll: Boolean, selectedTags: Seq[Tag])

  lazy val reactClass: ReactClass =
    React.createClass[TagFilterComponentProps, TagFilterComponentState](
      getInitialState = (_) => TagFilterComponentState(showAll = false, selectedTags = Seq.empty),
      render = (self) => {
        def handleSelectedTags(tag: Tag): Unit = {
          self.setState(
            (previousState: TagFilterComponentState) =>
              TagFilterComponentState(selectedTags = {
                if (previousState.selectedTags.contains(tag)) {
                  previousState.selectedTags.filter(_ != tag)
                } else {
                  previousState.selectedTags :+ tag
                }
              }, showAll = previousState.showAll)
          )
        }

        val tagList = TagListComponentProps(
          tags = Seq(
            Tag(TagId("tag-budget"), "budget"),
            Tag(TagId("tag-equipement"), "équipement"),
            Tag(TagId("tag-formation"), "formation"),
            Tag(TagId("tag-alimentation"), "Alimentation"),
            Tag(TagId("tag-bio"), "Bio"),
            Tag(TagId("tag-viande"), "viande"),
            Tag(TagId("tag-permaculture"), "Permaculture")
          ),
          withShowMoreButton = true,
          handleSelectedTags = handleSelectedTags
        )
        val selectedTags: Seq[Element] = self.state.selectedTags.map(tag => {
          <.li()(tag.label)
        })
        <.div(^.className := BulmaStyles.Element.isClipped)(
          <.ul()(selectedTags),
          <.div(^.className := BulmaStyles.Helpers.isPulledLeft)(
            <.span(^.className := Seq(AppComponentStyles.icon, BulmaStyles.Helpers.isSmall))(
              <.i(^.className := FontAwesomeStyles.lineChart)()
            ),
            <.Translate(^.value := "content.theme.matrix.filter.tag.title")()
          ),
          <.div(^.className := Seq(BulmaStyles.Helpers.isPulledLeft, TagFilterStyles.tagsList))(
            <.TagListComponent(^.wrapped := tagList)()
          ),
          <.style()(TagFilterStyles.render[String])
        )
      }
    )
}

object TagFilterStyles extends StyleSheet.Inline {
  import dsl._

  val tagsList: StyleA = style(marginLeft(2.rem))

}
