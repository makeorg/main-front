package org.make.front.components.proposal

import io.github.shogowada.scalajs.reactjs.React.Props
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.redux.ReactRedux
import io.github.shogowada.scalajs.reactjs.redux.Redux.Dispatch
import org.make.front.components.AppState
import org.make.front.models.{Proposal => ProposalModel, TranslatedTheme => TranslatedThemeModel, ThemeId => ThemeIdModel}

object ProposalTileWithThemeContainer {

  final case class ProposalTileWithThemeContainerProps(proposal: ProposalModel, index: Int)

  lazy val reactClass: ReactClass = ReactRedux.connectAdvanced(selectorFactory)(ProposalTileWithTheme.reactClass)

  def selectorFactory
    : (Dispatch) => (AppState,
                     Props[ProposalTileWithThemeContainerProps]) => ProposalTileWithTheme.ProposalTileWithThemeProps =
    (_: Dispatch) => { (appState: AppState, ownProps: Props[ProposalTileWithThemeContainerProps]) =>
      def searchThemeById(themeId: ThemeIdModel): Option[TranslatedThemeModel] = {
        appState.themes.find(_.id.value == themeId.value)
      }

      val theme: Option[TranslatedThemeModel] = ownProps.wrapped.proposal.themeId.flatMap(themeId => searchThemeById(themeId))

      val themeName: Option[String] = theme.map(_.title)

      val themeSlug: Option[String] = theme.map(_.slug)

      ProposalTileWithTheme.ProposalTileWithThemeProps(
        proposal = ownProps.wrapped.proposal,
        themeName = themeName.orNull,
        themeSlug = themeSlug.orNull,
        index = ownProps.wrapped.index
      )
    }
}
