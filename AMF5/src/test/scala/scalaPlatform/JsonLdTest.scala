package scalaPlatform

import amf.apicontract.client.scala.WebAPIConfiguration
import amf.core.client.scala.model.document.Document
import org.scalatest.flatspec.AsyncFlatSpec
import org.scalatest.matchers.must.Matchers.convertToAnyMustWrapper
import org.scalatest.matchers.should

class JsonLdTest extends AsyncFlatSpec with should.Matchers {

  "An embedded json-ld" should "give the corresponding in-memory model" in {
    val client = WebAPIConfiguration.WebAPI().baseUnitClient()
    client.parse(
      "file://src/test/resources/examples/banking-api.embedded.jsonld"
    ) map { result =>
      result.baseUnit mustBe a[Document]
      result.conforms shouldBe true
    }
  }

  "A flattened json-ld" should "give the corresponding in-memory model" in {
    val client = WebAPIConfiguration.WebAPI().baseUnitClient()
    client.parse(
      "file://src/test/resources/examples/banking-api.flattened.jsonld"
    ) map { result =>
      result.baseUnit mustBe a[Document]
      result.conforms shouldBe true
    }
  }
}
