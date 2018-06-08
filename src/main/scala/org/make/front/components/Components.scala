package org.make.front.components

import io.github.shogowada.scalajs.reactjs.VirtualDOM.VirtualDOMAttributes.Type.AS_IS
import io.github.shogowada.scalajs.reactjs.VirtualDOM.VirtualDOMElements.ReactClassElementSpec
import io.github.shogowada.scalajs.reactjs.VirtualDOM.{VirtualDOMAttributes, VirtualDOMElements}
import io.github.shogowada.statictags.{Attribute, BooleanAttributeSpec, SpaceSeparatedStringAttributeSpec}
import org.make.front.Main.CssSettings._

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
    def ContainerComponent: ReactClassElementSpec = self(org.make.front.components.Container.reactClass)
    def SpinnerComponent: ReactClassElementSpec = self(spinner.Spinner.reactClass)
    def NotificationsComponent: ReactClassElementSpec = self(notifications.Notifications.reactClass)
    def CookieAlertContainerComponent: ReactClassElementSpec = self(cookieAlert.CookieAlertContainer.reactClass)
    def TriggerSignUpComponent: ReactClassElementSpec = self(users.authenticate.TriggerSignUp.reactClass)

    def MainHeaderContainer: ReactClassElementSpec = self(mainHeader.MainHeaderContainer.reactClass)
    def MainFooterComponent: ReactClassElementSpec = self(mainFooter.MainFooter.reactClass)
    def NavInThemesContainerComponent: ReactClassElementSpec = self(navInThemes.NavInThemesContainer.reactClass)

    def UserNavComponent: ReactClassElementSpec = self(userNav.UserNav.reactClass)
    def UserNavContainerComponent: ReactClassElementSpec = self(userNav.UserNavContainer.reactClass)
    def SearchFormContainer: ReactClassElementSpec = self(search.SearchFormContainer.reactClass)
    def SearchResultsContainerComponent: ReactClassElementSpec = self(search.SearchResultsContainer.reactClass)
    def NoResultToSearchComponent: ReactClassElementSpec = self(search.NoResultToSearch.reactClass)
    def ModalComponent: ReactClassElementSpec = self(modals.Modal.reactClass)
    def FullscreenModalComponent: ReactClassElementSpec = self(modals.FullscreenModal.reactClass)

    def UserProfileComponent: ReactClassElementSpec =
      self(userProfile.UserProfile.reactClass)

    def ShareComponent: ReactClassElementSpec = self(share.ShareProposal.reactClass)

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
    def ShareOwnProposalComponent: ReactClassElementSpec = self(proposal.ShareOwnProposal.reactClass)
    def ProposalSOperationInfosComponent: ReactClassElementSpec = self(proposal.ProposalSOperationInfos.reactClass)
    def ProposalTileComponent: ReactClassElementSpec = self(proposal.ProposalTile.reactClass)
    def ProposalTileWithThemeContainerComponent: ReactClassElementSpec =
      self(proposal.ProposalTileWithThemeContainer.reactClass)
    def ProposalTileWithThemeComponent: ReactClassElementSpec = self(proposal.ProposalTileWithTheme.reactClass)
    def ProposalTileWithTagsComponent: ReactClassElementSpec = self(proposal.ProposalTileWithTags.reactClass)
    def ProposalActorVotedComponent: ReactClassElementSpec = self(proposal.ProposalActorVoted.reactClass)

    /*********************/
    def TrendingShowcaseContainerComponent: ReactClassElementSpec = self(showcase.TrendingShowcaseContainer.reactClass)
    def ThemeShowcaseContainerComponent: ReactClassElementSpec = self(showcase.ThemeShowcaseContainer.reactClass)
    def PromptingToProposeInRelationToThemeTileComponent: ReactClassElementSpec =
      self(showcase.PromptingToProposeInRelationToThemeTile.reactClass)
    def LabelShowcaseContainerComponent: ReactClassElementSpec = self(showcase.LabelShowcaseContainer.reactClass)

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
    def ConsultationPresentationMobileComponent: ReactClassElementSpec =
      self(consultation.ConsultationPresentationMobile.reactClass)

    /*********************/
    def VFFOperationIntroComponent: ReactClassElementSpec = self(operation.intro.VFFOperationIntro.reactClass)
    def VFFITOperationIntroComponent: ReactClassElementSpec = self(operation.intro.VFFITOperationIntro.reactClass)
    def VFFGBOperationIntroComponent: ReactClassElementSpec = self(operation.intro.VFFGBOperationIntro.reactClass)
    def MVEOperationIntroComponent: ReactClassElementSpec = self(operation.intro.MVEOperationIntro.reactClass)
    def ClimatParisOperationIntroComponent: ReactClassElementSpec =
      self(operation.intro.ClimatParisOperationIntro.reactClass)
    def LPAEOperationIntroComponent: ReactClassElementSpec = self(operation.intro.LPAEOperationIntro.reactClass)
    def ChanceAuxJeunesOperationIntroComponent: ReactClassElementSpec =
      self(operation.intro.ChanceAuxJeunesOperationIntro.reactClass)
    def MakeEuropeOperationIntroComponent: ReactClassElementSpec =
      self(operation.intro.MakeEuropeOperationIntro.reactClass)
    def OperationHeaderComponent: ReactClassElementSpec = self(operation.OperationHeader.reactClass)
    def SubmitProposalInRelationToOperationComponent: ReactClassElementSpec =
      self(operation.SubmitProposalInRelationToOperation.reactClass)
    def ResultsInOperationContainerComponent: ReactClassElementSpec =
      self(operation.ResultsInOperationContainer.reactClass)
    def ResultsInOperationComponent: ReactClassElementSpec = self(operation.ResultsInOperation.reactClass)
    def SequenceOfTheOperationComponent: ReactClassElementSpec =
      self(operation.sequence.SequenceOfTheOperation.reactClass)
    def SequenceOfTheOperationContainerComponent: ReactClassElementSpec =
      self(operation.sequence.SequenceOfTheOperationContainer.reactClass)

    /*********************/
    def HomeComponent: ReactClassElementSpec = self(home.Home.reactClass)
    def WelcomeComponent: ReactClassElementSpec = self(home.Welcome.reactClass)
    def VFFFeaturedOperationComponent: ReactClassElementSpec = self(home.VFFFeaturedOperation.reactClass)
    def MVEFeaturedOperationComponent: ReactClassElementSpec = self(home.MVEFeaturedOperation.reactClass)
    def CAJFeaturedOperationComponent: ReactClassElementSpec = self(home.CAJFeaturedOperation.reactClass)
    def ExplanationsComponent: ReactClassElementSpec = self(home.Explanations.reactClass)

  }

}
