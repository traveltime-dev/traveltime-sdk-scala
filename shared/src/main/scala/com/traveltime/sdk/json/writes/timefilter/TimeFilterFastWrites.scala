package com.traveltime.sdk.json.writes.timefilter

import com.traveltime.sdk.dto.requests.timefilter.TimeFilterFastRequest
import com.traveltime.sdk.dto.requests.timefilter.TimeFilterFastRequest._
import com.traveltime.sdk.dto.requests.common.CommonProperties.{Property, TimeFilterFastProperty}
import com.traveltime.sdk.dto.requests.common.Transportation.TimeFilterFastTransportation
import com.traveltime.sdk.dto.requests.common.Location
import com.traveltime.sdk.json.writes.CommonWrites._
import play.api.libs.functional.syntax._
import play.api.libs.json.{Writes, __}

import scala.concurrent.duration.FiniteDuration

object TimeFilterFastWrites {

  implicit val arrivalTimePeriodWrites: Writes[ArrivalTimePeriod] = Writes.StringWrites.contramap(_.periodType)

  implicit val oneToManyWrites: Writes[OneToMany] = (
    (__ \ "id").write[String] and
    (__ \ "departure_location_id").write[String] and
    (__ \ "arrival_location_ids").write[Seq[String]] and
    (__ \ "transportation").write[TimeFilterFastTransportation] and
    (__ \ "travel_time").write[FiniteDuration](finiteDurationToSecondsWrites) and
    (__ \ "arrival_time_period").write[ArrivalTimePeriod] and
    (__ \ "properties").write[Seq[Property]]
  )(unlift(OneToMany.unapply))

  implicit val manyToOneWrites: Writes[ManyToOne] = (
    (__ \ "id").write[String] and
    (__ \ "arrival_location_id").write[String] and
    (__ \ "departure_location_ids").write[Seq[String]] and
    (__ \ "transportation").write[TimeFilterFastTransportation] and
    (__ \ "travel_time").write[FiniteDuration](finiteDurationToSecondsWrites) and
    (__ \ "arrival_time_period").write[ArrivalTimePeriod] and
    (__ \ "properties").write[Seq[Property]]
  )(unlift(ManyToOne.unapply))

  implicit val timeFilterFastArrivalSearchWrites: Writes[ArrivalSearch] = (
    (__ \ "many_to_one").write[Seq[ManyToOne]] and
    (__ \ "one_to_many").write[Seq[OneToMany]]
  )(unlift(ArrivalSearch.unapply))

  implicit val timeFilterFastWrites: Writes[TimeFilterFastRequest] = (
    (__ \ "locations").write[Seq[Location]] and
    (__ \ "arrival_searches").write[ArrivalSearch]
  )(unlift(TimeFilterFastRequest.unapply))
}
