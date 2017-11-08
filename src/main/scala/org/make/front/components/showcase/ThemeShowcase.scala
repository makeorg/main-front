package org.make.front.components.showcase

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM.{<, _}
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.router.dom.RouterDOM._
import org.make.core.Counter
import org.make.front.components.Components.{RichVirtualDOMElements, _}
import org.make.front.components.proposal.ProposalTile.ProposalTileProps
import org.make.front.facades.{HexToRgba, I18n, Replacements}
import org.make.front.models.{
  GradientColor   => GradientColorModel,
  Proposal        => ProposalModel,
  TranslatedTheme => TranslatedThemeModel
}
import org.make.front.styles._
import org.make.front.styles.base.{ColRulesStyles, RowRulesStyles, TextStyles}
import org.make.front.styles.ui.CTAStyles
import org.make.front.styles.utils._
import org.make.services.proposal.SearchResult

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.{Failure, Success}
import org.make.front.Main.CssSettings._

object ThemeShowcase {

  final case class ThemeShowcaseProps(proposals: Future[SearchResult],
                                      maybeTheme: Option[TranslatedThemeModel],
                                      maybeIntro: Option[String],
                                      maybeNews: Option[String])

  final case class ThemeShowcaseState(proposals: Seq[ProposalModel])

  lazy val reactClass: ReactClass =
    React.createClass[ThemeShowcaseProps, ThemeShowcaseState](displayName = "Showcase", getInitialState = { _ =>
      ThemeShowcaseState(Seq.empty)
    }, render = {
      self =>
        self.props.wrapped.proposals.onComplete {
          case Failure(_)       =>
          case Success(results) => self.setState(_.copy(proposals = results.results))
        }

        val gradientValues: GradientColorModel =
          self.props.wrapped.maybeTheme.flatMap(_.gradient).getOrElse(GradientColorModel("#FFF", "#FFF"))
        val index = self.props.wrapped.maybeTheme.map(_.order).getOrElse(9999)

        object DynamicThemeShowcaseStyles extends StyleSheet.Inline {
          import dsl._

          val gradient: (Int) => StyleA =
            styleF.int(Range(index, index + 1)) { index =>
              styleS(
                background := s"linear-gradient(130deg, ${HexToRgba(gradientValues.from, 0.1F)}, ${HexToRgba(gradientValues.to, 0.1F)})"
              )
            }
        }

        val counter = Counter.showcaseCounter

        if (self.state.proposals.nonEmpty) {
          <.section(^.className := Seq(ThemeShowcaseStyles.wrapper, DynamicThemeShowcaseStyles.gradient(index)))(
            Seq(
              <.div(^.className := RowRulesStyles.centeredRow)(
                <.header(^.className := ColRulesStyles.col)(if (self.props.wrapped.maybeIntro.nonEmpty) {
                  <.p(^.className := Seq(ThemeShowcaseStyles.intro, TextStyles.mediumText, TextStyles.intro))(
                    self.props.wrapped.maybeIntro
                  )
                }, <.h2(^.className := Seq(if (self.props.wrapped.maybeNews.nonEmpty) {
                  ThemeShowcaseStyles.titleBeforeNews
                } else { ThemeShowcaseStyles.title }, TextStyles.bigTitle))(self.props.wrapped.maybeTheme.map(_.title).getOrElse("")), if (self.props.wrapped.maybeNews.nonEmpty) {
                  <.p(
                    ^.className := Seq(ThemeShowcaseStyles.news, TextStyles.smallerText),
                    ^.dangerouslySetInnerHTML := self.props.wrapped.maybeNews.getOrElse("")
                  )()
                }),
                <.ul(^.className := ThemeShowcaseStyles.propasalsList)(
                  self.state.proposals.map(
                    proposal =>
                      <.li(
                        ^.className := Seq(
                          ThemeShowcaseStyles.propasalItem,
                          ColRulesStyles.col,
                          ColRulesStyles.colHalfBeyondMedium,
                          ColRulesStyles.colQuarterBeyondLarge
                        )
                      )(
                        <.ProposalTileComponent(
                          ^.wrapped :=
                            ProposalTileProps(proposal = proposal, index = counter.getAndIncrement())
                        )()
                    )
                  )
                ),
                if (self.props.wrapped.maybeTheme.nonEmpty) {
                  <.p(^.className := Seq(ColRulesStyles.col, ThemeShowcaseStyles.SeeMoreLinkWrapper))(
                    <.Link(
                      ^.className := Seq(CTAStyles.basic, CTAStyles.basicOnA),
                      ^.to := s"/theme/${self.props.wrapped.maybeTheme.map(_.slug).getOrElse("")}"
                    )(
                      I18n
                        .t(
                          "theme-showcase.see-all",
                          Replacements(
                            (
                              "themeName",
                              self.props.wrapped.maybeTheme
                                .map(_.title)
                                .getOrElse("")
                            )
                          )
                        )
                    )
                  )
                }
              ),
              <.style()(ThemeShowcaseStyles.render[String], DynamicThemeShowcaseStyles.render[String])
            )
          )
        } else <.div.empty
    })
}

object ThemeShowcaseStyles extends StyleSheet.Inline {
  import dsl._

  val wrapper: StyleA =
    style(
      backgroundColor(ThemeStyles.BackgroundColor.blackVeryTransparent),
      padding :=! s"${ThemeStyles.SpacingValue.medium.pxToEm().value} 0",
      ThemeStyles.MediaQueries.beyondSmall(
        padding :=! s"${ThemeStyles.SpacingValue.larger.pxToEm().value} 0 ${(ThemeStyles.SpacingValue.larger - ThemeStyles.SpacingValue.small).pxToEm().value}"
      )
    )

  val intro: StyleA = style(
    marginBottom(ThemeStyles.SpacingValue.smaller.pxToEm(15)),
    ThemeStyles.MediaQueries.beyondSmall(marginBottom(ThemeStyles.SpacingValue.smaller.pxToEm(18)))
  )

  val title: StyleA = style(lineHeight(1))

  val titleBeforeNews: StyleA = style(
    display.inlineBlock,
    float.left,
    marginRight(ThemeStyles.SpacingValue.small.pxToEm(20)),
    ThemeStyles.MediaQueries.beyondSmall(marginRight(ThemeStyles.SpacingValue.small.pxToEm(34))),
    ThemeStyles.MediaQueries.beyondMedium(marginRight(ThemeStyles.SpacingValue.small.pxToEm(46))),
    lineHeight(1)
  )

  val news: StyleA =
    style(
      display.inlineBlock,
      float.left,
      padding :=! s"${2.pxToEm(13).value} ${(ThemeStyles.SpacingValue.small / 2).pxToEm(13).value}",
      lineHeight(15.pxToEm(13)),
      ThemeStyles.MediaQueries.beyondSmall(
        padding :=! s"${2.pxToEm(14).value} ${(ThemeStyles.SpacingValue.small / 2).pxToEm(14).value}",
        lineHeight(15.pxToEm(14))
      ),
      color(ThemeStyles.TextColor.white),
      backgroundColor(ThemeStyles.BackgroundColor.black)
    )

  val propasalsList: StyleA =
    style(display.flex, flexWrap.wrap)

  val propasalItem: StyleA =
    style(
      marginTop(ThemeStyles.SpacingValue.small.pxToEm()),
      ThemeStyles.MediaQueries.beyondSmall(marginBottom(ThemeStyles.SpacingValue.small.pxToEm()))
    )

  val SeeMoreLinkWrapper: StyleA = style(
    marginTop(ThemeStyles.SpacingValue.small.pxToEm()),
    marginBottom(ThemeStyles.SpacingValue.small.pxToEm()),
    textAlign.center
  )
}
