package services

import java.sql.Timestamp
import java.util.Calendar

import models.{Domain, Domains, Service, Services}
import org.scalatest.FlatSpec
import test.TestBase

class ServiceServiceSpec extends FlatSpec with TestBase {
  val domainService = new DomainService
  val serviceService = new ServiceService

  lazy val validDomainInstance = Domain(Some(1), "domain1", Some(new Timestamp(Calendar.getInstance().getTimeInMillis)), None)
  lazy val validServiceInstance = Service(None,
    "service1",
    Some("value proposition"),
    Some("business capacity"),
    Some(new Timestamp(Calendar.getInstance().getTimeInMillis)),
    None,
    validDomainInstance.id.get)

  "insert a service" should "add a new service to db" in {
    Domains.insert(validDomainInstance).await
    val service = Services.insert(validServiceInstance).await
    assert(serviceService.getServiceById(service.id.get).await.head != null)
  }

  "get a service by id" should "return a service" in {
    Domains.insert(validDomainInstance).await
    val service = Services.insert(validServiceInstance).await
    assert(serviceService.getServiceById(service.id.get).await.head.name == "service1")
  }

  "update a service" should "update content of a service record" in {
    Domains.insert(validDomainInstance).await
    val service = Services.insert(validServiceInstance).await
    val newService = service.copy(name = "newService")
    serviceService.updateService(newService).await
    assert(Services.getById(service.id.get).await.head.name == "newService")
  }

  "delete a service" should "delete a service in db" in {
    Domains.insert(validDomainInstance).await
    val service = Services.insert(validServiceInstance).await
    serviceService.deleteService(service.id.get).await
    assert(Services.getById(service.id.get).await.isEmpty)
  }
}
