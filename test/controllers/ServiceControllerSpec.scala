package controllers

import java.sql.Timestamp
import java.util.Calendar

import akka.actor.ActorSystem
import akka.stream.{ActorMaterializer, Materializer}
import models.{Domain, Service}
import org.scalatest.MustMatchers
import org.scalatestplus.play.guice._
import play.api.libs.json.Json
import play.api.test.Helpers._
import play.api.test._
import services.{DomainService, ServiceService}
import test.TestBase

class ServiceControllerSpec extends TestBase with GuiceOneAppPerTest with Injecting with MustMatchers {
  implicit val actorSystem = ActorSystem()
  implicit val mat: Materializer = ActorMaterializer()

  val controller = new ServiceController(stubControllerComponents())
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

  "ServiceController GET" should "get all services" in {
    domainService.addDomain(validDomainInstance).await
    serviceService.addService(validServiceInstance).await

    val service = controller.getAllServices()
      .apply(FakeRequest(GET, routes.ServiceController.getAllServices().url))

    status(service) mustBe OK
    contentType(service) mustBe Some("application/json")
    contentAsString(service) must include("service1")
  }

  "ServiceController POST" should "insert a new service" in {
    domainService.addDomain(validDomainInstance).await
    val service = controller.insertService.apply(
      FakeRequest(
        method = "POST",
        uri = routes.ServiceController.insertService.url,
        headers = FakeHeaders(Seq("Content-type" -> "application/json")),
        body = Json.obj("name" -> "service1", "domainId" -> 1)))

    status(service) mustBe OK
    contentType(service) mustBe Some("application/json")
    contentAsString(service) must include("service1")
  }

  "ServiceController PUT" should "update one service" in {
    domainService.addDomain(validDomainInstance).await
    serviceService.addService(validServiceInstance).await

    val service = controller.updateService(1).apply(
      FakeRequest(
        method = "PUT",
        uri = routes.ServiceController.updateService(1).url,
        headers = FakeHeaders(Seq("Content-type" -> "application/json")),
        body = Json.obj("name" -> "service1-update", "domainId" -> 1)))

    status(service) mustBe OK
    contentType(service) mustBe Some("application/json")
    contentAsString(service) must include(""""count":1""")
  }

  "ServiceController DELETE" should "delete one service" in {
    domainService.addDomain(validDomainInstance).await
    serviceService.addService(validServiceInstance).await

    val service = controller.deleteService(1).apply(
      FakeRequest("DELETE", routes.ServiceController.deleteService(1).url))

    status(service) mustBe OK
    contentType(service) mustBe Some("application/json")
    contentAsString(service) must include(""""count":1""")
  }
}
