/*
 *
 * Make.org Main Front
 * Copyright (C) 2018 Make.org
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 *
 */

package org.make.front.components

import io.github.shogowada.scalajs.reactjs.VirtualDOM.VirtualDOMAttributes.Type.AS_IS
import io.github.shogowada.scalajs.reactjs.VirtualDOM.VirtualDOMElements.ReactClassElementSpec
import io.github.shogowada.scalajs.reactjs.VirtualDOM.{VirtualDOMAttributes, VirtualDOMElements}
import io.github.shogowada.statictags.{Attribute, BooleanAttributeSpec, SpaceSeparatedStringAttributeSpec}
import org.make.front.Main.CssSettings._
import org.make.front.components.consultation.partners.{PartnerList, PartnersItems}
import org.make.front.components.currentOperations.CurrentOperationsContainer

import scala.scalajs.js

object Components {

  implicit class RichSpaceSeparatedStringAttributeSpec(val spec: SpaceSeparatedStringAttributeSpec) extends AnyVal {
    def :=(style: StyleA): Attribute[Iterable[String]] = spec := style.htmlClass

    def :=(styleSeq: js.Array[StyleA]): Attribute[Iterable[String]] = spec := styleSeq.map(_.htmlClass).toSeq
  }

  object DangerouslySetInnerHtmlParameters {
    def apply(html: String): js.Object = {
      js.Dynamic.literal(__html = html).asInstanceOf[js.Object]
    }
  }

  class DangerouslySetInnerHtmlParameterSpec {
    def :=(html: String): Attribute[js.Object] = {
      Attribute(name = "dangerouslySetInnerHTML", value = DangerouslySetInnerHtmlParameters(html), AS_IS)
    }
  }

  implicit class DangerouslySetInnerHTMLVirtualDOMAttributes(attributes: VirtualDOMAttributes) {
    lazy val dangerouslySetInnerHTML: DangerouslySetInnerHtmlParameterSpec = new DangerouslySetInnerHtmlParameterSpec
    lazy val readOnly: BooleanAttributeSpec = BooleanAttributeSpec("readOnly")
  }

  implicit class RichVirtualDOMElements(val self: VirtualDOMElements) extends AnyVal {

    def AppContainerComponent: ReactClassElementSpec = self(org.make.front.components.AppContainer.reactClass)
    def RedirectToCountryRouteComponent: ReactClassElementSpec =
      self(org.make.front.components.RedirectToCountryRoute.apply())
    def ContainerComponent: ReactClassElementSpec = self(org.make.front.components.Container.reactClass)
    def SpinnerComponent: ReactClassElementSpec = self(spinner.Spinner.reactClass)
    def NotificationsComponent: ReactClassElementSpec = self(notifications.Notifications.reactClass)
    def CookieAlertContainerComponent: ReactClassElementSpec = self(cookieAlert.CookieAlertContainer.reactClass)
    def TriggerSignUpComponent: ReactClassElementSpec = self(users.authenticate.TriggerSignUp.reactClass)

    def MainHeaderContainer: ReactClassElementSpec = self(mainHeader.MainHeaderContainer.reactClass)
    def MainFooterComponent: ReactClassElementSpec = self(mainFooter.MainFooter.reactClass)
    def AltFooterComponent: ReactClassElementSpec = self(mainFooter.AltFooter.reactClass)
    def NavInThemesContainerComponent: ReactClassElementSpec = self(navInThemes.NavInThemesContainer.reactClass)

    def UserNavComponent: ReactClassElementSpec = self(userNav.UserNav.reactClass)
    def UserNavContainerComponent: ReactClassElementSpec = self(userNav.UserNavContainer.reactClass)
    def SearchFormContainer: ReactClassElementSpec = self(search.SearchFormContainer.reactClass)
    def SearchResultsContainerComponent: ReactClassElementSpec = self(search.SearchResultsContainer.reactClass)
    def HomeSearchResultsContainerComponent: ReactClassElementSpec = self(search.HomeSearchResultsContainer.reactClass)
    def NoResultToSearchComponent: ReactClassElementSpec = self(search.NoResultToSearch.reactClass)
    def ModalComponent: ReactClassElementSpec = self(modals.Modal.reactClass)
    def FullscreenModalComponent: ReactClassElementSpec = self(modals.FullscreenModal.reactClass)

    /*********************/
    def UserProfileComponent: ReactClassElementSpec = self(userProfile.UserProfile.reactClass)
    def UserProfileInformationsComponent: ReactClassElementSpec = self(userProfile.UserProfileInformations.reactClass)
    def UserDescriptionComponent: ReactClassElementSpec = self(userProfile.UserDescription.reactClass)
    def UserProfileSummaryComponent: ReactClassElementSpec = self(userProfile.UserProfileSummary.reactClass)
    def UserProfileProposalsContainerComponent: ReactClassElementSpec =
      self(userProfile.UserProfileProposalsContainer.reactClass)
    def UserProfileActionsComponent: ReactClassElementSpec = self(userProfile.UserProfileActions.reactClass)
    def UserProfileSettingsComponent: ReactClassElementSpec = self(userProfile.UserProfileSettings.reactClass)
    def UserProfileResetPasswordContainerComponent: ReactClassElementSpec =
      self(userProfile.editingUserProfile.ResetPasswordContainer.reactClass)
    def UserProfileOptinNewsletterContainerComponent: ReactClassElementSpec =
      self(userProfile.editingUserProfile.OptinNewsletterContainer.reactClass)
    def UserProfileFormContainerComponent: ReactClassElementSpec =
      self(userProfile.editingUserProfile.UserProfileFormContainer.reactClass)
    def OrganisationProfileFormContainerComponent: ReactClassElementSpec =
      self(userProfile.editingUserProfile.OrganisationProfileFormContainer.reactClass)
    def UserProfileDeleteAccountComponent: ReactClassElementSpec =
      self(userProfile.editingUserProfile.DeleteAccount.reactClass)
    def UserProfileDeleteAccountFormContainerComponent: ReactClassElementSpec =
      self(userProfile.editingUserProfile.DeleteAccountFormContainer.reactClass)
    def UserProfileDeleteAccountFormComponent: ReactClassElementSpec =
      self(userProfile.editingUserProfile.DeleteAccountForm.reactClass)
    def TabNavComponent: ReactClassElementSpec = self(userProfile.navUserProfile.TabNav.reactClass)
    def ButtonNavComponent: ReactClassElementSpec =
      self(userProfile.navUserProfile.ButtonNav.reactClass)
    def UserLikeItProposalsContainerComponent: ReactClassElementSpec =
      self(userProfile.UserLikeItProposalsContainer.reactClass)

    /*********************/
    def ActorProfileContainerComponent: ReactClassElementSpec = self(actorProfile.ActorProfileContainer.reactClass)
    def ActorProfileComponent: ReactClassElementSpec = self(actorProfile.ActorProfile.reactClass)
    def ActorDescriptionComponent: ReactClassElementSpec = self(actorProfile.ActorDescription.reactClass)
    def ActorProfileInformationsComponent: ReactClassElementSpec =
      self(actorProfile.ActorProfileInformations.reactClass)
    def ActorProfileProposalsContainerComponent: ReactClassElementSpec =
      self(actorProfile.ActorProfileProposalsContainer.reactClass)
    def ActorProfileContributionsContainerComponent: ReactClassElementSpec =
      self(actorProfile.ActorProfileContributionsContainer.reactClass)
    def ActorTabNavComponent: ReactClassElementSpec = self(actorProfile.navActorProfile.ActorTabNav.reactClass)
    def ActorButtonNavComponent: ReactClassElementSpec =
      self(actorProfile.navActorProfile.ActorButtonNav.reactClass)

    /*********************/
    def ShareComponent: ReactClassElementSpec = self(share.ShareProposal.reactClass)
    def ShareLikeItProposalComponent: ReactClassElementSpec = self(share.ShareLikeItProposal.reactClass)
    def ShareMobileComponent: ReactClassElementSpec = self(share.ShareMobile.reactClass)
    def ShareProposalPageComponent: ReactClassElementSpec = self(share.ShareProposalPage.reactClass)
    def ShareUserProfileComponent: ReactClassElementSpec = self(share.ShareUserProfile.reactClass)

    /*********************/
    def ActivateAccountContainerComponent: ReactClassElementSpec =
      self(activateAccount.ActivateAccountContainer.reactClass)
    def RequireAuthenticatedUserComponent: ReactClassElementSpec =
      self(users.authenticate.RequireAuthenticatedUserContainer.reactClass)

    def RecoverPasswordContainerComponent: ReactClassElementSpec =
      self(authenticate.recoverPassword.RecoverPasswordContainer.reactClass)
    def ResetPasswordContainerComponent: ReactClassElementSpec =
      self(authenticate.resetPassword.ResetPasswordContainer.reactClass)
    def LoginOrRegisterComponent: ReactClassElementSpec = self(authenticate.LoginOrRegister.reactClass)
    def AuthenticateWithFacebookContainerComponent: ReactClassElementSpec =
      self(authenticate.AuthenticateWithFacebookContainer.reactClass)
    def AuthenticateWithGoogleContainerComponent: ReactClassElementSpec =
      self(authenticate.AuthenticateWithGoogleContainer.reactClass)

    def AuthenticateWithSocialNetworksComponent: ReactClassElementSpec =
      self(authenticate.AuthenticateWithSocialNetworks.reactClass)

    def RegisterWithSocialNetworksComponent: ReactClassElementSpec =
      self(authenticate.register.RegisterWithSocialNetworks.reactClass)

    def RegisterWithSocialNetworksOrEmailComponent: ReactClassElementSpec =
      self(authenticate.register.RegisterWithSocialNetworksOrEmail.regular)
    def RegisterWithSocialNetworksOrEmailExpandedComponent: ReactClassElementSpec =
      self(authenticate.register.RegisterWithSocialNetworksOrEmail.expanded)
    def RegisterWithEmailContainerComponent: ReactClassElementSpec =
      self(authenticate.register.RegisterContainer.registerWithEmailReactClass)
    def RegisterWithEmailExpandedContainerComponent: ReactClassElementSpec =
      self(authenticate.register.RegisterContainer.registerWithEmailExpandedReactClass)
    def LoginWithEmailComponent: ReactClassElementSpec = self(authenticate.login.LoginWithEmailContainer.reactClass)
    def LoginWithEmailOrSocialNetworksComponent: ReactClassElementSpec =
      self(authenticate.login.LoginWithSocialNetworksOrEmail.reactClass)
    def NewPasswordInputComponent: ReactClassElementSpec = self(authenticate.NewPasswordInput.reactClass)

    /*********************/
    def SubmitProposalComponent: ReactClassElementSpec = self(submitProposal.SubmitProposal.reactClass)
    def ConfirmationOfProposalSubmissionComponent: ReactClassElementSpec =
      self(submitProposal.ConfirmationOfProposalSubmission.reactClass)
    def SubmitProposalFormContainerComponent: ReactClassElementSpec =
      self(submitProposal.SubmitProposalFormContainer.reactClass)
    def SubmitProposalAndAuthenticateContainerComponent: ReactClassElementSpec =
      self(submitProposal.SubmitProposalAndAuthenticateContainer.reactClass)

    /*********************/
    def ProposalInfosComponent: ReactClassElementSpec = self(proposal.ProposalInfos.reactClass)
    def ProposalAuthorInfos: ReactClassElementSpec = self(proposal.ProposalAuthorInfos.reactClass)
    def ShareOwnProposalComponent: ReactClassElementSpec = self(proposal.ShareOwnProposal.reactClass)
    def ProposalSOperationInfosComponent: ReactClassElementSpec = self(proposal.ProposalSOperationInfos.reactClass)
    def ProposalTileWithoutVoteActionComponent: ReactClassElementSpec =
      self(proposal.ProposalTileWithoutVoteAction.reactClass)
    def ProposalTileComponent: ReactClassElementSpec = self(proposal.ProposalTile.reactClass)
    def ProposalTileWithThemeContainerComponent: ReactClassElementSpec =
      self(proposal.ProposalTileWithThemeContainer.reactClass)
    def ProposalTileWithTagsComponent: ReactClassElementSpec = self(proposal.ProposalTileWithTags.reactClass)
    def ProposalTileWithOrganisationsVotesComponent: ReactClassElementSpec =
      self(proposal.ProposalTileWithOrganisationsVotes.reactClass)
    def ProposalTileWithActorPositionComponent: ReactClassElementSpec =
      self(proposal.ProposalTileWithActorPosition.reactClass)
    def ProposalTileWithVideoComponent: ReactClassElementSpec = self(proposal.ProposalTileWithVideo.reactClass)
    def FakeProposalTileComponent: ReactClassElementSpec = self(proposal.FakeProposalTile.reactClass)
    def ProposalActorVotedComponent: ReactClassElementSpec = self(proposal.ProposalActorVoted.reactClass)

    /*********************/
    def TrendingShowcaseContainerComponent: ReactClassElementSpec = self(showcase.TrendingShowcaseContainer.reactClass)
    def OperationShowcaseContainerComponent: ReactClassElementSpec =
      self(showcase.OperationShowcaseContainer.reactClass)
    def PromptingToProposeInRelationToOperationTileComponent: ReactClassElementSpec =
      self(showcase.PromptingToProposeInRelationToOperationTile.reactClass)

    def FeaturedArticlesShowcaseContainerComponent: ReactClassElementSpec =
      self(showcase.FeaturedArticlesShowcaseContainer.reactClass)

    /*********************/
    def SequenceContainerComponent: ReactClassElementSpec = self(sequence.SequenceContainer.reactClass)
    def ProgressBarComponent: ReactClassElementSpec = self(sequence.ProgressBar.reactClass)
    def ProposalInsideSequenceComponent: ReactClassElementSpec =
      self(sequence.contents.ProposalInsideSequence.reactClass)
    def WaitingForSequence: ReactClassElementSpec = self(operation.sequence.WaitingForSequence.reactClass)

    /*********************/
    def VoteContainerComponent: ReactClassElementSpec = self(proposal.vote.VoteContainer.reactClass)
    def VoteButtonComponent: ReactClassElementSpec = self(proposal.vote.VoteButton.reactClass)
    def QualificateVoteButtonComponent: ReactClassElementSpec = self(proposal.vote.QualificateVoteButton.reactClass)
    def QualificateVoteComponent: ReactClassElementSpec = self(proposal.vote.QualificateVote.reactClass)
    def ResultsOfVoteComponent: ReactClassElementSpec = self(proposal.vote.ResultsOfVote.reactClass)
    def DisplayVotesDataComponent: ReactClassElementSpec =
      self(proposal.vote.DisplayVotesData.reactClass)
    def DisplayVotesResultsComponent: ReactClassElementSpec =
      self(proposal.vote.DisplayVotesResults.reactClass)
    def DisplayVotesQualificationsComponent: ReactClassElementSpec =
      self(proposal.vote.DisplayVotesQualifications.reactClass)

    /*********************/
    def PoliticalActionsContainerComponent: ReactClassElementSpec =
      self(politicalActions.PoliticalActionsContainer.reactClass)
    def PoliticalActionComponent: ReactClassElementSpec = self(politicalActions.PoliticalAction.reactClass)
    def NoPoliticalActionComponent: ReactClassElementSpec = self(politicalActions.NoPoliticalAction.reactClass)

    /*********************/
    def TagComponent: ReactClassElementSpec = self(tags.Tag.reactClass)
    def TagsListComponent: ReactClassElementSpec = self(tags.TagsList.reactClass)
    def FilterByTagsComponent: ReactClassElementSpec = self(tags.FilterByTags.reactClass)

    /*********************/
    def ThemeComponent: ReactClassElementSpec =
      self(theme.Theme.reactClass)
    def ThemeHeaderComponent: ReactClassElementSpec = self(theme.ThemeHeader.reactClass)
    def SubmitProposalInRelationToThemeComponent: ReactClassElementSpec =
      self(theme.SubmitProposalInRelationToTheme.reactClass)
    def ResultsInThemeContainerComponent: ReactClassElementSpec = self(theme.ResultsInThemeContainer.reactClass)
    def ResultsInThemeComponent: ReactClassElementSpec = self(theme.ResultsInTheme.reactClass)

    /*********************/
    def ConsultationComponent: ReactClassElementSpec = self(consultation.Consultation.reactClass)
    def ConsultationContainerComponent: ReactClassElementSpec = self(consultation.ConsultationContainer.reactClass)
    def ConsultationHeaderComponent: ReactClassElementSpec = self(consultation.ConsultationHeader.reactClass)
    def ConsultationLogoComponent: ReactClassElementSpec = self(consultation.ConsultationLogo.reactClass)
    def ConsultationProposalComponent: ReactClassElementSpec = self(consultation.ConsultationProposal.reactClass)
    def ConsultationLinkSequenceComponent: ReactClassElementSpec =
      self(consultation.ConsultationLinkSequence.reactClass)
    def ResultsInConsultationContainerComponent: ReactClassElementSpec =
      self(consultation.ResultsInConsultationContainer.reactClass)
    def ResultsInConsultationComponent: ReactClassElementSpec = self(consultation.ResultsInConsultation.reactClass)
    def ConsultationPresentationComponent: ReactClassElementSpec =
      self(consultation.ConsultationPresentation.reactClass)
    def ConsultationSection: ReactClassElementSpec = self(consultation.ConsultationSection.reactClass)
    def ActionsSectionContainer: ReactClassElementSpec = self(consultation.ActionsSectionContainer.reactClass)
    def ActionsSection: ReactClassElementSpec = self(consultation.ActionsSection.reactClass)
    def ConsultationCommunityComponent: ReactClassElementSpec = self(consultation.ConsultationCommunity.reactClass)
    def ConsultationShareComponent: ReactClassElementSpec = self(consultation.ConsultationShare.reactClass)
    def ConsultationShareMobileComponent: ReactClassElementSpec = self(consultation.ConsultationShareMobile.reactClass)
    def PartnersItemsComponent: ReactClassElementSpec = self(PartnersItems.reactClass)
    def CurrentOperationsContainerComponent: ReactClassElementSpec = self(CurrentOperationsContainer.reactClass)
    def PartnerListComponent: ReactClassElementSpec = self(PartnerList.reactClass)

    /*********************/
    def SubmitProposalInRelationToOperationComponent: ReactClassElementSpec =
      self(operation.SubmitProposalInRelationToOperation.reactClass)
    def SequenceOfTheOperationComponent: ReactClassElementSpec =
      self(operation.sequence.SequenceOfTheOperation.reactClass)
    def SequenceOfTheOperationContainerComponent: ReactClassElementSpec =
      self(operation.sequence.SequenceOfTheOperationContainer.reactClass)

    /*********************/
    def HomeComponent: ReactClassElementSpec = self(home.Home.reactClass)
    def WelcomeComponent: ReactClassElementSpec = self(home.Welcome.reactClass)
    def FeaturedOperationComponent: ReactClassElementSpec = self(home.FeaturedOperation.reactClass)
    def ExplanationsComponent: ReactClassElementSpec = self(home.Explanations.reactClass)

  }

}
