package org.make.front.components.operation

import io.github.shogowada.scalajs.reactjs.React.Props
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.redux.ReactRedux
import io.github.shogowada.scalajs.reactjs.redux.Redux.Dispatch
import io.github.shogowada.scalajs.reactjs.router.RouterProps._
import org.make.front.actions.SetCountry
import org.make.front.components.ObjectLoader.ObjectLoaderProps
import org.make.front.components.{AppState, ObjectLoader}
import org.make.front.models.{Operation => OperationModel, OperationExpanded, Tag}
import org.make.services.operation.OperationService
import org.make.services.tag.TagService
import org.scalajs.dom

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

object OperationContainer {

  lazy val reactClass: ReactClass = ReactRedux.connectAdvanced(selectorFactory)(ObjectLoader.reactClass)

  def selectorFactory: (Dispatch) => (AppState, Props[Unit]) => ObjectLoaderProps[OperationExpanded] =
    (dispatch: Dispatch) => { (appState: AppState, props: Props[Unit]) =>
      {
        val slug = props.`match`.params("operationSlug")
        // toDo remove default "FR" when backward compatibility not anymore required
        val countryCode: String = props.`match`.params.get("country").getOrElse("FR").toUpperCase
        if (appState.country != countryCode) {
          dispatch(SetCountry(countryCode))
        }

        val operationExpanded: () => Future[Option[OperationExpanded]] = () => {
          val operationAndTags: Future[(Option[OperationModel], Seq[Tag])] = for {
            operation <- OperationService.getOperationBySlugAndCountry(slug, countryCode)
            tags      <- TagService.getTags
          } yield (operation, tags)
          operationAndTags.map {
            case (maybeOperation, tagsList) =>
              OperationExpanded.getOperationExpandedFromOperation(maybeOperation, tagsList, countryCode)
          }
        }

        ObjectLoaderProps[OperationExpanded](
          load = operationExpanded,
          onNotFound = () => { props.history.push("/404") },
          childClass = Operation.reactClass,
          createChildProps = { operation =>
            Operation.OperationProps(
              operation,
              appState.language,
              onWillMount = () => {
                if (operation.isExpired) {
                  dom.window.location
                    .assign(operation.getWordingByLanguage(appState.language).flatMap(_.learnMoreUrl).getOrElse("/"))
                } else {
                  if (!operation.isActive) {
                    props.history.push("/404")
                  }
                }
              }
            )
          }
        )
      }
    }
}
