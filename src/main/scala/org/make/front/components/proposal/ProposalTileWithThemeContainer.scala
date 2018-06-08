package org.make.front.components.proposal

import io.github.shogowada.scalajs.reactjs.React.Props
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.redux.ReactRedux
import io.github.shogowada.scalajs.reactjs.redux.Redux.Dispatch
import org.make.front.components.AppState
import org.make.front.models.{
  SequenceId,
  Location          => LocationModel,
  OperationExpanded => OperationModel,
  Proposal          => ProposalModel,
  ThemeId           => ThemeIdModel,
  TranslatedTheme   => TranslatedThemeModel
}
import org.make.services.tracking.TrackingLocation

object ProposalTileWithThemeContainer {

  final case class ProposalTileWithThemeContainerProps(proposal: ProposalModel,
                                                       index: Int,
                                                       maybeOperation: Option[OperationModel],
                                                       maybeSequenceId: Option[SequenceId],
                                                       maybeLocation: Option[LocationModel],
                                                       trackingLocation: TrackingLocation)

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
        themeName = themeName.getOrElse(""),
        themeSlug = themeSlug.getOrElse(""),
        index = props.wrapped.index,
        maybeTheme = theme,
        maybeOperation = props.wrapped.maybeOperation,
        maybeSequenceId = props.wrapped.maybeSequenceId,
        maybeLocation = props.wrapped.maybeLocation,
        trackingLocation = props.wrapped.trackingLocation,
        country = appState.country
      )
    }
}
