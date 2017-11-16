package utils

import play.api.libs.json.{JsObject, JsValue, Json}

object JsonResponse {
  def apply(msg: String = "success", data: JsValue = Json.obj(), code: Int = 200): JsObject = {
    Json.obj(
      "code" -> code,
      "msg" -> msg,
      "data" -> data
    )
  }
}
