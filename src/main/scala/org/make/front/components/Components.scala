package org.make.front.components

import io.github.shogowada.scalajs.reactjs.VirtualDOM.VirtualDOMAttributes.Type.AS_IS
import io.github.shogowada.scalajs.reactjs.VirtualDOM.VirtualDOMElements.ReactClassElementSpec
import io.github.shogowada.scalajs.reactjs.VirtualDOM.{VirtualDOMAttributes, VirtualDOMElements}
import io.github.shogowada.statictags.{Attribute, SpaceSeparatedStringAttributeSpec}
import org.make.front.components.users.authenticate.login.{LoginWithEmailContainer, LoginWithEmailOrSocialNetworks}
import org.make.front.components.users.authenticate.register.{RegisterContainer, RegisterWithSocialNetworksOrEmail}
import org.make.front.components.users.authenticate.{AuthenticateWithSocialNetworksContainer, LoginOrRegister}
import org.make.front.components.containers.RecoverPasswordContainer
import org.make.front.components.users.resetPassword.ResetPasswordContainer
import org.make.front.components.users.userNav.{UserNav, UserNavContainer}

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

  implicit class TranslateVirtualDOMAttributes(attributes: VirtualDOMAttributes) {
    lazy val dangerouslySetInnerHTML: DangerouslySetInnerHtmlParameterSpec = new DangerouslySetInnerHtmlParameterSpec
  }

  implicit class RichVirtualDOMElements(val self: VirtualDOMElements) extends AnyVal {

    def AppComponent: ReactClassElementSpec =
      self(org.make.front.components.App.reactClass)

    def ContainerComponent: ReactClassElementSpec = self(org.make.front.components.ContainerComponent.reactClass)
    def MainHeaderComponent: ReactClassElementSpec = self(mainHeader.MainHeader.reactClass)
    def MainFooterComponent: ReactClassElementSpec = self(mainFooter.MainFooter.reactClass)
    def UserNavComponent: ReactClassElementSpec = self(UserNav.reactClass)
    def SearchFormComponent: ReactClassElementSpec = self(search.SearchForm.reactClass)
    def HomeComponent: ReactClassElementSpec = self(home.Home.reactClass)
    def MainIntroComponent: ReactClassElementSpec = self(home.MainIntro.reactClass)

    def ModalComponent: ReactClassElementSpec = self(modals.Modal.reactClass)
    def FullscreenModalComponent: ReactClassElementSpec = self(modals.FullscreenModal.reactClass)

    def TagComponent: ReactClassElementSpec = self(tags.Tag.reactClass)

    def TagsListComponent: ReactClassElementSpec = self(tags.TagsListComponent.reactClass)

    def FilterByTagsComponent: ReactClassElementSpec = self(tags.FilterByTags.reactClass)

    def ThemeHeaderComponent: ReactClassElementSpec = self(theme.ThemeHeader.reactClass)

    def SubmitProposalContainerComponent: ReactClassElementSpec =
      self(submitProposal.SubmitProposalContainer.reactClass)

    def SubmitProposalInRelationToThemeComponent: ReactClassElementSpec =
      self(submitProposal.SubmitProposalInRelationToTheme.reactClass)

    def PoliticalActionsContainerComponent: ReactClassElementSpec =
      self(politicalActions.PoliticalActionsContainer.reactClass)

    def PoliticalActionComponent: ReactClassElementSpec =
      self(politicalActions.PoliticalAction.reactClass)

    def VoteComponent: ReactClassElementSpec = self(proposals.vote.Vote.reactClass)

    def VoteButtonComponent: ReactClassElementSpec = self(proposals.vote.VoteButton.reactClass)

    def QualificateVoteButtonComponent: ReactClassElementSpec =
      self(proposals.vote.QualificateVoteButton.reactClass)

    def QualificateVoteComponent: ReactClassElementSpec =
      self(proposals.vote.QualificateVote.reactClass)

    def NotificationContainerComponent: ReactClassElementSpec =
      self(notification.NotificationContainer.reactClass)

    def UserNavContainerComponent: ReactClassElementSpec =
      self(UserNavContainer.reactClass)

    def ProposalsContainerComponent: ReactClassElementSpec =
      self(proposals.ProposalsListContainer.reactClass)

    def ProposalInfosComponent: ReactClassElementSpec =
      self(proposals.proposal.ProposalInfos.reactClass)

    def ProposalComponent: ReactClassElementSpec =
      self(proposals.proposal.Proposal.reactClass)

    def ProposalWithThemeContainerComponent: ReactClassElementSpec =
      self(proposals.proposal.ProposalWithThemeContainer.reactClass)

    def ProposalWithThemeComponent: ReactClassElementSpec =
      self(proposals.proposal.ProposalWithTheme.reactClass)

    def ProposalWithTagsComponent: ReactClassElementSpec =
      self(proposals.proposal.ProposalWithTags.reactClass)

    def NavInThemesContainerComponent: ReactClassElementSpec =
      self(navInThemes.NavInThemesContainer.reactClass)

    def RecoverPasswordContainerComponent: ReactClassElementSpec =
      self(RecoverPasswordContainer.reactClass)

    def ResetPasswordContainerComponent: ReactClassElementSpec =
      self(ResetPasswordContainer.reactClass)
    def ShowcaseContainerComponent: ReactClassElementSpec =
      self(showcase.ShowcaseContainer.reactClass)

    def ActivateAccountContainerComponent: ReactClassElementSpec =
      self(activateAccount.ActivateAccountContainer.reactClass)

    def AuthenticateWithSocialNetworksComponent: ReactClassElementSpec =
      self(AuthenticateWithSocialNetworksContainer.reactClass)

    def RegisterWithEmailComponent: ReactClassElementSpec = self(RegisterContainer.registerWithEmailReactClass)
    def RegisterWithEmailExpandedComponent: ReactClassElementSpec =
      self(RegisterContainer.registerWithEmailExpandedReactClass)

    def LoginWithEmailComponent: ReactClassElementSpec = self(LoginWithEmailContainer.reactClass)

    def LoginWithEmailOrSocialNetworksComponent: ReactClassElementSpec = self(LoginWithEmailOrSocialNetworks.reactClass)

    def RegisterWithSocialNetworksOrEmailComponent: ReactClassElementSpec =
      self(RegisterWithSocialNetworksOrEmail.regular)

    def RegisterWithSocialNetworksOrEmailExpandedComponent: ReactClassElementSpec =
      self(RegisterWithSocialNetworksOrEmail.expanded)

    def LoginOrRegisterComponent: ReactClassElementSpec =
      self(LoginOrRegister.reactClass)

    def ViewablePasswordComponent: ReactClassElementSpec =
      self(ViewablePassword.reactClass)
  }

}
