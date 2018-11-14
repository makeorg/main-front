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

package org.make.front.components.showcase

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM.{<, _}
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import org.make.front.Main.CssSettings._
import org.make.front.components.Components._
import org.make.front.facades.I18n
import org.make.front.facades.ReactSlick.{ReactTooltipVirtualDOMAttributes, ReactTooltipVirtualDOMElements}
import org.make.front.facades.Unescape.unescape
import org.make.front.models.{FeaturedArticle => FeaturedArticleModel}
import org.make.front.styles.ThemeStyles
import org.make.front.styles.base._
import org.make.front.styles.utils._

import scala.scalajs.js

object FeaturedArticlesShowcase {

  final case class FeaturedArticlesShowcaseProps(articles: js.Array[FeaturedArticleModel])

  lazy val reactClass: ReactClass =
    React
      .createClass[FeaturedArticlesShowcaseProps, Unit](
        displayName = "FeaturedArticlesShowcase",
        render = { self =>
          def articleTile(article: FeaturedArticleModel) =
            <.article(^.className := FeaturedArticleTileStyles.operation)(
              <.a(^.href := article.seeMoreLink, ^.className := TextStyles.boldText)(
                <.img(
                  ^.className := FeaturedArticleTileStyles.operationPicture,
                  ^.src := article.illUrl,
                  ^.alt := article.imageAlt.getOrElse("")
                )()
              ),
              <.h2(^.className := js.Array(FeaturedArticleTileStyles.label, TextStyles.verySmallTitle))(article.label),
              <.div(^.className := FeaturedArticleTileStyles.excerptWrapper)(
                <.p(^.className := TextStyles.mediumText)(
                  article.excerpt,
                  <.br()(),
                  <.a(^.href := article.seeMoreLink, ^.className := TextStyles.boldText)(article.seeMoreLabel)
                )
              )
            )

          <.section(^.className := js.Array(LayoutRulesStyles.centeredRow, FeaturedArticleTileStyles.wrapper))(
            self.props.wrapped.articles
              .map(article => articleTile(article))
              .toSeq,
            <.style()(FeaturedArticleTileStyles.render[String])
          )
        }
      )

}

object FeaturedArticleTileStyles extends StyleSheet.Inline {

  import dsl._

  val specialPadding = ThemeStyles.SpacingValue.small / 2

  val specialTabletPadding = ThemeStyles.SpacingValue.small + specialPadding

  val wrapper: StyleA =
    style(
      padding(ThemeStyles.SpacingValue.small.pxToEm()),
      ThemeStyles.MediaQueries
        .beyondSmall(padding(ThemeStyles.SpacingValue.small.pxToEm(), specialTabletPadding.pxToEm())),
      ThemeStyles.MediaQueries
        .beyondMedium(
          padding(ThemeStyles.SpacingValue.medium.pxToEm(), ThemeStyles.SpacingValue.small.pxToEm()),
          display.flex,
          justifyContent.spaceBetween
        )
    )

  val operation: StyleA =
    style(
      display.inlineBlock,
      width(100.%%),
      padding(`0`),
      marginBottom(ThemeStyles.SpacingValue.small.pxToEm()),
      ThemeStyles.MediaQueries.beyondSmall(width(50.%%), padding(`0`, specialPadding.pxToEm())),
      ThemeStyles.MediaQueries.beyondMedium(width(100.%%), padding(`0`, ThemeStyles.SpacingValue.small.pxToEm()))
    )

  val operationPicture: StyleA =
    style(width(100.%%))

  val label: StyleA =
    style(marginTop(ThemeStyles.SpacingValue.small.pxToEm(13)))

  val excerptWrapper: StyleA =
    style(unsafeChild("a")(color(ThemeStyles.ThemeColor.primary)))
}
