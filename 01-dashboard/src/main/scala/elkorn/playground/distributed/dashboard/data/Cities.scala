package elkorn.playground.distributed.dashboard.data

object Cities {
  sealed abstract class City(val name: String)

  object NewYork extends City("New York")
  object Chicago extends City("Chicago")
  object LasVegas extends City("Las Vegas")

  val values: Set[City] = Set(NewYork, Chicago, LasVegas)

  def withName(name: String) = values.find(_.name == name)
}
