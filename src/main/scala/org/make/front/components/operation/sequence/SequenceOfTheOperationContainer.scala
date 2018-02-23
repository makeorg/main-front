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
import org.make.services.proposal.ProposalService
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
        val shouldReload = state.country != countryCode
        if (shouldReload) {
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

        def startSequence(sequenceId: SequenceId)(proposals: Seq[ProposalId]): Future[SequenceModel] = {
          firstProposalSlug.map { slug =>
            ProposalService
              .searchProposals(slug = Some(slug), language = Some(state.language), country = Some(state.country))
              .map(_.results.map(_.id))
          }.getOrElse(Future.successful(Seq.empty)).flatMap { proposalsToInclude =>
            SequenceService.startSequenceById(
              sequenceId,
              proposalsToInclude ++ proposals.filter(id => !proposalsToInclude.contains(id))
            )
          }
        }

        val futureMaybeOperationAndSequence: () => Future[Option[(OperationModel, SequenceModel)]] = () => {
          val operationAndTags: Future[(Option[Operation], Seq[Tag])] = for {
            operation <- OperationService.getOperationBySlugAndCountry(operationSlug, state.country)
            tags      <- TagService.getTags
          } yield (operation, tags)
          operationAndTags.map {
            case (maybeOperation, tagsList) =>
              OperationModel.getOperationExpandedFromOperation(maybeOperation, tagsList, state.country)
          }.flatMap {
            case None => Future.successful(None)
            case Some(operation) =>
              startSequence(operation.landingSequenceId)(Seq.empty)
                .map(sequence => Some((operation, sequence)))
          }
        }

        val onNotFound: () => Unit = if (shouldReload) { () =>
          {}
        } else { () =>
          props.history.push("/404")
        }

        val load: () => Future[Option[(OperationModel, SequenceModel)]] = if (shouldReload) { () =>
          {
            Future.successful(None)
          }
        } else {
          futureMaybeOperationAndSequence
        }

        ObjectLoaderProps[(OperationModel, SequenceModel)](
          load = load,
          onNotFound = onNotFound,
          childClass = SequenceOfTheOperation.reactClass,
          createChildProps = {
            case (operation, sequence) =>
              SequenceOfTheOperation.SequenceOfTheOperationProps(
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
