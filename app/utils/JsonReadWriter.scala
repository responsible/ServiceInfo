package utils

import java.sql.Timestamp

import models.Domain
import play.api.libs.functional.syntax._
import play.api.libs.json.{JsPath, Reads, Writes}

object JsonReadWriter {
  implicit lazy val domainWrites: Writes[Domain] = (
    (JsPath \ "id").writeNullable[Int] and
      (JsPath \ "name").write[String] and
      (JsPath \ "createTime").writeNullable[Timestamp] and
      (JsPath \ "updateTime").writeNullable[Timestamp]
    ) (unlift(Domain.unapply))

  implicit lazy val domainReads: Reads[Domain] = (
    (JsPath \ "id").readNullable[Int] and
      (JsPath \ "name").read[String] and
      (JsPath \ "createTime").readNullable[Long].map(_.map(new Timestamp(_))) and
      (JsPath \ "updateTime").readNullable[Long].map(_.map(new Timestamp(_)))
    ) (Domain.apply _)
}
