package com.igeolise.traveltimesdk.dto.requests

import cats.Monad
import cats.implicits._
import com.igeolise.traveltimesdk.dto.responses.TravelTimeSdkError
import com.igeolise.traveltimesdk.dto.responses.TravelTimeSdkError.ValidationError
import com.igeolise.traveltimesdk.json.reads.ErrorReads._
import com.softwaremill.sttp.{Request, _}
import play.api.libs.json._

object RequestUtils {

  trait TravelTimePlatformRequest[Response] {
    def send[R[_] : Monad, S](sttpRequest: SttpRequest[R, S]): R[Either[TravelTimeSdkError, Response]]

    def sttpRequest(host: Uri): Request[String, Nothing]
  }

  case class SttpRequest[R[_] : Monad, S](
    backend: SttpBackend[R, S],
    request: Request[String, S]
  )

  def send[R[_] : Monad, S, Response](
    sttpRequest: SttpRequest[R, S],
    validationFn: JsValue => JsResult[Response]
  ): R[Either[TravelTimeSdkError, Response]] = {

    sttpRequest.backend
      .send(sttpRequest.request)
      .map(_.body)
      .map(_.handleJsonResponse(validationFn))
  }

  def makePostRequest(requestBody: JsValue, endPoint: String, host: Uri): Request[String, Nothing] = {
    sttp
      .body(requestBody.toString)
      .contentType(MediaTypes.Json)
      .post(uri"$host/${endPoint.split('/').map(_.trim).toList}")
  }

  implicit class HandleJsonResponse(self: Either[String, String]) {
    def handleJsonResponse[Response](
      validationFn: JsValue => JsResult[Response]
    ): Either[TravelTimeSdkError, Response] =
      self match {
        case Left(err) => Left(handleErrorResponse(err))
        case Right(value) =>
          val jsVal = Json.parse(value)
            validationFn(jsVal) match {
            case e: JsError => Left(ValidationError(e))
            case s: JsSuccess[Response] => Right(s.get)
          }
      }

    def getJson[Response](
      validationFn: JsValue => JsResult[Response]
    ): Either[TravelTimeSdkError, JsValue] =
      self match {
        case Left(err) => Left(handleErrorResponse(err))
        case Right(value) =>
          validationFn(Json.parse(value)) match {
            case e: JsError => Left(ValidationError(e))
            case s: JsSuccess[Response] => Right(s.asInstanceOf[JsValue])
          }
      }
  }

  def handleErrorResponse(errString: String): TravelTimeSdkError = {
    Json.parse(errString).validate[TravelTimeSdkError.ErroResponseDetails] match {
      case JsSuccess(value, _) => TravelTimeSdkError.ErrorResponse(value)
      case a: JsError => ValidationError(a)
    }
  }

}
