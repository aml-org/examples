package scalaPlatform

import amf.apicontract.client.scala.{OASConfiguration, RAMLConfiguration}
import amf.apicontract.client.scala.model.domain.api.WebApi
import amf.core.client.scala.model.document.Document
import org.scalatest.flatspec.AsyncFlatSpec
import org.scalatest.matchers.should

class TransformationTest extends AsyncFlatSpec with should.Matchers {

  "AMF transformation" should "transform a RAML 1.0 applying resource types with default pipeline" in {
    val client = RAMLConfiguration.RAML10().baseUnitClient()
    client.parse("file://src/test/resources/examples/raml-resource-type.raml") map { parseResult =>
      val transformed = client.transform(parseResult.baseUnit)
      val document = transformed.baseUnit.asInstanceOf[Document]
      val allOperations = document.encodes.asInstanceOf[WebApi].endPoints.flatMap(_.operations)
      assert(allOperations.nonEmpty,"resource type should be resolved defining new operation")
    }
  }

  it should "transform an OAS 3.0 api with default pipeline" in {
    val client = OASConfiguration.OAS30().baseUnitClient()
    client.parse("file://src/test/resources/examples/banking-api-oas30.json") map { parseResult =>
      val transformed =
        client.transform(parseResult.baseUnit)
      val document = transformed.baseUnit.asInstanceOf[Document]
      val allOperations = document.encodes.asInstanceOf[WebApi].endPoints.flatMap(_.operations)
      assert(allOperations.forall(_.servers.nonEmpty),"servers should be resolved to operations")
    }
  }

  it should "apply a RAML 1.0 Overlay to an api" in {
    val client = RAMLConfiguration.RAML10().baseUnitClient()
    client.parse(
      "file://src/test/resources/examples/raml-overlay/test-overlay.raml"
    ) map { parseResult =>
      assert(
        parseResult.baseUnit.references.size == 1,
        "unresolved overlay should reference main API"
      )
      val transformed =
        client.transform(parseResult.baseUnit)

      assert(
        transformed.baseUnit.references.isEmpty,
        "transformed model shouldn't reference anything"
      )
      val webapi = transformed.baseUnit.asInstanceOf[Document].encodes.asInstanceOf[WebApi]
      assert(webapi.endPoints.size > 1)
    }
  }
}
