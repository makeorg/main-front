package org.make.front.components.showcase

import java.time.ZonedDateTime

import io.github.shogowada.scalajs.reactjs.React.Props
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.redux.ReactRedux
import io.github.shogowada.scalajs.reactjs.redux.Redux.Dispatch
import org.make.front.components.AppState
import org.make.front.models.{
  Author          => AuthorModel,
  Proposal        => ProposalModel,
  ProposalContext => ProposalContextModel,
  ProposalId      => ProposalIdModel,
  Qualification   => QualificationModel,
  Tag             => TagModel,
  TagId           => TagIdModel,
  ThemeId         => ThemeIdModel,
  UserId          => UserIdModel,
  Vote            => VoteModel
}

object ShowcaseContainer {

  final case class ShowcaseContainerProps(introTranslationKey: String)

  val proposals: Seq[ProposalModel] = {
    for (i <- 1 to 2)
      yield
        ProposalModel(
          id = ProposalIdModel("abcd"),
          userId = UserIdModel("asdf"),
          content = "proposal content fetched from API",
          slug = "proposal",
          status = "",
          createdAt = ZonedDateTime.now(),
          updatedAt = None,
          votesAgree = VoteModel(
            key = "agree",
            count = 2500,
            qualifications = Seq(
              QualificationModel(key = "likeIt", count = 952),
              QualificationModel(key = "doable", count = 97),
              QualificationModel(key = "platitudeAgree", count = 7)
            )
          ),
          votesDisagree = VoteModel(
            key = "disagree",
            count = 660,
            qualifications = Seq(
              QualificationModel(key = "noWay", count = 320),
              QualificationModel(key = "impossible", count = 53),
              QualificationModel(key = "platitudeDisagree", count = 9)
            )
          ),
          votesNeutral = VoteModel(
            key = "neutral",
            count = 170,
            qualifications = Seq(
              QualificationModel(key = "doNotUnderstand", count = 74),
              QualificationModel(key = "noOpinion", count = 12),
              QualificationModel(key = "doNotCare", count = 3)
            )
          ),
          proposalContext = ProposalContextModel(operation = None, source = None, location = None, question = None),
          trending = None,
          labels = Seq.empty,
          author = AuthorModel(firstname = Some("Marco"), postalCode = Some("75"), age = Some(42)),
          country = "FR",
          language = "fr",
          themeId = Some(ThemeIdModel(i.toString)),
          tags = Seq(
            TagModel(tagId = TagIdModel("1"), label = "un"),
            TagModel(tagId = TagIdModel("2"), label = "deux"),
            TagModel(tagId = TagIdModel("3"), label = "trois")
          )
        )
  }

  lazy val reactClass: ReactClass = ReactRedux.connectAdvanced(selectorFactory)(Showcase.reactClass)

  def selectorFactory: (Dispatch) => (AppState, Props[ShowcaseContainerProps]) => Showcase.ShowcaseProps =
    (_: Dispatch) => { (appState: AppState, ownProps: Props[ShowcaseContainerProps]) =>
      Showcase.ShowcaseProps(proposals = proposals, introTranslationKey = ownProps.wrapped.introTranslationKey)
    }

}
