package elkorn.playground.distributed.dashboard.write

import akka.actor.Props
import akka.persistence.PersistentActor
import elkorn.playground.distributed.dashboard.CoreComponent
import elkorn.playground.distributed.dashboard.data.{ Fare, Trip }
import elkorn.playground.distributed.dashboard.write.WriterComponent.WriteResult

import scala.concurrent.Future
import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global
import akka.pattern.ask
import akka.util.Timeout

private object WriterActor {
  def props() = Props(classOf[WriterActor])
}

private class WriterActor extends PersistentActor {
  override def receiveRecover: Receive = {
    case x => println(s"Received recover $x")
  }

  override def receiveCommand: Receive = {
    case trip: Trip => persist(trip) { _ =>
      sender() ! WriteResult.Success
    }

    case fare: Fare => persist(fare) { _ =>
      sender() ! WriteResult.Success
    }
  }

  override def persistenceId: String = "Writer"
}

trait PersistentActorWriterComponent extends WriterComponent {
  this: CoreComponent =>

  private implicit val timeout = Timeout(5 seconds)

  lazy val writer = new Writer {
    private val actor = system.actorOf(WriterActor.props())
    override def writeFare(fare: Fare): Future[WriteResult] =
      (actor ? fare).mapTo[WriteResult]

    override def writeTrip(trip: Trip): Future[WriteResult] =
      (actor ? trip).mapTo[WriteResult]
  }

}
