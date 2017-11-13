package org.make.front.components

import org.make.front.facades._
import org.make.front.models.{
  BusinessConfiguration => BusinessConfigurationModel,
  GradientColor         => GradientColorModel,
  Operation             => OperationModel,
  OperationId           => OperationIdModel,
  PoliticalAction       => PoliticalActionModel,
  Sequence              => SequenceModel,
  SequenceId            => SequenceIdModel,
  TranslatedTheme       => TranslatedThemeModel,
  User                  => UserModel
}

final case class AppState(configuration: Option[BusinessConfigurationModel],
                          politicalActions: Seq[PoliticalActionModel],
                          operations: Seq[OperationModel] = Seq(
                            OperationModel(
                              operationId = OperationIdModel(OperationModel.vff),
                              url = "consultation/comment-lutter-contre-les-violences-faites-aux-femmes",
                              slug = "vff",
                              title = "Comment lutter contre les violences faites aux&nbsp;femmes&nbsp;?",
                              label = OperationModel.vff,
                              actionsCount = 0,
                              proposalsCount = 0,
                              color = "#660779",
                              gradient = Some(GradientColorModel("#AB92CA", "#54325A")),
                              tags = Seq(),
                              logoUrl = Some(vffLogo.toString),
                              darkerLogoUrl = Some(vffDarkerLogo.toString),
                              sequence = Some(
                                SequenceModel(
                                  sequenceId = SequenceIdModel("1"),
                                  slug = "comment-lutter-contre-les-violences-faites-aux-femmes",
                                  title = "Comment lutter contre les violences faites aux&nbsp;femmes&nbsp;?"
                                )
                              )
                            )
                          ),
                          bait: String = "Il faut ",
                          connectedUser: Option[UserModel],
                          country: String = "FR",
                          language: String = "fr") {

  def themes: Seq[TranslatedThemeModel] = configuration.map(_.themesForLocale(country, language)).getOrElse(Seq.empty)
  def findTheme(slug: String): Option[TranslatedThemeModel] = themes.find(_.slug == slug)

  def findOperation(slug: String): Option[OperationModel] = operations.find(_.slug == slug)

}
