package scalaPlatform

import amf.apicontract.client.scala.{APIConfiguration, OASConfiguration, RAMLConfiguration, WebAPIConfiguration}
import amf.core.client.scala.model.document.Document
import amf.core.internal.remote.Spec
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

  it should "parse an OAS 3.0 API from a string" in {
    val client = OASConfiguration.OAS30().baseUnitClient()
    val api =
      """{
        |  "openapi": "3.0.0",
        |  "info": {
        |    "title": "Basic content",
        |    "version": "0.1"
        |  },
        |  "paths": {}
        |}""".stripMargin
    client.parseContent(api) map { result =>
      result.baseUnit mustBe a[Document]
      println(result.results)
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

  it should "parse an unknown WebAPI" in {
    val client = WebAPIConfiguration.WebAPI().baseUnitClient()
    client.parse("file://src/test/resources/examples/banking-api.raml") map { result =>
      result.baseUnit mustBe a[Document]
      result.conforms shouldBe true
      result.sourceSpec.isRaml shouldBe true
      result.sourceSpec.id mustBe Spec.RAML10.id
    }
  }

  it should "parse an unknown WebAPI from a string" in {
    val client = WebAPIConfiguration.WebAPI().baseUnitClient()
    val api =
      """{
        |  "openapi": "3.0.0",
        |  "info": {
        |    "title": "Basic content",
        |    "version": "0.1"
        |  },
        |  "paths": {}
        |}""".stripMargin
    client.parseContent(api) map { result =>
      result.baseUnit mustBe a[Document]
      println(result.results)
      result.conforms shouldBe true
      result.sourceSpec.isOas shouldBe true
      result.sourceSpec.id mustBe Spec.OAS30.id
    }
  }

  it should "parse an unknown API" in {
    val client = WebAPIConfiguration.WebAPI().baseUnitClient()
    client.parse("file://src/test/resources/examples/async.yaml") map { result =>
      result.baseUnit mustBe a[Document]
      result.conforms shouldBe true
      result.sourceSpec.isAsync shouldBe true
      result.sourceSpec.id mustBe Spec.ASYNC20.id
    }
  }

  it should "parse an unknown API from a string" in {
    val client = APIConfiguration.API().baseUnitClient()
    val api =
      """asyncapi: "2.0.0"
        |info:
        |  title: "Something"
        |  version: "1.0"
        |channels: {}
        |""".stripMargin
    client.parseContent(api) map { result =>
      result.baseUnit mustBe a[Document]
      println(result.results)
      result.conforms shouldBe true
      result.sourceSpec.isAsync shouldBe true
      result.sourceSpec.id mustBe Spec.ASYNC20.id
    }
  }
}
