package elkorn.playground.distributed.dashboard.input

import akka.http.scaladsl.Http
import akka.http.scaladsl.model.StatusCodes._
import akka.http.scaladsl.server.Directives._
import elkorn.playground.distributed.dashboard.CoreComponent
import elkorn.playground.distributed.dashboard.data.Parser
import elkorn.playground.distributed.dashboard.write.WriterComponent
import elkorn.playground.distributed.dashboard.write.WriterComponent.WriteResult

import scala.concurrent.Future

trait HttpApiInputComponent extends InputComponent {
  this: CoreComponent with WriterComponent =>

  private val routes = pathEndOrSingleSlash {
    get {
      complete { Future.successful(OK) }
    }
  } ~ path("input") {
    post {
      entity(as[String]) { str =>
        complete {
          (Parser.parseTrip(str).map(writer.writeTrip)
            orElse Parser.parseFare(str).map(writer.writeFare)).map(_.map(Some(_))).getOrElse(Future.successful(None)).map {
              case Some(WriteResult.Success) => OK
              case Some(WriteResult.Failure) => InternalServerError
              case None => BadRequest
            }
        }
      }
    }
  }

  override def runInput(): Unit =
    Http().bindAndHandle(routes, config.getString("http.interface"), config.getInt("http.port"))
}
