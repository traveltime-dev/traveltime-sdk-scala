package com.igeolise.traveltimesdk.dto.requests.geocoding

import com.igeolise.traveltimesdk.TravelTimeSDK.{ResponseBody, TravelTimeRequest}
import com.igeolise.traveltimesdk.dto.common.{BCP47, Coords}
import com.igeolise.traveltimesdk.dto.requests.geocoding.QueryFragmentsUtils.queryFragments
import com.igeolise.traveltimesdk.dto.responses.GeocodingResponse
import com.igeolise.traveltimesdk.{TravelTimeHost, TravelTimeSDK}
import sttp.client.Request

/**
  * Match a query string to geographic coordinates.
  *
  * @param query       A query to geocode. Can be an address, a postcode or a venue. For example SW1A 0AA or
  *                    Victoria street, London. Providing a country or city the request will get you more
  *                    accurate results.
  * @param focusCoords Latitude and longitude of a focus point. This will prioritize results around this point.
  *                    Note that this does not exclude results that are far away from the focus point.
  * @param countryCode Country code in ISO 3166-1 alpha-2 or alpha-3
  *                    [[https://en.wikipedia.org/wiki/List_of_ISO_3166_country_codes Country codes]].
  *                    Only return the results that are within the specified country.
  * @param acceptLanguage BCP47 tag https://tools.ietf.org/html/bcp47 . For example: "en", "fr-FR"
  */
case class GeocodingRequest(
  query: String,
  focusCoords: Option[Coords] = None,
  countryCode: Option[String] = None,
  acceptLanguage: Option[BCP47] = None
) extends TravelTimeRequest[GeocodingResponse] with GeocodingRequestWithLanguage {

  final def sttpRequest[S](host: TravelTimeHost): Request[ResponseBody, S] =
    TravelTimeSDK.createGetRequest(
      host
        .uri
        .path("v4", "geocoding", "search")
        .params(queryFragments(query, focusCoords, countryCode): _*)
    )
}
