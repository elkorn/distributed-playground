package elkorn.playground.distributed.dashboard

import elkorn.playground.distributed.dashboard.input.HttpApiInputComponent
import elkorn.playground.distributed.dashboard.write.PersistentActorWriterComponent

object DashboardApp extends App {
  val input = new HttpApiInputComponent with DefaultCoreComponent with PersistentActorWriterComponent {}

  input.runInput()
}
