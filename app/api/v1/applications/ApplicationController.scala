package api.v1.applications

import com.google.inject.Inject
import play.api.Logger
import play.api.libs.json.Json
import play.api.mvc.{Action, AnyContent}

import scala.concurrent.{ExecutionContext, Future}

class ApplicationController @Inject()(cc: ApplicationControllerComponents)(
  implicit ec: ExecutionContext) extends ApplicationBaseController(cc) {

  private val logger = Logger(getClass)

  implicit val appJsonFormat = Json.format[ApplicationJSON]

  def index: Action[AnyContent] = ApplicationAction.async { implicit request =>
    logger.trace("index: ")
    applicationResourceHandler.find.map { applications =>
      Ok(Json.toJson(applications))
    }
  }

  def show(id: Long): Action[AnyContent] = ApplicationAction.async {
    implicit request =>
      logger.trace(s"show: id = $id")
      applicationResourceHandler.lookup(id).map { application =>
        Ok(Json.toJson(application))
      }
  }

  def create: Action[AnyContent]= ApplicationAction.async { implicit request =>
    logger.trace(s"inserting the data")
    val json = request.body.asJson.get
    applicationResourceHandler.create(json.as[ApplicationJSON]).map { id =>
      Ok(Json.toJson(s"data inserted with id = $id"))
    }
  }

  def update(id: Long): Action[AnyContent] = ApplicationAction.async { implicit request =>
    logger.trace(s"updating the data for id: $id")
    val json = request.body.asJson.get
    val apJson = json.as[ApplicationJSON]

    applicationResourceHandler.lookup(id).map {
      case None => Future(s"No data found with id = $id")
      case _ => applicationResourceHandler.update(apJson, id).map { a =>
        s"updated data for id: $id"
      }
    }.flatten.map { str =>
        Ok(Json.toJson(s"$str"))
    }
  }

  def workloadForOfficer(count: Option[Int] = None) : Action[AnyContent] = ApplicationAction.async { implicit req =>
    logger.trace(s"getting the work load")

    applicationResourceHandler.getWorkLoadForOfficer(count).map { wl =>
      Ok(Json.toJson(wl))
    }
  }

  def workloadForOfficerWithWard: Action[AnyContent] = ApplicationAction.async { implicit req =>
    logger.trace(s"getting the work load")

    applicationResourceHandler.getWorkLoadForOfficerWithWard.map { wl =>
      Ok(Json.toJson(wl))
    }
  }

  def fetchCases(someKeyWord: Option[String] = None): Action[AnyContent] = ApplicationAction.async { implicit req =>
    logger.trace(s"getting the cases for ${someKeyWord.getOrElse("")}")

    applicationResourceHandler.fetchCases(someKeyWord).map {
      case Nil =>  Ok(Json.toJson(s"No cases found with keyword: ${someKeyWord.getOrElse("")}"))
      case listOfValue =>  Ok(Json.toJson(listOfValue))
    }
  }
}

