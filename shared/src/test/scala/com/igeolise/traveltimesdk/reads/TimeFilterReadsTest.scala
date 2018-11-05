package com.igeolise.traveltimesdk.reads

import com.igeolise.traveltimesdk.json.reads.timefilter.TimeFilterReads._
import com.igeolise.traveltimesdk.dto.responses.timefilter.TimeFilterResponse
import com.igeolise.traveltimesdk.dto.responses.timefilter.TimeFilterResponse.{Location, Properties, SingleSearchResult}
import com.igeolise.traveltimesdk.TestUtils
import org.scalatest.{FunSpec, Matchers}
import play.api.libs.json.{JsSuccess, Json}
import scala.concurrent.duration._

class TimeFilterReadsTest extends FunSpec with Matchers {

  it("parse forward_search response") {
    val json = TestUtils.resource("shared/src/test/resources/json/TimeFilter/response/timeFilterResponse.json")
    val result = Json.parse(json).validate[TimeFilterResponse]

    result shouldBe a [JsSuccess[_]]

    result.get shouldBe TimeFilterResponse(
      Seq(
        SingleSearchResult(
          "forward search example",
          Seq(
            Location(
              "Hyde Park",
              Seq(
                Properties(Some(Duration(1753, SECONDS))),
                Properties(Some(Duration(1804, SECONDS))),
                Properties(Some(Duration(1815, SECONDS)))
              )
            )
          ),
          Seq(
            "ZSL London Zoo"
          )
        )
      ),
      Json.parse(json)
    )
  }

}