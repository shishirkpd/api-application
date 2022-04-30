package api.v1.applications

import akka.actor.ActorSystem
import play.api.db.evolutions.Evolutions
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import play.api.libs.concurrent.CustomExecutionContext
import play.api.{Logger, MarkerContext}
import slick.jdbc.JdbcProfile

import javax.inject.{Inject, Singleton}
import scala.concurrent.Future

case class ApplicationData(id: Long,
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
                           decision: String
                          )


class ApplicationExecutionContext @Inject()(actorSystem: ActorSystem)
  extends CustomExecutionContext(actorSystem, "repository.dispatcher")

trait ApplicationRepository {
  def list()(implicit mc: MarkerContext): Future[Iterable[ApplicationData]]

  def get(id: Long)(implicit mc: MarkerContext): Future[Option[ApplicationData]]

  def create(data: ApplicationData)(implicit mc: MarkerContext): Future[Long]

  def update(data: ApplicationData)(implicit mc: MarkerContext): Future[Int]
}

@Singleton
class ApplicationRepositoryImpl @Inject()(protected val dbConfigProvider: DatabaseConfigProvider)(implicit
                                                                                                  ec: ApplicationExecutionContext)
  extends ApplicationRepository with HasDatabaseConfigProvider[JdbcProfile] {

  private val logger = Logger(this.getClass)

  import profile.api._

  private val Applications = TableQuery[ApplicationsTable]

  override def list()(implicit mc: MarkerContext): Future[Iterable[ApplicationData]] = {
    logger.trace(s"list: ")
    db.run(Applications.take(50).result)
  }

  override def get(id: Long)(
    implicit mc: MarkerContext): Future[Option[ApplicationData]] = {
      logger.trace(s"get: id = $id")
      db.run(Applications.filter(_.id === id).result.headOption)
  }

  override def create(data: ApplicationData)(implicit mc: MarkerContext): Future[Long] = {
    logger.trace(s"create: data = $data")
    db.run(Applications returning Applications.map(_.id) += data)
  }

  override def update(data: ApplicationData)(implicit mc: MarkerContext): Future[Int] = {
    logger.debug(s"updating the data: $data")
    db.run(Applications.filter(_.id === data.id) update data )
  }

  private class ApplicationsTable(tag: Tag) extends Table[ApplicationData](tag, "APPLICATIONS") {
    def id = column[Long]("ID", O.PrimaryKey, O.AutoInc)

    def casetext = column[String]("CASETEXT")

    def caseofficer = column[String]("CASEOFFICER")

    def casereference = column[String]("CASEREFERENCE")

    def agentaddress = column[String]("AGENTADDRESS")

    def applicantaddress = column[String]("APPLICANTADDRESS")

    def agent = column[String]("AGENT")

    def casedate = column[String]("CASEDATE")

    def decisiontype = column[String]("DECISIONTYPE")

    def publicconsultationstartdate = column[String]("PUBLICCONSULTATIONSTARTDATE")

    def ward = column[String]("WARD")

    def publicconsultationenddate = column[String]("PUBLICCONSULTATIONENDDATE")

    def applicantname = column[String]("APPLICANTNAME")

    def committeearea = column[String]("COMMITTEEAREA")

    def decision = column[String]("DECISION")

    def * = (id, casetext, caseofficer, casereference, agentaddress, applicantaddress, agent, casedate, decisiontype,
      publicconsultationstartdate, ward, publicconsultationenddate, applicantname, committeearea, decision) <>
      (ApplicationData.tupled, ApplicationData.unapply)
  }
}
