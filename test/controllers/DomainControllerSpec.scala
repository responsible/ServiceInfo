package controllers

import java.sql.Timestamp
import java.util.Calendar

import akka.actor.ActorSystem
import akka.stream.{ActorMaterializer, Materializer}
import models.Domain
import org.scalatest.MustMatchers
import org.scalatestplus.play.guice._
import play.api.libs.json.Json
import play.api.test.Helpers._
import play.api.test._
import services.DomainService
import test.TestBase

class DomainControllerSpec extends TestBase with GuiceOneAppPerTest with Injecting with MustMatchers {
  implicit val actorSystem = ActorSystem()
  implicit val mat: Materializer = ActorMaterializer()

  val controller = new DomainController(stubControllerComponents())
  val domainService = new DomainService

  "DomainController GET" should "get all domains" in {
    domainService.addDomain(Domain(None, "domain1", Some(new Timestamp(Calendar.getInstance().getTimeInMillis)), None))

    val domain = controller.getAllDomains()
      .apply(FakeRequest(GET, routes.DomainController.getAllDomains().url))

    status(domain) mustBe OK
    contentType(domain) mustBe Some("application/json")
    contentAsString(domain) must include("domain1")
  }

  "DomainController POST" should "insert a new domain" in {
    val domain = controller.insertDomain.apply(
      FakeRequest(
        method = "POST",
        uri = routes.DomainController.insertDomain.url,
        headers = FakeHeaders(Seq("Content-type" -> "application/json")),
        body = Json.obj("name" -> "domain1")))

    status(domain) mustBe OK
    contentType(domain) mustBe Some("application/json")
    contentAsString(domain) must include("domain1")
  }

  "DomainController PUT" should "update one domain" in {
    domainService.addDomain(Domain(None, "domain1", Some(new Timestamp(Calendar.getInstance().getTimeInMillis)), None))

    val domain = controller.updateDomain(1).apply(
      FakeRequest(
        method = "PUT",
        uri = routes.DomainController.updateDomain(1).url,
        headers = FakeHeaders(Seq("Content-type" -> "application/json")),
        body = Json.obj("name" -> "domain1-update")))

    status(domain) mustBe OK
    contentType(domain) mustBe Some("application/json")
    contentAsString(domain) must include(""""count":1""")
  }

  "DomainController DELETE" should "delete one domain" in {
    domainService.addDomain(Domain(None, "domain1", Some(new Timestamp(Calendar.getInstance().getTimeInMillis)), None))

    val domain = controller.deleteDomain(1).apply(
      FakeRequest("DELETE", routes.DomainController.deleteDomain(1).url))

    status(domain) mustBe OK
    contentType(domain) mustBe Some("application/json")
    contentAsString(domain) must include(""""count":1""")
  }
}
