package api.v1.applications

import api.v1.applications.util.JsonMapperUtil.createApplicationData
import play.api.libs.json.{Format, Json}
import play.api.{Logger, MarkerContext}

import javax.inject.{Inject, Provider}
import scala.collection.immutable.ListMap
import scala.concurrent.{ExecutionContext, Future}

/**
 * DTO for displaying Application information.
 */
case class ApplicationResource(id: String,
                               link: String,
                               casetext: String,
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

object ApplicationResource {
  /**
   * Mapping to read/write a ApplicationResource out as a JSON value.
   */
  implicit val format: Format[ApplicationResource] = Json.format
}

class ApplicationResourceHandler @Inject()(
                                            routerProvider: Provider[ApplicationRouter],
                                            applicationRepository: ApplicationRepository)(implicit
                                                                                          ec: ExecutionContext) {
  private val logger = Logger(getClass)

  def find(implicit mc: MarkerContext): Future[Iterable[ApplicationResource]] = {
    applicationRepository.list().map { applicationDataList =>
      applicationDataList.map(applicationData => createApplicationResource(applicationData))
    }
  }

  def lookup(id: Long)(
    implicit mc: MarkerContext): Future[Option[ApplicationResource]] = {
    val applicationFuture = applicationRepository.get(id)
    applicationFuture.map { maybeApplicationData =>
      maybeApplicationData.map { applicationData =>
        createApplicationResource(applicationData)
      }
    }
  }

  def create(applicationJSON: ApplicationJSON)(implicit mc: MarkerContext): Future[Long] = {
    applicationRepository.create(createApplicationData(applicationJSON))
  }

  def update(apJson: ApplicationJSON, id: Long): Future[Int]  = {
    val apData: ApplicationData = createApplicationData(apJson)
    applicationRepository.update(apData.copy(id = id))
  }

  def getWorkLoadForOfficerWithWard: Future[Map[String, Map[String, Int]]] = {
    find.map { apLis =>
      val wardAndApp: Map[String, Iterable[ApplicationResource]] = apLis.groupBy(_.caseofficer)
      val rs: Map[String, Map[String, Int]] = wardAndApp.map { kv =>
        kv._1 -> kv._2.groupBy(_.ward).map(x => (x._1, x._2.size))
      }
      logger.trace(s"${rs.toString}")
      ListMap(rs.toSeq.sortWith(_._2.size > _._2.size):_*)
    }
  }

  def getWorkLoadForOfficer(count: Option[Int] = None): Future[Map[String, Int]] = {
    find.map { apList =>
      val officerWithApp: Map[String, Int] = apList.groupBy(_.caseofficer).map(x => x._1 -> x._2.size).collect {
        case (k, v) if count.isDefined && count.get == v => (k,v)
        case (k, v) if count.isEmpty => (k,v)
      }
      logger.trace(s"${officerWithApp.toString}")
      ListMap(officerWithApp.toSeq.sortWith(_._2 > _._2):_*)
    }
  }

  def fetchCases(someKeyWord: Option[String] = None): Future[List[ApplicationResource]] = {
    find.map { apList =>
      someKeyWord match {
        case Some(value) => apList.filter(_.casetext.contains(value)).toList
        case None => apList.toList
      }
    }
  }


  private def createApplicationResource(a: ApplicationData): ApplicationResource = {
    ApplicationResource(a.id.toString, routerProvider.get.link(a.id), a.casetext, a.caseofficer, a.casereference, a
      .agentaddress, a.applicantaddress, a.agent, a.casedate, a.decisiontype, a.publicconsultationstartdate, a.ward,
      a.publicconsultationenddate, a.applicantname, a.committeearea, a.decision)
  }

}