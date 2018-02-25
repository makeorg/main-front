package org.make.front.components.currentlyRunning

import io.github.shogowada.scalajs.reactjs.React.Props
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.redux.ReactRedux
import io.github.shogowada.scalajs.reactjs.redux.Redux.Dispatch
import io.github.shogowada.scalajs.reactjs.router.RouterProps._
import org.make.front.actions.SetCountry
import org.make.front.components.ObjectLoader.ObjectLoaderProps
import org.make.front.components.{AppState, ObjectLoader}
import org.make.front.models.{Operation => OperationModel, OperationExpanded => OperationExpandedModel, Tag => TagModel}
import org.make.services.operation.OperationService
import org.make.services.tag.TagService
import org.make.front.facades._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

object CurrentlyRunningContainer {

  lazy val reactClass: ReactClass = ReactRedux.connectAdvanced(selectorFactory)(ObjectLoader.reactClass)

  def selectorFactory: (Dispatch) => (AppState, Props[Unit]) => ObjectLoaderProps[OperationExpandedModel] =
    (dispatch: Dispatch) => { (appState: AppState, props: Props[Unit]) =>
      {}
      val countryCode: String = props.`match`.params.get("country").getOrElse("FR").toUpperCase

      if (appState.country != countryCode) {
        dispatch(SetCountry(countryCode))
      }

      val operationExpanded: () => Future[Option[OperationExpandedModel]] = () => {
        val operationAndTags: Future[(Option[OperationModel], Seq[TagModel])] = for {
          operation <- OperationService.getOperationBySlugAndCountry("vff", countryCode)
          tags      <- TagService.getTags
        } yield (operation, tags)
        operationAndTags.map {
          case (maybeOperation, tagsList) =>
            OperationExpandedModel.getOperationExpandedFromOperation(maybeOperation, tagsList, countryCode)
        }
      }

      val supportedCountries = appState.configuration
        .map(_.supportedCountries)
        .map { country =>
          country.map(_.defaultLanguage) match {
            case Seq("fr") =>
              country.map(_.copy(flagUrl = frFlag.toString))
            case Seq("gb") =>
              country.map(_.copy(flagUrl = gbFlag.toString))
            case Seq("it") =>
              country.map(_.copy(flagUrl = itFlag.toString))
            case _ =>
              country.map(_.copy(flagUrl = ""))
          }
        }
        .getOrElse(Seq.empty)

      ObjectLoaderProps[OperationExpandedModel](
        load = operationExpanded,
        onNotFound = () => {
          props.history.push("/404")
        },
        childClass = CurrentlyRunning.reactClass,
        createChildProps = { operation =>
          CurrentlyRunning.CurrentlyRunningProps(
            country = appState.language,
            operations = Seq(operation, operation),
            supportedCountries = supportedCountries
          )
        }
      )
    }
}
