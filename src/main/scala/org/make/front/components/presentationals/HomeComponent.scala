package org.make.front.components.presentationals

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import org.make.front.models.{Proposal, Tag}
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
                    content = "proposal content fetched from API",
                    authorFirstname = "Marco",
                    authorPostalCode = Some("75"),
                    authorAge = Some(42),
                    nbVoteAgree = 84,
                    nbVoteDisagree = 42,
                    nbVoteNeutral = 21,
                    tags = Seq(Tag(name = "un"), Tag(name = "deux"), Tag(name = "trois"))
                  )
                )
              )()
            )
          )
      )
    )
}
