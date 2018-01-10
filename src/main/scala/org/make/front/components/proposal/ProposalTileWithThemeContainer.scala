package org.make.front.components.proposal

import io.github.shogowada.scalajs.reactjs.React.Props
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.redux.ReactRedux
import io.github.shogowada.scalajs.reactjs.redux.Redux.Dispatch
import org.make.front.components.AppState
import org.make.front.models.{
  Location          => LocationModel,
  OperationExpanded => OperationModel,
  Proposal          => ProposalModel,
  Sequence          => SequenceModel,
  ThemeId           => ThemeIdModel,
  TranslatedTheme   => TranslatedThemeModel
}

object ProposalTileWithThemeContainer {

  final case class ProposalTileWithThemeContainerProps(proposal: ProposalModel,
                                                       index: Int,
                                                       maybeOperation: Option[OperationModel],
                                                       maybeSequence: Option[SequenceModel],
                                                       maybeLocation: Option[LocationModel])

  lazy val reactClass: ReactClass = ReactRedux.connectAdvanced(selectorFactory)(ProposalTileWithTheme.reactClass)

  def selectorFactory
    : (Dispatch) => (AppState,
                     Props[ProposalTileWithThemeContainerProps]) => ProposalTileWithTheme.ProposalTileWithThemeProps =
    (_: Dispatch) => { (appState: AppState, props: Props[ProposalTileWithThemeContainerProps]) =>
      def searchThemeById(themeId: ThemeIdModel): Option[TranslatedThemeModel] = {
        appState.themes.find(_.id.value == themeId.value)
      }

      val theme: Option[TranslatedThemeModel] =
        props.wrapped.proposal.themeId.flatMap(themeId => searchThemeById(themeId))

      val themeName: Option[String] = theme.map(_.title)

      val themeSlug: Option[String] = theme.map(_.slug)

      ProposalTileWithTheme.ProposalTileWithThemeProps(
        proposal = props.wrapped.proposal,
        themeName = themeName.orNull,
        themeSlug = themeSlug.orNull,
        index = props.wrapped.index,
        maybeTheme = theme,
        maybeOperation = props.wrapped.maybeOperation,
        maybeSequence = props.wrapped.maybeSequence,
        maybeLocation = props.wrapped.maybeLocation
      )
    }
}
