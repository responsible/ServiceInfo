package models

import java.sql.Timestamp

import play.api.libs.functional.syntax.unlift
import play.api.libs.json.{JsPath, Reads, Writes}
import slick.jdbc.PostgresProfile.api._
import slick.lifted.TableQuery
import play.api.libs.functional.syntax._

import scala.concurrent.Future

case class Service(id: Option[Int], name: String, valueProposition: Option[String], bizCapacity: Option[String], createTime: Option[Timestamp], updateTime: Option[Timestamp], domainId: Int)

class Services(tag: Tag) extends Table[Service](tag, "service") {
  def id = column[Int]("id", O.PrimaryKey, O.AutoInc)

  def name = column[String]("name")

  def valueProposition = column[Option[String]]("value_proposition")

  def bizCapacity = column[Option[String]]("biz_capacity")

  def createTime = column[Option[Timestamp]]("create_time")

  def updateTime = column[Option[Timestamp]]("update_time")

  def domainId = column[Int]("domain_id")

  def * = (id.?, name, valueProposition, bizCapacity, createTime, updateTime, domainId) <> (Service.tupled, Service.unapply)

  def domain = foreignKey("domain_id", domainId, Domains.query)(_.id)
}

object Services {
  val db: Database = Database.forConfig("local_db")

  val query = TableQuery[Services]

  def insert(service: Service): Future[Service] = db.run(query.returning(query.map(_.id)).into((query, id) => query.copy(id = Some(id))) += service)

  def delete(id: Int): Future[Int] = db.run(query.filter(_.id === id).delete)

  def update(service: Service): Future[Int] = db.run(query.filter(_.id === service.id).update(service))

  def getById(id: Int): Future[Option[Service]] = db.run(query.filter(_.id === id).result.headOption)

  def getAll: Future[Seq[Service]] = db.run(query.result)

  implicit lazy val serviceWrites: Writes[Service] = (
    (JsPath \ "id").writeNullable[Int] and
      (JsPath \ "name").write[String] and
      (JsPath \ "valueProposition").writeNullable[String] and
      (JsPath \ "bizCapacity").writeNullable[String] and
      (JsPath \ "createTime").writeNullable[Timestamp] and
      (JsPath \ "updateTime").writeNullable[Timestamp] and
      (JsPath \ "domainId").write[Int]
    ) (unlift(Service.unapply))

  implicit lazy val serviceReads: Reads[Service] = (
    (JsPath \ "id").readNullable[Int] and
      (JsPath \ "name").read[String] and
      (JsPath \ "valueProposition").readNullable[String] and
      (JsPath \ "bizCapacity").readNullable[String] and
      (JsPath \ "createTime").readNullable[Long].map(_.map(new Timestamp(_))) and
      (JsPath \ "updateTime").readNullable[Long].map(_.map(new Timestamp(_))) and
      (JsPath \ "domainId").read[Int]
    ) (Service.apply _)
}