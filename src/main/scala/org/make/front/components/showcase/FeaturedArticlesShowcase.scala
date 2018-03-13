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
import org.make.front.styles.base.{ColRulesStyles, LayoutRulesStyles, RWDHideRulesStyles, TextStyles}
import org.make.front.styles.utils._

object FeaturedArticlesShowcase {

  final case class FeaturedArticlesShowcaseProps(articles: Seq[FeaturedArticleModel])

  lazy val reactClass: ReactClass =
    React
      .createClass[FeaturedArticlesShowcaseProps, Unit](
        displayName = "FeaturedArticlesShowcase",
        render = { self =>
          def articleTile(article: FeaturedArticleModel) = <.article()(
            <.div(^.className := FeaturedArticleTileStyles.illWrapper)(
              <.a(^.href := article.seeMoreLink, ^.className := TextStyles.boldText, ^.target := "_blank")(
                <.img(
                  ^.src := article.illUrl,
                  ^.srcset := article.illUrl + " 1x," + article.ill2xUrl + " 2x",
                  ^.alt := article.imageAlt.getOrElse("")
                )()
              )
            ),
            <.div(^.className := FeaturedArticleTileStyles.contentWrapper)(
              <.div(^.className := FeaturedArticleTileStyles.labelWrapper)(
                <.div(^.className := FeaturedArticleTileStyles.labelInnerWrapper)(
                  <.p(^.className := Seq(FeaturedArticleTileStyles.label, TextStyles.label))(article.label)
                )
              ),
              <.div(^.className := FeaturedArticleTileStyles.excerptWrapper)(
                <.p(^.className := TextStyles.mediumText)(
                  article.excerpt,
                  <.br()(),
                  <.a(^.href := article.seeMoreLink, ^.className := TextStyles.boldText, ^.target := "_blank")(
                    article.seeMoreLabel
                  )
                )
              )
            )
          )

          <.section(^.className := FeaturedArticlesShowcaseStyles.wrapper)(
            <.header(^.className := LayoutRulesStyles.centeredRow)(
              <.h2(^.className := TextStyles.mediumTitle)(unescape(I18n.t("home.featured-articles-showcase.title")))
            ),
            <.div(
              ^.className := Seq(
                RWDHideRulesStyles.hideBeyondMedium,
                LayoutRulesStyles.centeredRowWithCols,
                FeaturedArticlesShowcaseStyles.slideshow
              )
            )(
              <.Slider(^.infinite := false, ^.arrows := false)(
                self.props.wrapped.articles.map(
                  article =>
                    <.div(
                      ^.className :=
                        Seq(ColRulesStyles.col, FeaturedArticleTileStyles.wrapper)
                    )(articleTile(article))
                )
              )
            ),
            <.div(
              ^.className := Seq(
                RWDHideRulesStyles.showBlockBeyondMedium,
                RWDHideRulesStyles.hideBeyondLarge,
                LayoutRulesStyles.centeredRowWithCols,
                FeaturedArticlesShowcaseStyles.slideshow
              )
            )(
              <.Slider(^.infinite := false, ^.arrows := false, ^.slidesToShow := 2, ^.slidesToScroll := 2)(
                self.props.wrapped.articles.map(
                  article =>
                    <.div(
                      ^.className := Seq(
                        FeaturedArticleTileStyles.wrapper,
                        ColRulesStyles.col,
                        ColRulesStyles.colHalfBeyondMedium
                      )
                    )(articleTile(article))
                )
              )
            ),
            <.div(^.className := RWDHideRulesStyles.showBlockBeyondLarge)(
              <.ul(^.className := LayoutRulesStyles.centeredRowWithCols)(
                self.props.wrapped.articles.map(
                  article =>
                    <.li(
                      ^.className := Seq(
                        FeaturedArticleTileStyles.wrapper,
                        ColRulesStyles.col,
                        ColRulesStyles.colThirdBeyondLarge
                      )
                    )(articleTile(article))
                )
              )
            ),
            <.style()(FeaturedArticlesShowcaseStyles.render[String], FeaturedArticleTileStyles.render[String])
          )
        }
      )

}

object FeaturedArticlesShowcaseStyles extends StyleSheet.Inline {
  import dsl._

  val wrapper: StyleA =
    style(
      padding(ThemeStyles.SpacingValue.medium.pxToEm(), `0`),
      ThemeStyles.MediaQueries.beyondSmall(
        padding(
          ThemeStyles.SpacingValue.larger.pxToEm(),
          `0`,
          (ThemeStyles.SpacingValue.larger - ThemeStyles.SpacingValue.small).pxToEm()
        )
      ),
      overflow.hidden
    )

  val slideshow: StyleA =
    style(
      width(95.%%),
      unsafeChild(".slick-list")(overflow.visible),
      unsafeChild(".slick-slide")(height.auto, minHeight.inherit),
      unsafeChild(".slick-track")(display.flex)
    )
}

object FeaturedArticleTileStyles extends StyleSheet.Inline {

  import dsl._

  val wrapper: StyleA =
    style(
      marginTop(ThemeStyles.SpacingValue.small.pxToEm()),
      ThemeStyles.MediaQueries.beyondSmall(marginBottom(ThemeStyles.SpacingValue.small.pxToEm()))
    )

  val illWrapper: StyleA =
    style(
      position.relative,
      padding(((50 * 100) / 180).%%, 50.%%),
      backgroundColor(ThemeStyles.BackgroundColor.blackVeryTransparent),
      overflow.hidden,
      unsafeChild("img")(
        position.absolute,
        top(50.%%),
        left(50.%%),
        height.auto,
        maxHeight.none,
        minHeight(100.%%),
        width.auto,
        maxWidth.none,
        minWidth(100.%%),
        transform := s"translate(-50%, -50%)"
      )
    )

  val contentWrapper: StyleA =
    style(position.relative)

  val labelWrapper: StyleA =
    style(
      position.absolute,
      left(`0`),
      bottom(100.%%),
      width(100.%%),
      padding(`0`, (ThemeStyles.SpacingValue.largerMedium / 2).pxToEm()),
      overflow.hidden
    )

  val labelInnerWrapper: StyleA =
    style(
      position.relative,
      padding(`0`, (ThemeStyles.SpacingValue.largerMedium / 2).pxToEm()),
      textAlign.center,
      (&.before)(
        content := "''",
        position.absolute,
        left(`0`),
        bottom(`0`),
        display.block,
        height(50.%%),
        width(100.%%),
        backgroundColor(ThemeStyles.TextColor.white),
        boxShadow := "0 0px 4px 0 rgba(0,0,0,0.5)"
      )
    )
  val label: StyleA =
    style(position.relative)

  val excerptWrapper: StyleA =
    style(
      padding(ThemeStyles.SpacingValue.smaller.pxToEm(), ThemeStyles.SpacingValue.largerMedium.pxToEm(), `0`),
      unsafeChild("a")(color(ThemeStyles.ThemeColor.primary))
    )
}
