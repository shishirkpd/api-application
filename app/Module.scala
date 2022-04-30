import api.v1.applications.{ApplicationJSONLoader, ApplicationRepository, ApplicationRepositoryImpl}
import com.google.inject.AbstractModule
import net.codingwell.scalaguice.ScalaModule
import play.api.{Configuration, Environment}

import javax.inject._

/**
  * Sets up custom components for Play.
  *
  * https://www.playframework.com/documentation/latest/ScalaDependencyInjection
  */
class Module(environment: Environment, configuration: Configuration)
    extends AbstractModule
    with ScalaModule {

  override def configure(): Unit = {
    bind[ApplicationRepository].to[ApplicationRepositoryImpl].in[Singleton]()
    bind[ApplicationJSONLoader].asEagerSingleton()
  }
}
