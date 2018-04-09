package org.make.front.components.navInThemes

import io.github.shogowada.scalajs.reactjs.React.Props
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.redux.ReactRedux
import io.github.shogowada.scalajs.reactjs.redux.Redux.Dispatch
import org.make.front.actions.LoadConfiguration
import org.make.front.components.AppState

object NavInThemesContainer {

  lazy val reactClass: ReactClass = ReactRedux.connectAdvanced(selectorFactory)(NavInThemes.reactClass)

  def selectorFactory: (Dispatch) => (AppState, Props[Unit]) => NavInThemes.NavInThemesProps =
    (dispatch: Dispatch) => { (appState: AppState, _: Props[Unit]) =>
      if (appState.configuration.isEmpty) {
        dispatch(LoadConfiguration)
      }

      val shouldDisplayTheme =
        appState.configuration
          .flatMap(_.supportedCountries.find(_.countryCode == appState.country).map(_.coreIsAvailable))
          .getOrElse(false)

      NavInThemes.NavInThemesProps(appState.themes, shouldDisplayTheme, appState.country)
    }
}
