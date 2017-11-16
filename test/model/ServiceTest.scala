package model

import java.sql.Timestamp
import java.util.Calendar

import models.{Domain, Domains, Service, Services}
import slick.jdbc.PostgresProfile.api._
import test.TestBase

class ServiceTest extends TestBase {
  lazy val validDomain = Domain(Some(1), "domain1", Some(new Timestamp(Calendar.getInstance().getTimeInMillis)), None)
  lazy val validService = Service(None,
    "service1",
    Some("value proposition"),
    Some("business capacity"),
    Some(new Timestamp(Calendar.getInstance().getTimeInMillis)),
    None,
    validDomain.id.get)

  "insert a service" should "make table size increase one" in {
    Domains.insert(validDomain).await
    val originRecordCnt = db.run(Services.query.length.result).await
    Services.insert(validService).await
    assert(db.run(Services.query.length.result).await === originRecordCnt + 1)
  }

  "get a domain" should "return one service object" in {
    Domains.insert(validDomain).await
    Services.insert(validService).await
    assert(Services.getById(1).await.get.name === "service1")
  }

  "update a service" should "return a new service" in {
    Domains.insert(validDomain).await
    val service = Services.insert(validService).await
    Services.update(service.copy(name = "newService")).await
    val newService = Services.getById(service.id.get).await
    assert(newService.get.name == "newService")
  }

  "delete a service" should "make the table size decrease one" in {
    Domains.insert(validDomain).await
    val service = Services.insert(validService).await
    val originRecordCnt = db.run(Services.query.length.result).await
    Services.delete(service.id.get).await
    assert(db.run(Services.query.length.result).await == originRecordCnt - 1)
  }
}
