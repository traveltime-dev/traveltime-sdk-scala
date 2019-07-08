package com.igeolise.traveltimesdk.dto.common

import com.igeolise.traveltimesdk.dto.requests.common.CommonProperties.TimeFilterZonesProperty
import com.igeolise.traveltimesdk.dto.requests.common.RangeParams.FullRangeParams
import com.igeolise.traveltimesdk.dto.requests.common.Transportation
import scala.concurrent.duration.FiniteDuration

case class Zone(
  code: String,
  properties: ZoneSearchProperties
)

case class ZoneSearchProperties(
  travelTimeReachable: Option[TravelTime],
  travelTimeAll: Option[TravelTime],
  coverage: Option[Double]
)

case class TravelTime(
  min: FiniteDuration,
  max: FiniteDuration,
  mean: FiniteDuration,
  median: FiniteDuration
)

object ZoneSearches{
  case class DepartureSearch(
    id: String,
    coords: Coords,
    transportation: Transportation,
    departureTime: String,
    travelTime: FiniteDuration,
    reachablePostcodesThreshold: Double,
    properties: Seq[TimeFilterZonesProperty],
    range: Option[FullRangeParams] = None
  )

  case class ArrivalSearch(
    id: String,
    coords: Coords,
    transportation: Transportation,
    arrivalTime: String,
    travelTime: FiniteDuration,
    reachablePostcodesThreshold: Double,
    properties: Seq[TimeFilterZonesProperty],
    range: Option[FullRangeParams] = None
  )
}
