package org.make.front.components

import org.make.front.actions.StorePendingProposal
import org.make.front.models.{Notification, PoliticalAction, Theme, User}

final case class AppState(themes: Seq[Theme],
                          politicalActions: Seq[PoliticalAction],
                          connectedUser: Option[User],
                          technicalState: TechnicalState)

final case class TechnicalState(notifications: Seq[Notification],
                                showLoginModal: Boolean,
                                useProposalLoginView: Boolean,
                                pendingProposalAction: Option[StorePendingProposal] = None,
                                passwordRecoveryModalIsOpen: Boolean)
