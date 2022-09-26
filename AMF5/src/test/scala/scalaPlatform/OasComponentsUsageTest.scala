package scalaPlatform

import amf.apicontract.client.scala.OASConfiguration
import amf.apicontract.client.scala.model.domain.Operation
import amf.apicontract.client.scala.model.domain.api.Api
import amf.core.client.scala.config.CachedReference
import amf.core.client.scala.model.document.Document
import org.junit.runner.RunWith
import org.scalatest.flatspec.AsyncFlatSpec
import org.scalatest.matchers.should
import org.scalatestplus.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class OasComponentsUsageTest extends AsyncFlatSpec with should.Matchers {

  val apiPath = "file://src/test/resources/examples/components/api.yaml"
  val componentsPath = "file://src/test/resources/examples/components/oas-3-component.yaml"
  val aliasPath = "file://src/test/resources/examples/components/myComponents.yaml"

  it should "parse an OAS Component and use it from cache" in {
    val componentClient = OASConfiguration.OAS30Component().baseUnitClient()
    for {
      component <- componentClient.parse(componentsPath)
      apiResult <- {
        component.conforms shouldBe true
        val cache = new ImmutableCache().add(aliasPath, componentsPath, component.baseUnit)
        val apiClient = OASConfiguration.OAS30().withUnitCache(cache).baseUnitClient()
        apiClient.parse(apiPath)
      }
    } yield {
      apiResult.conforms shouldBe true
      val api = apiResult.baseUnit.asInstanceOf[Document]
      val payload = api.encodes.asInstanceOf[Api].endPoints.head.operations.head.responses.head.payloads.head
      payload.schema.isLink shouldBe true
      payload.examples.head.isLink shouldBe true
    }
  }
}
