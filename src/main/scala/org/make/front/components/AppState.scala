package org.make.front.components

import org.make.front.models.{
  SequenceId            => SequenceIdModel,
  Operation             => OperationModel,
  Sequence              => SequenceModel,
  BusinessConfiguration => BusinessConfigurationModel,
  PoliticalAction       => PoliticalActionModel,
  OperationId           => OperationIdModel,
  GradientColor         => GradientColorModel,
  ProposalId            => ProposalIdModel,
  User                  => UserModel,
  Theme                 => ThemeModel
}

final case class AppState(configuration: Option[BusinessConfigurationModel],
                          politicalActions: Seq[PoliticalActionModel],
                          operations: Seq[OperationModel] = Seq(
                            OperationModel(
                              operationId = OperationIdModel("1"),
                              slug = "vff",
                              title = "Comment lutter contre les violences faites aux&nbsp;femmes&nbsp;?",
                              actionsCount = 0,
                              proposalsCount = 0,
                              color = "#660779",
                              gradient = Some(GradientColorModel("#AB92CA", "#54325A")),
                              tags = Seq()
                            )
                          ),
                          sequences: Seq[SequenceModel] = Seq(
                            SequenceModel(
                              sequenceId = SequenceIdModel("1"),
                              slug = "comment-lutter-contre-les-violences-faites-aux-femmes",
                              title = "Comment lutter contre les violences faites aux&nbsp;femmes&nbsp;?",
                              proposalsSlugs = Seq(
                                "il-faut-generaliser-les-voitures-en-libre-service-partout-sur-le-territoire-afin-que-les-gens-n-aient-plus-besoin-de-posseder-un-vehicule",
                                "il-faut-rendre-le-recyclage-plus-simple-selon-les-communes-tel-ou-tel-dechet-est-accepte-ou-refuse-on-ne-s-y-retrouve-plus-on-abandonne",
                                "il-faut-disposer-des-machines-de-recyclage-du-plastique-qui-permettraient-de-creer-des-objets-par-impression-3d",
                                "il-faut-developper-les-machines-sportives-reliees-a-une-batterie-pour-produire-son-electricite-sport-energie-motivation"
                              )
                            )
                          ),
                          bait: String = "Il faut ",
                          connectedUser: Option[UserModel],
                          country: String = "FR",
                          language: String = "fr") {

  def themes: Seq[ThemeModel] = configuration.map(_.themesForLocale(country, language)).getOrElse(Seq.empty)
  def findTheme(slug: String): Option[ThemeModel] = themes.find(_.slug == slug)

  def findOperation(slug: String): Option[OperationModel] = operations.find(_.slug == slug)

}
