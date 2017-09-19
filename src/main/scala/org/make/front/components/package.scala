package org.make.front.components

import io.github.shogowada.scalajs.reactjs.VirtualDOM.VirtualDOMAttributes.Type.AS_IS
import io.github.shogowada.scalajs.reactjs.VirtualDOM.VirtualDOMElements.ReactClassElementSpec
import io.github.shogowada.scalajs.reactjs.VirtualDOM.{VirtualDOMAttributes, VirtualDOMElements}
import io.github.shogowada.statictags.{Attribute, SpaceSeparatedStringAttributeSpec}
import org.make.front.components.authenticate.AuthenticateWithSocialNetworksContainer

import scala.scalajs.js
import scalacss.StyleA

package object presentationals {
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
    def UserNavComponent: ReactClassElementSpec = self(userNav.UserNav.reactClass)
    def SearchFormComponent: ReactClassElementSpec = self(search.SearchForm.reactClass)
    def HomeComponent: ReactClassElementSpec = self(home.Home.reactClass)
    def MainIntroComponent: ReactClassElementSpec = self(home.MainIntro.reactClass)

    def ModalComponent: ReactClassElementSpec = self(modals.Modal.reactClass)
    def FullscreenModalComponent: ReactClassElementSpec = self(modals.FullscreenModal.reactClass)

    def TagComponent: ReactClassElementSpec = self(tags.Tag.reactClass)
    def TagsListComponent: ReactClassElementSpec = self(tags.TagsListComponent.reactClass)
    def FilterByTagsComponent: ReactClassElementSpec = self(tags.FilterByTags.reactClass)

    def ThemeHeaderComponent: ReactClassElementSpec = self(theme.ThemeHeader.reactClass)
    def ConnectUserComponent: ReactClassElementSpec = self(connectUser.ConnectUser.reactClass)

    def SubmitProposalContainerComponent: ReactClassElementSpec =
      self(submitProposal.SubmitProposalContainer.reactClass)

    def SubmitProposalInRelationToThemeComponent: ReactClassElementSpec =
      self(submitProposal.SubmitProposalInRelationToTheme.reactClass)

    def PoliticalActionsContainerComponent: ReactClassElementSpec =
      self(politicalActions.PoliticalActionsContainer.reactClass)

    def PoliticalActionComponent: ReactClassElementSpec =
      self(politicalActions.PoliticalAction.reactClass)

    def VoteComponent: ReactClassElementSpec = self(vote.Vote.reactClass)
    def VoteButtonComponent: ReactClassElementSpec = self(presentationals.VoteButton.reactClass)
    def QualificateVoteComponent: ReactClassElementSpec =
      self(vote.QualificateVote.reactClass)

    def NotificationContainerComponent: ReactClassElementSpec =
      self(notification.NotificationContainer.reactClass)
    def UserNavContainerComponent: ReactClassElementSpec =
      self(userNav.UserNavContainer.reactClass)
    def ConnectUserContainerComponent: ReactClassElementSpec =
      self(connectUser.ConnectUserContainer.reactClass)
    def ProposalsContainerComponent: ReactClassElementSpec =
      self(proposals.ProposalsListContainer.reactClass)
    def ProposalComponent: ReactClassElementSpec =
      self(proposals.Proposal.reactClass)

    def NavInThemesContainerComponent: ReactClassElementSpec =
      self(navInThemes.NavInThemesContainer.reactClass)

    def PasswordRecoveryContainerComponent: ReactClassElementSpec =
      self(containers.PasswordRecoveryContainer.reactClass)
    def PasswordResetContainerComponent: ReactClassElementSpec =
      self(resetPassword.PasswordResetContainer.reactClass)
    def ShowcaseContainerComponent: ReactClassElementSpec =
      self(showcase.ShowcaseContainer.reactClass)
    def ActivateAccountContainerComponent: ReactClassElementSpec =
      self(activateAccount.ActivateAccountContainer.reactClass)

    def AuthenticateWithSocialNetworksComponent: ReactClassElementSpec =
      self(AuthenticateWithSocialNetworksContainer.reactClass)
  }

}
