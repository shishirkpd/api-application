package api.v1.applications

import api.v1.applications.util.JsonMapperUtil.createApplicationData
import com.google.inject.Inject
import play.api.Logger
import play.api.db.evolutions.ApplicationEvolutions
import play.api.libs.functional.syntax.toFunctionalBuilderOps
import play.api.libs.json.{JsPath, Json, Reads}

import scala.concurrent.ExecutionContext
import scala.reflect.io.File
import scala.util.{Failure, Success}

case class ApplicationJSON(casetext: String,
                           caseofficer: String,
                           casereference: String,
                           agentaddress: String,
                           applicantaddress: String,
                           agent: String,
                           casedate: String,
                           decisiontype: String,
                           publicconsultationstartdate: String,
                           ward: String,
                           publicconsultationenddate: String,
                           applicantname: String,
                           committeearea: String,
                           decision: String)


object ApplicationJSON {
  implicit val applcationReads: Reads[ApplicationJSON] = (
    (JsPath \ "CASETEXT").read[String] and
      (JsPath \ "CASEOFFICER").read[String] and
      (JsPath \ "CASEREFERENCE").read[String] and
      (JsPath \ "AGENTADDRESS").read[String] and
      (JsPath \ "APPLICANTADDRESS").read[String] and
      (JsPath \ "AGENT").read[String] and
      (JsPath \ "CASEDATE").read[String] and
      (JsPath \ "DECISIONTYPE").read[String] and
      (JsPath \ "PUBLICCONSULTATIONSTARTDATE").read[String] and
      (JsPath \ "WARD").read[String] and
      (JsPath \ "PUBLICCONSULTATIONENDDATE").read[String] and
      (JsPath \ "APPLICANTNAME").read[String] and
      (JsPath \ "COMMITTEEAREA").read[String] and
      (JsPath \ "DECISION").read[String]
    ) (ApplicationJSON.apply _)
}


class ApplicationJSONLoader @Inject()(
                                       applicationRepository: ApplicationRepository,
                                       applicationEvolutions: ApplicationEvolutions)(
                                       implicit ec: ExecutionContext
                                     ) {

  private val logger = Logger(this.getClass)

  // Wait until evolutions is applied to load the applications
  if (applicationEvolutions.upToDate) {
    logger.debug("Loading applications data...")
    loadApplications()
  } else {
    logger.warn("Evolutions is not up to date, applications won't be loaded yet!")
  }


  private def loadApplications(): Unit = {
    val jsonFile = File("./data/planning-applications-weekly-list.json")
    val json = Json.parse(jsonFile.inputStream())
    val result = json.as[List[ApplicationJSON]]

    result
      .map(applicationJson => createApplicationData(applicationJson))
      .foreach(applicationData => applicationRepository.create(applicationData)
        .onComplete {
          case Success(value) => logger.debug(s"Created application id: ${value}")
          case Failure(t) => logger.error(s"Error creating application: ${applicationData}: ${t.getMessage}")
        })
  }
}


