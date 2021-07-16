package scalaPlatform

import amf.apicontract.client.scala.{OASConfiguration, RAMLConfiguration}
import amf.core.client.scala.model.document.Document
import org.scalatest.flatspec.AsyncFlatSpec
import org.scalatest.matchers.must.Matchers.convertToAnyMustWrapper
import org.scalatest.matchers.should

class ParsingTest extends AsyncFlatSpec with should.Matchers {

  "AMF client" should "parse an OAS 2.0 API" in {
    val client = OASConfiguration.OAS20().baseUnitClient()
    client.parse("file://src/test/resources/examples/banking-api.json") map { result =>
      result.baseUnit mustBe a[Document]
      result.conforms shouldBe true
    }
  }

  it should "parse an OAS 2.0 API from a string" in {
    val client = OASConfiguration.OAS20().baseUnitClient()
    val api =
      """{
        |    "swagger": "2.0",
        |    "info": {
        |        "title": "ACME Banking HTTP API",
        |        "version": "1.0"
        |    },
        |    "host": "acme-banking.com",
        |    "paths": {}
        |}""".stripMargin
    client.parseContent(api) map { result =>
      result.baseUnit mustBe a[Document]
      result.conforms shouldBe true
    }
  }

  it should "parse an OAS 3.0 API" in {
    val client = OASConfiguration.OAS30().baseUnitClient()
    client.parse(
      "file://src/test/resources/examples/banking-api-oas30.json"
    ) map { result =>
      result.baseUnit mustBe a[Document]
      result.conforms shouldBe true
    }
  }

  it should "parse an RAML 1.0 API" in {
    val client = RAMLConfiguration.RAML10().baseUnitClient()
    client.parse("file://src/test/resources/examples/banking-api.raml") map { result =>
      result.baseUnit mustBe a[Document]
      result.conforms shouldBe true
    }
  }

  it should "parse an RAML 1.0 API from a string" in {
    val client = RAMLConfiguration.RAML10().baseUnitClient()
    val api =
      """#%RAML 1.0
        |
        |title: ACME Banking HTTP API
        |version: 1.0""".stripMargin
    client.parseContent(api) map { result =>
      result.baseUnit mustBe a[Document]
      result.conforms shouldBe true
    }
  }

  it should "parse an RAML 0.8 API" in {
    val client = RAMLConfiguration.RAML08().baseUnitClient()
    client.parse(
      "file://src/test/resources/examples/banking-api-08.raml"
    ) map { result =>
      result.baseUnit mustBe a[Document]
      result.conforms shouldBe true
    }
  }

  it should "parse an RAML 0.8 API from a string" in {
    val client = RAMLConfiguration.RAML08().baseUnitClient()
    val api =
      """#%RAML 0.8
        |
        |title: ACME Banking HTTP API
        |version: 1.0""".stripMargin
    client.parseContent(api) map { result =>
      result.baseUnit mustBe a[Document]
      result.conforms shouldBe true
    }
  }
}
