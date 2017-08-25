package org.make.front.components.presentationals

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM.{<, _}
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import org.make.front.components.AppComponentStyles
import org.make.front.components.presentationals.TagListComponent.TagListComponentProps
import org.make.front.facades.Translate.TranslateVirtualDOMElements
import org.make.front.models.Tag
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
  * <.TagFilterComponent(
  *     ^.wrapped := TagFilterComponentProps(tags = Seq(Tag("hello"), Tag("world")), toggleShowAll = false)
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
          def handleSelectedTags(tag: Tag) = {
            self.setState((previousState: TagFilterComponentState) => TagFilterComponentState(
              selectedTags = {
                if (previousState.selectedTags.contains(tag)) {
                  previousState.selectedTags.filter(_ != tag)
                } else {
                  previousState.selectedTags :+ tag
                }
              },
              showAll = previousState.showAll
            ))
          }

          <.div(^.className := BulmaStyles.Element.isClipped)(
            <.ul()(
              self.state.selectedTags.map(tag => {
                <.li()(tag.name)
              })
            ),
            <.div(^.className := BulmaStyles.Helpers.isPulledLeft)(
              <.span(^.className := Seq(AppComponentStyles.icon, BulmaStyles.Helpers.isSmall))(
                <.i(^.className := FontAwesomeStyles.lineChart)()
              ),
              <.Translate(
                ^.value := "content.theme.matrix.filter.tag.title"
              )()
            ),
            <.div(^.className := Seq(BulmaStyles.Helpers.isPulledLeft, TagFilterStyles.tagsList))(
              <.TagListComponent(
                ^.wrapped := TagListComponentProps(
                  tags = Seq(
                    Tag("budget"),
                    Tag("équipement"),
                    Tag("formation"),
                    Tag("Alimentation"),
                    Tag("Bio"),
                    Tag("viande"),
                    Tag("Permaculture")
                  ),
                  withShowMoreButton = true,
                  handleSelectedTags = handleSelectedTags
                )
              )()
            ),
            <.style()(TagFilterStyles.render[String])
          )
        }
      )
}


object TagFilterStyles extends StyleSheet.Inline {
  import dsl._

  val tagsList: StyleA = style(
    marginLeft(2.rem)
  )

}