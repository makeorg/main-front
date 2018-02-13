package org.make.front.components.operation.sequence

import io.github.shogowada.scalajs.reactjs.React.Props
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.redux.ReactRedux
import io.github.shogowada.scalajs.reactjs.redux.Redux.Dispatch
import io.github.shogowada.scalajs.reactjs.router.RouterProps._
import io.github.shogowada.scalajs.reactjs.router.WithRouter
import org.make.front.actions.SetCountry
import org.make.front.components.ObjectLoader.ObjectLoaderProps
import org.make.front.components.{AppState, ObjectLoader}
import org.make.front.models.{
  Operation,
  ProposalId,
  SequenceId,
  Tag,
  OperationExpanded => OperationModel,
  Sequence          => SequenceModel
}
import org.make.services.operation.OperationService
import org.make.services.sequence.SequenceService
import org.make.services.tag.TagService
import org.scalajs.dom

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

object SequenceOfTheOperationContainer {

  lazy val reactClass: ReactClass = WithRouter(ReactRedux.connectAdvanced(selectorFactory)(ObjectLoader.reactClass))

  def selectorFactory: (Dispatch) => (AppState, Props[Unit]) => ObjectLoaderProps[(OperationModel, SequenceModel)] =
    (dispatch: Dispatch) => { (state: AppState, props: Props[Unit]) =>
      {
        val operationSlug: String = props.`match`.params("operationSlug")
        // toDo remove default "FR" when backward compatibility not anymore required
        val countryCode: String = props.`match`.params.get("country").getOrElse("FR").toUpperCase
        if (state.country != countryCode) {
          dispatch(SetCountry(countryCode))
        }

        val search = org.scalajs.dom.window.location.search
        val firstProposalSlug = (if (search.startsWith("?")) { search.substring(1) } else { search })
          .split("&")
          .map(_.split("="))
          .find {
            case Array("firstProposal", _) => true
            case _                         => false
          }
          .flatMap {
            case Array(_, value) => Some(value)
            case _               => None
          }

        val futureMaybeOperationExpanded: () => Future[Option[(OperationModel, SequenceModel)]] = () => {
          val operationAndTags: Future[(Option[Operation], Seq[Tag])] = for {
            operation <- OperationService.getOperationBySlugAndCountry(operationSlug, countryCode)
            tags      <- TagService.getTags
          } yield (operation, tags)
          operationAndTags.map {
            case (maybeOperation, tagsList) =>
              OperationModel.getOperationExpandedFromOperation(maybeOperation, tagsList, countryCode)
          }.flatMap {
            case None => Future.successful(None)
            case Some(operation) =>
              SequenceService
                .startSequenceById(operation.landingSequenceId, Seq.empty)
                .map(sequence => Some((operation, sequence)))
          }
        }

        def startSequence(sequenceId: SequenceId)(proposals: Seq[ProposalId]): Future[SequenceModel] = {
          SequenceService.startSequenceById(sequenceId, proposals)
        }

        ObjectLoaderProps[(OperationModel, SequenceModel)](
          load = futureMaybeOperationExpanded,
          onNotFound = () => { props.history.push("/404") },
          childClass = SequenceOfTheOperation.reactClass,
          createChildProps = {
            case (operation, sequence) =>
              SequenceOfTheOperation.SequenceOfTheOperationProps(
                maybeFirstProposalSlug = firstProposalSlug,
                isConnected = state.connectedUser.isDefined,
                operation = operation,
                redirectHome = () => props.history.push("/"),
                startSequence = startSequence(operation.landingSequenceId),
                sequence = sequence,
                language = state.language,
                country = state.country,
                onWillMount = () => {
                  if (operation.isExpired) {
                    dom.window.location
                      .assign(operation.getWordingByLanguage(state.language).flatMap(_.learnMoreUrl).getOrElse("/"))
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
