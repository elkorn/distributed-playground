package elkorn.playground.distributed.dashboard

import elkorn.playground.distributed.dashboard.input.HttpApiInputComponent

object DashboardApp extends App with DefaultCoreComponent {
  val input = new HttpApiInputComponent with DefaultCoreComponent {}

  input.runInput()
}
