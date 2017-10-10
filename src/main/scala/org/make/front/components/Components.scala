package org.make.front.components

import io.github.shogowada.scalajs.reactjs.VirtualDOM.VirtualDOMAttributes.Type.AS_IS
import io.github.shogowada.scalajs.reactjs.VirtualDOM.VirtualDOMElements.ReactClassElementSpec
import io.github.shogowada.scalajs.reactjs.VirtualDOM.{VirtualDOMAttributes, VirtualDOMElements}
import io.github.shogowada.statictags.{Attribute, BooleanAttributeSpec, SpaceSeparatedStringAttributeSpec}

import scala.scalajs.js
import scalacss.StyleA

object Components {
  implicit class RichSpaceSeparatedStringAttributeSpec(val spec: SpaceSeparatedStringAttributeSpec) extends AnyVal {
    def :=(style: StyleA): Attribute[Iterable[String]] = spec := style.htmlClass
    def :=(styleSeq: Seq[StyleA]): Attribute[Iterable[String]] = spec := styleSeq.map(_.htmlClass)
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

    def AppComponent: ReactClassElementSpec = self(org.make.front.components.App.reactClass)

    def SpinnerComponent: ReactClassElementSpec = self(spinner.Spinner.reactClass)

    def ContainerComponent: ReactClassElementSpec = self(org.make.front.components.Container.reactClass)
    def NotificationsComponent: ReactClassElementSpec = self(notifications.Notifications.reactClass)
    def MainHeaderComponent: ReactClassElementSpec = self(mainHeader.MainHeader.reactClass)
    def MainFooterComponent: ReactClassElementSpec = self(mainFooter.MainFooter.reactClass)
    def NavInThemesContainerComponent: ReactClassElementSpec = self(navInThemes.NavInThemesContainer.reactClass)
    def UserNavComponent: ReactClassElementSpec = self(userNav.UserNav.reactClass)
    def UserNavContainerComponent: ReactClassElementSpec = self(userNav.UserNavContainer.reactClass)

    def SearchFormComponent: ReactClassElementSpec = self(search.SearchForm.reactClass)
    def SearchResultsContainerComponent: ReactClassElementSpec = self(search.SearchResultsContainer.reactClass)
    def NoResultToSearchComponent: ReactClassElementSpec = self(search.NoResultToSearch.reactClass)

    def ModalComponent: ReactClassElementSpec = self(modals.Modal.reactClass)
    def FullscreenModalComponent: ReactClassElementSpec = self(modals.FullscreenModal.reactClass)

    /*********************/
    def ActivateAccountContainerComponent: ReactClassElementSpec =
      self(activateAccount.ActivateAccountContainer.reactClass)
    def RecoverPasswordContainerComponent: ReactClassElementSpec =
      self(authenticate.recoverPassword.RecoverPasswordContainer.reactClass)
    def ResetPasswordContainerComponent: ReactClassElementSpec =
      self(authenticate.resetPassword.ResetPasswordContainer.reactClass)
    def LoginOrRegisterComponent: ReactClassElementSpec = self(authenticate.LoginOrRegister.reactClass)
    def AuthenticateWithSocialNetworksComponent: ReactClassElementSpec =
      self(authenticate.AuthenticateWithSocialNetworksContainer.reactClass)
    def RequireAuthenticatedUserComponent: ReactClassElementSpec =
      self(users.authenticate.RequireAuthenticatedUserContainer.reactClass)
    def RegisterWithSocialNetworksOrEmailComponent: ReactClassElementSpec =
      self(authenticate.register.RegisterWithSocialNetworksOrEmail.regular)
    def RegisterWithSocialNetworksOrEmailExpandedComponent: ReactClassElementSpec =
      self(authenticate.register.RegisterWithSocialNetworksOrEmail.expanded)
    def RegisterWithEmailComponent: ReactClassElementSpec =
      self(authenticate.register.RegisterContainer.registerWithEmailReactClass)
    def RegisterWithEmailExpandedComponent: ReactClassElementSpec =
      self(authenticate.register.RegisterContainer.registerWithEmailExpandedReactClass)
    def LoginWithEmailComponent: ReactClassElementSpec = self(authenticate.login.LoginWithEmailContainer.reactClass)
    def LoginWithEmailOrSocialNetworksComponent: ReactClassElementSpec =
      self(authenticate.login.LoginWithEmailOrSocialNetworks.reactClass)
    def NewPasswordInputComponent: ReactClassElementSpec = self(authenticate.NewPasswordInput.reactClass)

    /*********************/
    def SubmitProposalComponent: ReactClassElementSpec = self(submitProposal.SubmitProposal.reactClass)
    def ConfirmationOfProposalSubmissionComponent: ReactClassElementSpec =
      self(submitProposal.ConfirmationOfProposalSubmission.reactClass)
    def SubmitProposalFormComponent: ReactClassElementSpec = self(submitProposal.SubmitProposalFormContainer.reactClass)
    def SubmitProposalAndLoginComponent: ReactClassElementSpec =
      self(submitProposal.SubmitProposalAndLoginContainer.reactClass)

    /*********************/
    def ProposalInfosComponent: ReactClassElementSpec = self(proposal.ProposalInfos.reactClass)
    def ShareOwnProposalComponent: ReactClassElementSpec = self(proposal.ShareOwnProposal.reactClass)
    def ProposalComponent: ReactClassElementSpec = self(proposal.Proposal.reactClass)
    def ProposalWithThemeContainerComponent: ReactClassElementSpec =
      self(proposal.ProposalWithThemeContainer.reactClass)
    def ProposalWithThemeComponent: ReactClassElementSpec = self(proposal.ProposalWithTheme.reactClass)
    def ProposalWithTagsComponent: ReactClassElementSpec = self(proposal.ProposalWithTags.reactClass)

    def TrendingShowcaseContainerComponent: ReactClassElementSpec = self(showcase.TrendingShowcaseContainer.reactClass)
    def ThemeShowcaseContainerComponent: ReactClassElementSpec = self(showcase.ThemeShowcaseContainer.reactClass)
    def SequenceContainerComponent: ReactClassElementSpec = self(sequence.SequenceContainer.reactClass)
    def OperationSequenceComponent: ReactClassElementSpec = self(operation.OperationSequence.reactClass)
    def OperationSequenceContainerComponent: ReactClassElementSpec =
      self(operation.OperationSequenceContainer.reactClass)

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
    def ThemeHeaderComponent: ReactClassElementSpec = self(theme.ThemeHeader.reactClass)
    def SubmitProposalInRelationToThemeComponent: ReactClassElementSpec =
      self(theme.SubmitProposalInRelationToTheme.reactClass)
    def ResultsInThemeContainerComponent: ReactClassElementSpec = self(theme.ResultsInThemeContainer.reactClass)
    def ResultsInThemeComponent: ReactClassElementSpec = self(theme.ResultsInTheme.reactClass)

    /*********************/
    def VFFIntroComponent: ReactClassElementSpec = self(operation.VFFIntro.reactClass)
    def OperationHeaderComponent: ReactClassElementSpec = self(operation.OperationHeader.reactClass)
    def SubmitProposalInRelationToOperationComponent: ReactClassElementSpec =
      self(operation.SubmitProposalInRelationToOperation.reactClass)
    def ResultsInOperationContainerComponent: ReactClassElementSpec =
      self(operation.ResultsInOperationContainer.reactClass)
    def ResultsInOperationComponent: ReactClassElementSpec = self(operation.ResultsInOperation.reactClass)

    /*********************/
    def HomeComponent: ReactClassElementSpec = self(home.Home.reactClass)
    def IntroComponent: ReactClassElementSpec = self(home.Intro.reactClass)
    def ExplanationsComponent: ReactClassElementSpec = self(home.Explanations.reactClass)
  }

}
