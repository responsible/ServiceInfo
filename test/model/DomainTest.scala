package model

import java.sql.Timestamp
import java.util.Calendar

import models.{Domain, Domains}
import org.scalatest._
import slick.jdbc.PostgresProfile.api._
import test.TestBase

class DomainTest extends FlatSpec with TestBase {
  def getValidDomainInstance = {
    Domain(None, "domain1", Some(new Timestamp(Calendar.getInstance().getTimeInMillis)), None)
  }

  "insert one domain" should "make table size increase one" in {
    val originRecordCnt = db.run(Domains.query.length.result).await
    Domains.insert(getValidDomainInstance).await
    assert(db.run(Domains.query.length.result).await === originRecordCnt + 1)
  }

  "get one domain" should "return one domain object" in {
    val domain = Domains.insert(getValidDomainInstance).await
    val newDomain: Domain = Domains.getById(domain.id.get).await.head
    assert(newDomain.name == domain.name)
  }

  "update domain" should "return a new domain" in {
    val domain = Domains.insert(getValidDomainInstance).await
    val newDomain = domain.copy(name = "newName")
    Domains.update(newDomain).await
    val domainResult = Domains.getById(domain.id.get).await.head
    assert(domainResult.name == "newName")
  }

  "delete one domain" should "make table size decrease one" in {
    val domain = Domains.insert(getValidDomainInstance).await
    val originRecordCnt = db.run(Domains.query.length.result).await
    Domains.delete(domain.id.get).await
    assert(db.run(Domains.query.length.result).await === originRecordCnt - 1)
  }
}
