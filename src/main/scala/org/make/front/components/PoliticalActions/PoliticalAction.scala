package org.make.front.components.PoliticalActions
import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import org.make.front.components.presentationals._
import org.make.front.facades.I18n
import org.make.front.models.PoliticalAction
import org.make.front.styles.{FontAwesomeStyles, ThemeStyles}

import scalacss.DevDefaults._

object PoliticalActionComponent {

  case class PoliticalActionProps(politicalAction: PoliticalAction)

  lazy val reactClass: ReactClass = React.createClass[PoliticalActionProps, Unit](render = (self) => {
    val politicalAction = self.props.wrapped.politicalAction

    <.article()(
      <.img(^.className := PoliticalActionStyles.image, ^.src := politicalAction.imageUrl)(),
      <.div(^.className := PoliticalActionStyles.infos)(
        <.p(^.className := PoliticalActionStyles.date)(
          <.i(^.className := FontAwesomeStyles.calendarOpen)(),
          politicalAction.date
        ),
        <.p(^.className := PoliticalActionStyles.location)(
          <.i(^.className := FontAwesomeStyles.mapMarker)(),
          politicalAction.location
        )
      ),
      <.p()(politicalAction.text, <.a(^.className := PoliticalActionStyles.seeMore)(I18n.t("content.theme.moreInfos"))),
      <.style()(PoliticalActionStyles.render[String])
    )

  })
}

object PoliticalActionStyles extends StyleSheet.Inline {
  import dsl._

  val image: StyleA = style()

  val infos: StyleA =
    style(color(ThemeStyles.TextColor.light))

  val info: StyleA = style(float.left)

  val date: StyleA = style(marginRight(1.rem))

  val location: StyleA = style()

  val seeMore: StyleA = style(color(ThemeStyles.ThemeColor.primary))
}
