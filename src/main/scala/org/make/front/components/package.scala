package org.make.front.components

import io.github.shogowada.scalajs.reactjs.VirtualDOM.VirtualDOMElements
import io.github.shogowada.scalajs.reactjs.VirtualDOM.VirtualDOMElements.ReactClassElementSpec
import io.github.shogowada.statictags.{Attribute, SpaceSeparatedStringAttributeSpec}

import scalacss.StyleA

package object presentationals {
  implicit class RichSpaceSeparatedStringAttributeSpec(val spec: SpaceSeparatedStringAttributeSpec) extends AnyVal {
    def :=(style: StyleA): Attribute[Iterable[String]] = spec := style.htmlClass
    def :=(styleSeq: Seq[StyleA]): Attribute[Iterable[String]] = spec := styleSeq.map(_.htmlClass)
  }

  implicit class RichVirtualDOMElements(val self: VirtualDOMElements) extends AnyVal {
    // Components

    def ContainerComponent: ReactClassElementSpec = self(org.make.front.components.ContainerComponent.reactClass)
    def MainHeaderComponent: ReactClassElementSpec = self(MainHeader.MainHeaderComponent.reactClass)
    def MainFooterComponent: ReactClassElementSpec = self(MainFooter.MainFooterComponent.reactClass)
    def UserNavComponent: ReactClassElementSpec = self(UserNav.UserNavComponent.reactClass)
    def SearchFormComponent: ReactClassElementSpec = self(Search.SearchFormComponent.reactClass)
    def HomeComponent: ReactClassElementSpec = self(Home.HomeComponent.reactClass)
    def MainIntroComponent: ReactClassElementSpec = self(Home.MainIntroComponent.reactClass)

    def ModalComponent: ReactClassElementSpec = self(Modal.ModalComponent.reactClass)
    def FullscreenModalComponent: ReactClassElementSpec = self(Modal.FullscreenModalComponent.reactClass)

    def TagComponent: ReactClassElementSpec = self(Tags.TagComponent.reactClass)
    def TagsListComponent: ReactClassElementSpec = self(Tags.TagsListComponent.reactClass)
    def FilterByTagsComponent: ReactClassElementSpec = self(Tags.FilterByTagsComponent.reactClass)

    def ThemeHeaderComponent: ReactClassElementSpec = self(Theme.ThemeHeaderComponent.reactClass)
    def ConnectUserComponent: ReactClassElementSpec = self(ConnectUser.ConnectUserComponent.reactClass)

    def ProposalComponent: ReactClassElementSpec = self(Proposals.ProposalComponent.reactClass)
    def ProposalsListComponent: ReactClassElementSpec = self(Proposals.ProposalsListComponent.reactClass)

    def SubmitProposalFormContainerComponent: ReactClassElementSpec =
      self(SubmitProposal.SubmitProposalFormContainerComponent.reactClass)
    def SubmitProposalInRelationToThemeComponent: ReactClassElementSpec =
      self(SubmitProposal.SubmitProposalInRelationToThemeComponent.reactClass)

    def VoteComponent: ReactClassElementSpec = self(Vote.VoteComponent.reactClass)
    def VoteButtonComponent: ReactClassElementSpec = self(presentationals.VoteButtonComponent.reactClass)
    def QualificateVoteComponent: ReactClassElementSpec =
      self(Vote.QualificateVoteComponent.reactClass)

    def AppComponent: ReactClassElementSpec =
      self(org.make.front.components.AppComponent.reactClass)
    def NotificationContainerComponent: ReactClassElementSpec =
      self(Notification.NotificationContainerComponent.reactClass)
    def UserNavContainerComponent: ReactClassElementSpec =
      self(UserNav.UserNavContainerComponent.reactClass)
    def ConnectUserContainerComponent: ReactClassElementSpec =
      self(ConnectUser.ConnectUserContainerComponent.reactClass)
    def ProposalsListContainerComponent: ReactClassElementSpec =
      self(Proposals.ProposalsListContainerComponent.reactClass)
    def NavInThemesContainerComponent: ReactClassElementSpec =
      self(NavInThemes.NavInThemesContainer.reactClass)
    def PoliticalActionsComponent: ReactClassElementSpec =
      self(PoliticalAction.PoliticalActionContainerComponent.reactClass)

    def PasswordRecoveryContainerComponent: ReactClassElementSpec =
      self(containers.PasswordRecoveryContainerComponent.reactClass)
    def PasswordResetContainerComponent: ReactClassElementSpec =
      self(ResetPassword.PasswordResetContainerComponent.reactClass)
    def ShowcaseContainerComponent: ReactClassElementSpec =
      self(Showcase.ShowcaseContainerComponent.reactClass)
    def ActivateAccountContainerComponent: ReactClassElementSpec =
      self(ActivateAccount.ActivateAccountContainerComponent.reactClass)
  }
}
