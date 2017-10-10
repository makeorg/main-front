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
                              proposals = Seq(
                                ProposalIdModel("c2cbfa62-27e4-496f-a463-f4dd045d237c"),
                                ProposalIdModel("ea77936f-d787-41c6-8e76-1ae2ac5d17bb"),
                                ProposalIdModel("beda79df-8ff6-420e-82e3-dd0814111159"),
                                ProposalIdModel("c6526d7e-b39b-4676-9d69-3ccd26660d94")
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
