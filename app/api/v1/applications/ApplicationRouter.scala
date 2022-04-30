package api.v1.applications

import com.google.inject.Inject
import play.api.routing.Router.Routes
import play.api.routing.SimpleRouter
import play.api.routing.sird._

class ApplicationRouter @Inject()(controller: ApplicationController) extends SimpleRouter {
  val prefix = "/api/v1/application"

  def link(id: Long): String = {
    import io.lemonlabs.uri.dsl._
    val url = prefix / id.toString
    url.toString()
  }

  override def routes: Routes = {
    case GET(p"/") => controller.index()

    case GET(p"/workload/officer/ward") => controller.workloadForOfficerWithWard

    case GET(p"/workload/officer") => controller.workloadForOfficer()

    case GET(p"/workload/officer/$count") => controller.workloadForOfficer(Option(count.toInt))

    case GET(p"/case") => controller.fetchCases()

    case GET(p"/case/$word") => controller.fetchCases(Option(word))

    case GET(p"/$id") =>
      controller.show(id.toLong)

    case POST(p"/") => controller.create

    case POST(p"/$id") => controller.update(id.toLong)

  }

}
