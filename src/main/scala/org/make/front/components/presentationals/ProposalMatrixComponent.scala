package org.make.front.components.presentationals

import java.time.ZonedDateTime

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import org.make.front.components.AppComponentStyles
import org.make.front.facades.Translate.TranslateVirtualDOMElements
import org.make.front.models._
import org.make.front.styles.BulmaStyles

import scalacss.DevDefaults._
import scalacss.internal.mutable.StyleSheet

object ProposalMatrixComponent {

  val proposals: Seq[Proposal] = {
    for( i <- 1 to 20) yield Proposal(
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
  }

  lazy val reactClass: ReactClass = React.createClass[Unit, Unit](
    render = (self) =>
      <.div()(
        <.h2(^.className := AppComponentStyles.title2)(
          <.Translate(
            ^.value := "content.theme.matrix.title"
          )()
        ),
        <.TagFilterComponent()(),
        <.div(^.className := ProposalMatrixStyles.proposalList)(
          <.div(^.className := Seq(BulmaStyles.Grid.Columns.columns, BulmaStyles.Grid.Columns.columnsMultiLines))(
            proposals.map( proposal =>
              <.ProposalTileComponent(
                ^.wrapped := ProposalTileComponent.ProposalTileProps(
                  proposal = proposal
                )
              )()
            )
        )
      ),
      <.style()(ProposalMatrixStyles.render[String])
    )
  )
}

object ProposalMatrixStyles extends StyleSheet.Inline {
  import dsl._

  val proposalList: StyleA = style(
    marginTop(4.rem)
  )
}
