package elkorn.playground.distributed.dashboard.input

import akka.http.scaladsl.Http
import akka.http.scaladsl.model.StatusCodes._
import akka.http.scaladsl.server.Directives._
import elkorn.playground.distributed.dashboard.CoreComponent

import scala.concurrent.Future

trait HttpApiInputComponent extends InputComponent {
  this: CoreComponent =>

  private val routes = pathPrefix("input") {
    post {
      entity(as[String]) { str =>
        complete {
          Future.successful(OK)
        }
      }
    }
  }

  override def runInput(): Unit =
    Http().bindAndHandle(routes, config.getString("http.interface"), config.getInt("http.port"))
}
