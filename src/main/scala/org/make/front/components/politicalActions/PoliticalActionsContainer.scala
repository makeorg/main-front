package org.make.front.components.politicalActions

import io.github.shogowada.scalajs.reactjs.React.Props
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.redux.ReactRedux
import io.github.shogowada.scalajs.reactjs.redux.Redux.Dispatch
import org.make.front.actions.LoadPoliticalAction
import org.make.front.components.AppState
import org.make.front.models.{TranslatedTheme => TranslatedThemeModel}

object PoliticalActionsContainer {

  case class PoliticalActionsContainerProps(currentTheme: Option[TranslatedThemeModel])

  lazy val reactClass: ReactClass =
    ReactRedux.connectAdvanced(selectorFactory)(PoliticalActionsList.reactClass)

  def selectorFactory: (
    Dispatch
  ) => (AppState, Props[PoliticalActionsContainerProps]) => PoliticalActionsList.PoliticalActionsListProps = {
    (dispatch: Dispatch) =>
      { (state: AppState, props: Props[PoliticalActionsContainerProps]) =>
        val politicalActionsList = state.politicalActions

        dispatch(LoadPoliticalAction)

        PoliticalActionsList.PoliticalActionsListProps(
          politicalActionsList.filter(_.themeSlug == props.wrapped.currentTheme.map(_.slug))
        )
      }
  }
}
