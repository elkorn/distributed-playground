package elkorn.playground.distributed.dashboard.data

import java.time.LocalDateTime

import elkorn.playground.distributed.dashboard.data.Cities.City

case class Trip(id: Long, clientId: Long, city: City, dateTime: LocalDateTime)
