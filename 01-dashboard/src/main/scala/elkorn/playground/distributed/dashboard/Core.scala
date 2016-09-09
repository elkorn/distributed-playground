package elkorn.playground.distributed.dashboard

import akka.actor.ActorSystem
import akka.event.{ Logging, LoggingAdapter }
import akka.stream.ActorMaterializer
import com.typesafe.config.{ Config, ConfigFactory }

import scala.concurrent.ExecutionContextExecutor

trait CoreComponent {
  implicit def system: ActorSystem
  implicit def executor: ExecutionContextExecutor
  implicit def materializer: ActorMaterializer

  def config: Config
  def logger: LoggingAdapter
}

trait DefaultCoreComponent extends CoreComponent {
  override implicit lazy val system: ActorSystem = ActorSystem()

  override implicit lazy val executor: ExecutionContextExecutor = system.dispatcher

  override implicit lazy val materializer: ActorMaterializer = ActorMaterializer()

  override lazy val config: Config = ConfigFactory.load()

  override lazy val logger: LoggingAdapter = Logging(system, getClass)
}
