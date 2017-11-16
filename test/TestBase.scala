package test

import models.Domains
import slick.jdbc.PostgresProfile.api._

import scala.language.implicitConversions
import scala.concurrent.duration._
import scala.concurrent.{Await, Future}
import org.scalatest.{BeforeAndAfter, FlatSpec}

trait TestBase extends FlatSpec with BeforeAndAfter {
  val db: Database = Database.forConfig("local_db")

  implicit def futureToUtil[T](future: Future[T]) = new TestBaseUtil[T](future)

  before {
    db.run(Domains.query.schema.create).await
  }

  after {
    db.run(Domains.query.schema.drop).await
  }
}

class TestBaseUtil[T](val future: Future[T]) {
  def await = Await.result(future, 5 seconds)
}
