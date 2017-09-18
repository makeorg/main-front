package org.make.front.components.Tags

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.React.Self
import io.github.shogowada.scalajs.reactjs.VirtualDOM.{<, _}
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import org.make.front.components.Tags.TagsListComponent.TagsListComponentProps
import org.make.front.components.presentationals._
import org.make.front.facades.I18n
import org.make.front.facades.Unescape.unescape
import org.make.front.models.Tag
import org.make.front.styles.{FontAwesomeStyles, TextStyles, ThemeStyles}

import scalacss.DevDefaults._
import scalacss.internal.Length
import scalacss.internal.mutable.StyleSheet

object FilterByTagsComponent {

  type FilterByTagsSelf = Self[FilterByTagsProps, FilterByTagsState]

  case class FilterByTagsProps(tags: Seq[Tag], handleSelectedTags: Seq[Tag] => Unit)

  case class FilterByTagsState(showAll: Boolean, selectedTags: Seq[Tag])

  lazy val reactClass: ReactClass =
    React.createClass[FilterByTagsProps, FilterByTagsState](
      getInitialState = (_) => FilterByTagsState(showAll = false, selectedTags = Seq.empty),
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

        val tagsListProps = TagsListComponentProps(
          tags = self.props.wrapped.tags,
          withShowMoreTagsButton = self.props.wrapped.tags.size > 6,
          handleSelectedTags = handleSelectedTags
        )

        <.div()(
          <.p(^.className := Seq(FilterByTagsStyles.intro))(
            <.i(^.className := Seq(FilterByTagsStyles.illInIntro, FontAwesomeStyles.lineChart))(),
            unescape(I18n.t("content.theme.matrix.filter.tag.title"))
          ),
          <.nav(^.className := FilterByTagsStyles.tagsList)(<.TagsListComponent(^.wrapped := tagsListProps)()),
          <.style()(FilterByTagsStyles.render[String])
        )
      }
    )
}

object FilterByTagsStyles extends StyleSheet.Inline {

  import dsl._

  //TODO: globalize function
  implicit class NormalizedSize(val baseSize: Int) extends AnyVal {
    def pxToEm(browserContextSize: Int = 16): Length[Double] = {
      (baseSize.toFloat / browserContextSize.toFloat).em
    }
  }

  val tagsList: StyleA = style()

  val intro: StyleA =
    style(
      marginBottom(5.pxToEm(15)),
      ThemeStyles.Font.circularStdBook,
      fontSize(15.pxToEm()),
      color :=! ThemeStyles.TextColor.light,
      ThemeStyles.MediaQueries.beyondSmall(
        float.left,
        marginTop(5.pxToEm()),
        marginRight(10.pxToEm()),
        marginBottom(`0`),
        fontSize(16.pxToEm()),
        lineHeight(24.pxToEm())
      )
    )

  val illInIntro: StyleA =
    style(verticalAlign.baseline, marginRight(5.pxToEm()))

}
