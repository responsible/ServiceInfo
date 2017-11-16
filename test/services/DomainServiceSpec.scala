package services

import java.sql.Timestamp
import java.util.Calendar

import models.{Domain, Domains}
import org.scalatest.FlatSpec
import test.TestBase

class DomainServiceSpec extends FlatSpec with TestBase {
  val domainService = new DomainService

  def getValidDomainInstance = {
    Domain(None, "domain1", Some(new Timestamp(Calendar.getInstance().getTimeInMillis)), None)
  }

  "get a domain by id" should "return a domain" in {
    val domain = Domains.insert(getValidDomainInstance).await
    assert(domainService.getDomainById(domain.id.get).await.head.name == "domain1")
  }

  "insert a domain" should "add a new domain to db" in {
    val domain = Domains.insert(getValidDomainInstance).await
    assert(domainService.getDomainById(domain.id.get).await.head != null)
  }

  "update a domain" should "update content of a domain record" in {
    val domain = Domains.insert(getValidDomainInstance).await
    val newDomain = domain.copy(name = "newName")
    domainService.updateDomain(newDomain).await
    assert(Domains.getById(domain.id.get).await.head.name == "newName")
  }

  "delete a domain" should "delete a domain in db" in {
    val domain = Domains.insert(getValidDomainInstance).await
    domainService.deleteDomain(domain.id.get).await
    assert(Domains.getById(domain.id.get).await.isEmpty)
  }
}
