package org.make.front.components.Showcase

import java.time.ZonedDateTime

import io.github.shogowada.scalajs.reactjs.React.Props
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.redux.ReactRedux
import io.github.shogowada.scalajs.reactjs.redux.Redux.Dispatch
import org.make.front.models._

object ShowcaseContainerComponent {

  final case class ShowcaseContainerProps(introTranslationKey: String)

  val proposals: Seq[Proposal] = {
    for (i <- 1 to 2)
      yield
        Proposal(
          id = ProposalId("abcd"),
          userId = UserId("asdf"),
          content = "proposal content fetched from API",
          slug = "proposal",
          status = "",
          createdAt = ZonedDateTime.now(),
          updatedAt = None,
          voteAgree = Vote(
            key = "agree",
            count = 2500,
            qualifications = Seq(
              Qualification(key = "likeIt", count = 952),
              Qualification(key = "doable", count = 97),
              Qualification(key = "platitudeAgree", count = 7)
            )
          ),
          voteDisagree = Vote(
            key = "disagree",
            count = 660,
            qualifications = Seq(
              Qualification(key = "noWay", count = 320),
              Qualification(key = "impossible", count = 53),
              Qualification(key = "platitudeDisagree", count = 9)
            )
          ),
          voteNeutral = Vote(
            key = "neutral",
            count = 170,
            qualifications = Seq(
              Qualification(key = "doNotUnderstand", count = 74),
              Qualification(key = "noOpinion", count = 12),
              Qualification(key = "doNotCare", count = 3)
            )
          ),
          proposalContext = ProposalContext(operation = None, source = None, location = None, question = None),
          trending = None,
          labels = Seq.empty,
          author = Author(firstname = Some("Marco"), postalCode = Some("75"), age = Some(42)),
          country = "FR",
          language = "fr",
          themeId = Some(ThemeId(i.toString)),
          tags = Seq(
            Tag(tagId = TagId("1"), label = "un"),
            Tag(tagId = TagId("2"), label = "deux"),
            Tag(tagId = TagId("3"), label = "trois")
          )
        )
  }

  lazy val reactClass: ReactClass = ReactRedux.connectAdvanced(selectorFactory)(ShowcaseComponent.reactClass)

  def selectorFactory: (Dispatch) => (AppState, Props[ShowcaseContainerProps]) => ShowcaseComponent.ShowcaseProps =
    (_: Dispatch) => { (appState: AppState, ownProps: Props[ShowcaseContainerProps]) =>
      def searchThemeByThemeId(themeId: ThemeId): Option[Theme] = {
        appState.themes.find(_.id == themeId)
      }

      ShowcaseComponent.ShowcaseProps(
        proposals = proposals,
        introTranslationKey = ownProps.wrapped.introTranslationKey,
        searchThemeByThemeId = searchThemeByThemeId
      )
    }

}
