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

object FilterByTags {

  type FilterByTagsSelf = Self[FilterByTagsProps, FilterByTagsState]

  case class FilterByTagsProps(tags: Seq[TagModel], onTagSelectionChange: Seq[TagModel] => Unit)

  case class FilterByTagsState(showAll: Boolean, selectedTags: Seq[TagModel])

  lazy val reactClass: ReactClass =
    React.createClass[FilterByTagsProps, FilterByTagsState](
      displayName = "FilterByTags",
      getInitialState = (_) => FilterByTagsState(showAll = false, selectedTags = Seq.empty),
      render = { self =>
        def handleSelectedTags(tag: TagModel): Unit = {
          val previouslySelectedTags = self.state.selectedTags
          val selectedTags = if (previouslySelectedTags.contains(tag)) {
            previouslySelectedTags.filterNot(_ == tag)
          } else {
            previouslySelectedTags ++ Seq(tag)
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
            <.p(^.className := Seq(FilterByTagsStyles.intro, TextStyles.smallText))(
              <.i(^.className := Seq(FilterByTagsStyles.illInIntro, FontAwesomeStyles.lineChart))(),
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
      margin(
        (ThemeStyles.SpacingValue.smaller / 2).pxToEm(),
        ThemeStyles.SpacingValue.smaller.pxToEm(),
        (ThemeStyles.SpacingValue.smaller / 2).pxToEm(),
        `0`
      )
    )

  val intro: StyleA =
    style(color(ThemeStyles.TextColor.light))

  val illInIntro: StyleA =
    style(verticalAlign.baseline, marginRight((ThemeStyles.SpacingValue.smaller / 2).pxToEm()))

}
