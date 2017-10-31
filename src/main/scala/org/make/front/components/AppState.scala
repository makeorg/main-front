package org.make.front.components

import org.make.front.models.{
  BusinessConfiguration => BusinessConfigurationModel,
  GradientColor         => GradientColorModel,
  Operation             => OperationModel,
  OperationId           => OperationIdModel,
  PoliticalAction       => PoliticalActionModel,
  Sequence              => SequenceModel,
  SequenceId            => SequenceIdModel,
  Theme                 => ThemeModel,
  User                  => UserModel
}

final case class AppState(configuration: Option[BusinessConfigurationModel],
                          politicalActions: Seq[PoliticalActionModel],
                          operations: Seq[OperationModel] = Seq(
                            OperationModel(
                              operationId = OperationIdModel(OperationModel.vff),
                              slug = "stop-aux-violences-faites-aux-femmes",
                              title = "Comment lutter contre les violences faites aux&nbsp;femmes&nbsp;?",
                              label = OperationModel.vff,
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
                              title = "Comment lutter contre les violences faites aux&nbsp;femmes&nbsp;?"
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
