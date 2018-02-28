package org.make.front.components.currentOperations

import io.github.shogowada.scalajs.reactjs.React.Props
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.redux.ReactRedux
import io.github.shogowada.scalajs.reactjs.redux.Redux.Dispatch
import io.github.shogowada.scalajs.reactjs.router.RouterProps._
import org.make.front.actions.SetCountry
import org.make.front.components.ObjectLoader.ObjectLoaderProps
import org.make.front.components.{AppState, ObjectLoader}
import org.make.front.facades._
import org.make.front.models.{BusinessConfiguration, CountryConfiguration, Operation => OperationModel, OperationExpanded => OperationExpandedModel, Tag => TagModel}
import org.make.services.operation.OperationService
import org.make.services.tag.TagService

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

object CurrentOperationsContainer {

  lazy val reactClass: ReactClass = ReactRedux.connectAdvanced(selectorFactory)(ObjectLoader.reactClass)

  def selectorFactory: (Dispatch) => (AppState, Props[Unit]) => ObjectLoaderProps[Seq[OperationExpandedModel]] =
    (dispatch: Dispatch) => { (appState: AppState, props: Props[Unit]) =>
      {}
      val countryCode: String = props.`match`.params.get("country").getOrElse("FR").toUpperCase

      val supportedCountries: Seq[CountryConfiguration] = appState.configuration.map { businessConfiguration: BusinessConfiguration =>
        businessConfiguration.supportedCountries.map { countryConfiguration: CountryConfiguration =>
          countryConfiguration.defaultLanguage match {
            case "fr" =>
              countryConfiguration.copy(flagUrl = frFlag.toString)
            case "en" =>
              countryConfiguration.copy(flagUrl = gbFlag.toString)
            case "it" =>
              countryConfiguration.copy(flagUrl = itFlag.toString)
            case _ =>
              countryConfiguration.copy(flagUrl = "")
          }

        }
      }.getOrElse(Seq.empty)

      if (!supportedCountries.exists(country => country.countryCode == countryCode)) {
        props.history.push("/404")
      }

      if (appState.country != countryCode) {
        dispatch(SetCountry(countryCode))
      }

      val operationExpanded: () => Future[Option[Seq[OperationExpandedModel]]] = () => {
        val operationsAndTags: Future[(Seq[OperationModel], Seq[TagModel])] = for {
          operations <- OperationService.getOperationsByCountry(countryCode)
          tags      <- TagService.getTags
        } yield (operations, tags)

        operationsAndTags.map {
          case (operations, tagsList) =>
            operations.flatMap { operation =>
              OperationExpandedModel.getOperationExpandedFromOperation(Some(operation), tagsList, countryCode)
            }
        }.map(Some(_))
      }



      ObjectLoaderProps[Seq[OperationExpandedModel]](
        load = operationExpanded,
        onNotFound = () => {
          props.history.push("/404")
        },
        childClass = CurrentOperations.reactClass,
        createChildProps = { operations =>
          CurrentOperations.CurrentOperationsProps(
            country = appState.language,
            operations = operations,
            supportedCountries = supportedCountries
          )
        }
      )
    }
}
