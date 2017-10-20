package org.make.front.components.proposal

import io.github.shogowada.scalajs.reactjs.React.Props
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.redux.ReactRedux
import io.github.shogowada.scalajs.reactjs.redux.Redux.Dispatch
import org.make.front.components.AppState
import org.make.front.models.{Proposal => ProposalModel, Theme => ThemeModel, ThemeId => ThemeIdModel}

object ProposalWithThemeContainer {

  final case class ProposalWithThemeContainerProps(proposal: ProposalModel, index: Int)

  lazy val reactClass: ReactClass = ReactRedux.connectAdvanced(selectorFactory)(ProposalWithTheme.reactClass)

  def selectorFactory
    : (Dispatch) => (AppState, Props[ProposalWithThemeContainerProps]) => ProposalWithTheme.ProposalWithThemeProps =
    (_: Dispatch) => { (appState: AppState, ownProps: Props[ProposalWithThemeContainerProps]) =>
      def searchThemeById(themeId: ThemeIdModel): Option[ThemeModel] = {
        appState.themes.find(_.id == themeId)
      }

      val theme: Option[ThemeModel] = ownProps.wrapped.proposal.themeId.flatMap(themeId => searchThemeById(themeId))

      val themeName: Option[String] = theme.map(_.title)

      val themeSlug: Option[String] = theme.map(_.slug)

      ProposalWithTheme.ProposalWithThemeProps(
        proposal = ownProps.wrapped.proposal,
        themeName = themeName.orNull,
        themeSlug = themeSlug.orNull,
        index = ownProps.wrapped.index
      )
    }
}
