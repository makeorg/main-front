package org.make.front.components.presentationals

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.React.Self
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.elements.ReactElement
import io.github.shogowada.statictags.Element
import org.make.front.components.presentationals.TagFilterComponent.{TagFilterProps, TagFilterSelf}
import org.make.front.facades.Translate.{TranslateVirtualDOMAttributes, TranslateVirtualDOMElements}
import org.make.front.models._
import org.make.front.styles.{BulmaStyles, MakeStyles}

import scalacss.DevDefaults._
import scalacss.internal.mutable.StyleSheet

object ProposalMatrixComponent {

  type ProposalMatrixSelf = Self[ProposalMatrixProps, ProposalMatrixState]

  case class ProposalMatrixProps(handleSelectedTags: ProposalMatrixSelf => Seq[Tag] => Unit, tags: Seq[Tag])
  case class ProposalMatrixState(listProposals: Seq[Proposal], selectedTags: Seq[Tag])

  lazy val reactClass: ReactClass = React.createClass[ProposalMatrixProps, ProposalMatrixState](
    getInitialState = (_) => ProposalMatrixState(Seq.empty, Seq.empty),
    render = (self) => {
      val noContent: Element = <.div(^.className := ProposalMatrixStyles.noContent)(
        <.span(^.className := ProposalMatrixStyles.sadSmiley)("😞"),
        <.br()(),
        <.Translate(^.value := "content.theme.matrix.noContent", ^.dangerousHtml := true)(),
        <.br()(),
        <.Translate(^.value := "content.theme.matrix.selectOtherTags")()
      )

      val listProposals: Seq[ReactElement] = self.state.listProposals.map(
        proposal => <.ProposalTileComponent(^.wrapped := ProposalTileComponent.ProposalTileProps(proposal = proposal))()
      )

      <.div(^.className := ProposalMatrixStyles.matrix)(
        <.h2(^.className := ProposalMatrixStyles.title2)(<.Translate(^.value := "content.theme.matrix.title")()),
        <.TagFilterComponent(
          ^.wrapped := TagFilterProps(self.props.wrapped.tags, self.props.wrapped.handleSelectedTags(self))
        )(),
        <.div(^.className := ProposalMatrixStyles.proposalList)(
          <.div(^.className := Seq(BulmaStyles.Grid.Columns.columns, BulmaStyles.Grid.Columns.columnsMultiLines))(
            if (self.state.listProposals.nonEmpty) {
              listProposals
            } else {
              noContent
            }
          )
        ),
        <.style()(ProposalMatrixStyles.render[String])
      )
    }
  )
}

object ProposalMatrixStyles extends StyleSheet.Inline {

  import dsl._

  val title2: StyleA = style(MakeStyles.title2, MakeStyles.Font.tradeGothicLTStd, marginTop(3.rem))
  val matrix: StyleA = style(minHeight(50.rem))
  val proposalList: StyleA = style(marginTop(4.rem))
  val sadSmiley: StyleA = style(fontSize(4.8.rem))
  val noContent: StyleA =
    style(
      marginTop(10.rem),
      width(100.%%),
      textAlign.center,
      fontSize(1.8.rem),
      lineHeight(3.4.rem),
      MakeStyles.Font.circularStdBook
    )
}
