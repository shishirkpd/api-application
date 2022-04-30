package api.v1.applications

import net.logstash.logback.marker.LogstashMarker
import play.api.http.{FileMimeTypes, HttpVerbs}
import play.api.i18n.{Langs, MessagesApi}
import play.api.mvc.{ActionBuilder, AnyContent, BaseController, BodyParser, ControllerComponents, DefaultActionBuilder, MessagesRequestHeader, PlayBodyParsers, PreferredMessagesProvider, Request, RequestHeader, Result, WrappedRequest}
import play.api.{Logger, MarkerContext}

import javax.inject.Inject
import scala.concurrent.{ExecutionContext, Future}

trait ApplicationRequestHeader
  extends MessagesRequestHeader
    with PreferredMessagesProvider

class ApplicationRequest[A](request: Request[A], val messagesApi: MessagesApi)
  extends WrappedRequest(request)
    with ApplicationRequestHeader

trait RequestMarkerContext {
  import net.logstash.logback.marker.Markers

  private def marker(tuple: (String, Any)) = Markers.append(tuple._1, tuple._2)

  private implicit class RichLogstashMarker(marker1: LogstashMarker) {
    def &&(marker2: LogstashMarker): LogstashMarker = marker1.and(marker2)
  }

  implicit def requestHeaderToMarkerContext(
                                             implicit request: RequestHeader): MarkerContext = {
    MarkerContext {
      marker("id" -> request.id) && marker("host" -> request.host) && marker(
        "remoteAddress" -> request.remoteAddress)
    }
  }

}

class ApplicationActionBuilder @Inject()(messagesApi: MessagesApi,
                                         playBodyParsers: PlayBodyParsers)(
                                          implicit val executionContext: ExecutionContext)
  extends ActionBuilder[ApplicationRequest, AnyContent]
    with RequestMarkerContext
    with HttpVerbs {

  override val parser: BodyParser[AnyContent] = playBodyParsers.anyContent

  type ApplicationRequestBlock[A] = ApplicationRequest[A] => Future[Result]

  private val logger = Logger(this.getClass)

  override def invokeBlock[A](request: Request[A],
                              block: ApplicationRequestBlock[A]): Future[Result] = {
    // Convert to marker context and use request in block
    implicit val markerContext: MarkerContext = requestHeaderToMarkerContext(
      request)
    logger.trace(s"invokeBlock: ")

    val future = block(new ApplicationRequest(request, messagesApi))

    future.map { result =>
      request.method match {
        case GET | HEAD =>
          result.withHeaders("Cache-Control" -> s"max-age: 100")
        case other =>
          result
      }
    }
  }
}

case class ApplicationControllerComponents @Inject()(
                                                      applicationActionBuilder: ApplicationActionBuilder,
                                                      applicationResourceHandler: ApplicationResourceHandler,
                                                      actionBuilder: DefaultActionBuilder,
                                                      parsers: PlayBodyParsers,
                                                      messagesApi: MessagesApi,
                                                      langs: Langs,
                                                      fileMimeTypes: FileMimeTypes,
                                                      executionContext: scala.concurrent.ExecutionContext)
  extends ControllerComponents

class ApplicationBaseController @Inject()(acc: ApplicationControllerComponents)
  extends BaseController
    with RequestMarkerContext {
  override protected def controllerComponents: ControllerComponents = acc

  def ApplicationAction: ApplicationActionBuilder = acc.applicationActionBuilder

  def applicationResourceHandler: ApplicationResourceHandler = acc.applicationResourceHandler
}
