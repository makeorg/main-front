package org.make.front.components.presentationals

import java.time.ZonedDateTime

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import org.make.front.models._
import org.make.front.styles.BulmaStyles

object HomeComponent {
  lazy val reactClass: ReactClass =
    React.createClass[Unit, Unit](
      render = (_) =>
        <.div()(
          <.HomeHeaderComponent.empty,
          <.div(^.className := BulmaStyles.Layout.container)(
            <.div(
              ^.className := BulmaStyles.Grid.Columns.columns,
              ^.style := Map("margin-top" -> "0", "margin-bottom" -> "0")
            )(
              <.ProposalTileComponent(
                ^.wrapped := ProposalTileComponent.ProposalTileProps(
                  proposal = Proposal(
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
                    themeId = Some(ThemeId("1")),
                    tags = Seq(Tag(name = "un"), Tag(name = "deux"), Tag(name = "trois"))
                  )
                )
              )()
            )
          )
      )
    )
}
