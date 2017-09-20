package org.make.front.components.proposals.proposal

import io.github.shogowada.scalajs.reactjs.React.Props
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.redux.ReactRedux
import io.github.shogowada.scalajs.reactjs.redux.Redux.Dispatch
import org.make.front.components.AppState
import org.make.front.models.{Proposal => ProposalModel, Theme => ThemeModel, ThemeId => ThemeIdModel}

object ProposalWithThemeContainer {

  final case class ProposalWithThemeContainerProps(proposal: ProposalModel)

  lazy val reactClass: ReactClass = ReactRedux.connectAdvanced(selectorFactory)(ProposalWithTheme.reactClass)

  def selectorFactory
    : (Dispatch) => (AppState, Props[ProposalWithThemeContainerProps]) => ProposalWithTheme.ProposalWithThemeProps =
    (_: Dispatch) => { (appState: AppState, ownProps: Props[ProposalWithThemeContainerProps]) =>
      def searchThemeById(themeId: ThemeIdModel): Option[ThemeModel] = {
        appState.themes.find(_.id == themeId)
      }

      val themeName: Option[String] =
        ownProps.wrapped.proposal.themeId.flatMap(themeId => searchThemeById(themeId)).map(_.title)

      ProposalWithTheme.ProposalWithThemeProps(proposal = ownProps.wrapped.proposal, themeName = themeName.orNull)

    }
}
