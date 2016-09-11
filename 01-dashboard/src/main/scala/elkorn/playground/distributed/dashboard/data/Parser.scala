package elkorn.playground.distributed.dashboard.data

import java.time.LocalDateTime

object Parser {
  def parseFare(str: String): Option[Fare] = {
    val split = str.split(",")
    if (split.length != 2) return None

    try {
      val tripId = split(0).toLong
      val fare = split(1).toDouble
      Some(Fare(tripId, fare))
    } catch {
      case e: Throwable => None
    }
  }

  def parseTrip(str: String): Option[Trip] = {
    val split = str.split(",")
    if (split.length != 4) None

    try {
      val tripId = split(0).toLong
      val clientId = split(1).toLong
      val city = Cities.withName(split(2))
      val dateTime = LocalDateTime.parse(split(3))
      city.map(Trip(tripId, clientId, _, dateTime))
    } catch {
      case e: Throwable => None
    }
  }
}
