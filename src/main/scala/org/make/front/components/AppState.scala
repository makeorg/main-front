package org.make.front.components

import org.make.front.facades._
import org.make.front.models.{
  Tag                   => TagModel,
  TagId                 => TagIdModel,
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
                              url = "consultation/vff/selection",
                              slug = "vff",
                              title = "Comment lutter contre les violences faites aux&nbsp;femmes&nbsp;?",
                              label = OperationModel.vff,
                              actionsCount = 0,
                              proposalsCount = 0,
                              color = "#660779",
                              gradient = Some(GradientColorModel("#AB92CA", "#54325A")),
                              tags = Seq(
                                TagModel(tagId = TagIdModel("signalement"), label = "signalement"),
                                TagModel(tagId = TagIdModel("police-justice"), label = "police & justice"),
                                TagModel(tagId = TagIdModel("monde-du-travail"), label = "monde du travail"),
                                TagModel(tagId = TagIdModel("transports"), label = "transports"),
                                TagModel(tagId = TagIdModel("action-publique"), label = "action publique"),
                                TagModel(tagId = TagIdModel("hebergement"), label = "hébergement"),
                                TagModel(
                                  tagId = TagIdModel("education-sensibilisation"),
                                  label = "éducation & sensibilisation"
                                ),
                                TagModel(tagId = TagIdModel("soutien-psychologique"), label = "soutien psychologique"),
                                TagModel(
                                  tagId = TagIdModel("independance-financiere"),
                                  label = "indépendance financière"
                                ),
                                TagModel(tagId = TagIdModel("agissements-sexistes"), label = "agissements sexistes"),
                                TagModel(tagId = TagIdModel("mutilations"), label = "mutilations"),
                                TagModel(tagId = TagIdModel("violences-sexuelles"), label = "violences sexuelles"),
                                TagModel(tagId = TagIdModel("harcelement"), label = "harcèlement"),
                                TagModel(tagId = TagIdModel("traditions-nefastes"), label = "traditions néfastes"),
                                TagModel(tagId = TagIdModel("image-de-la-femme"), label = "image de la femme"),
                                TagModel(tagId = TagIdModel("violences-conjugales"), label = "violences conjugales"),
                                TagModel(tagId = TagIdModel("prevention"), label = "prévention"),
                                TagModel(tagId = TagIdModel("protection"), label = "protection"),
                                TagModel(tagId = TagIdModel("reponses"), label = "réponses")
                              ),
                              logoUrl = Some(vffLogo.toString),
                              darkerLogoUrl = Some(vffDarkerLogo.toString),
                              sequence = Some(
                                SequenceModel(
                                  sequenceId = SequenceIdModel("1"),
                                  slug = "comment-lutter-contre-les-violences-faites-aux-femmes",
                                  title = "Comment lutter contre les violences faites aux&nbsp;femmes&nbsp;?",
                                  proposals = Seq.empty
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
