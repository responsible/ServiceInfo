package models

import java.sql.Timestamp

import play.api.libs.functional.syntax.unlift
import play.api.libs.json.{JsPath, Reads, Writes}
import slick.jdbc.PostgresProfile.api._
import slick.lifted.TableQuery
import play.api.libs.functional.syntax._

import scala.concurrent.Future

case class Domain(id: Option[Int], name: String, createTime: Option[Timestamp], updateTime: Option[Timestamp])

class Domains(tag: Tag) extends Table[Domain](tag, "domain") {
  def id = column[Int]("id", O.PrimaryKey, O.AutoInc)

  def name = column[String]("name")

  def createTime = column[Option[Timestamp]]("create_time")

  def updateTime = column[Option[Timestamp]]("update_time")

  def * = (id.?, name, createTime, updateTime) <> (Domain.tupled, Domain.unapply)
}

object Domains {
  val db: Database = Database.forConfig("local_db")

  val query = TableQuery[Domains]

  def insert(domain: Domain): Future[Domain] = db.run(query.returning(query.map(_.id)).into((query, id) => query.copy(id = Some(id))) += domain)

  def delete(id: Int): Future[Int] = db.run(query.filter(_.id === id).delete)

  def update(domain: Domain): Future[Int] = db.run(query.filter(_.id === domain.id).update(domain))

  def getById(id: Int): Future[Option[Domain]] = db.run(query.filter(_.id === id).result.headOption)

  def getAll: Future[Seq[Domain]] = db.run(query.result)

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
