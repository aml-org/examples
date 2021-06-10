import amf.client.environment.{OASConfiguration, RAMLConfiguration}
import amf.core.model.document.Document
import amf.plugins.document.apicontract.resolution.pipelines.{Oas30TransformationPipeline, Raml10TransformationPipeline}
import amf.plugins.document.apicontract.resolution.pipelines.compatibility.Raml10CompatibilityPipeline
import amf.plugins.domain.apicontract.models.api.WebApi
import org.scalatest.flatspec.AsyncFlatSpec
import org.scalatest.matchers.should

class TransformationTestScala extends AsyncFlatSpec with should.Matchers {

  "AMF transformation" should "transform a RAML 1.0 applying resource types with default pipeline" in {
    val client = RAMLConfiguration.RAML10().createClient()
    client.parse("file://resources/examples/raml-resource-type.raml") map { parseResult =>
      val transformed = client.transform(parseResult.bu, Raml10TransformationPipeline.name)
      val document = transformed.bu.asInstanceOf[Document]
      val allOperations = document.encodes.asInstanceOf[WebApi].endPoints.flatMap(_.operations)
      assert(allOperations.nonEmpty,"resource type should be resolved defining new operation")
    }
  }

  it should "transform an OAS 3.0 api with default pipeline" in {
    val client = OASConfiguration.OAS30().createClient()
    client.parse("file://resources/examples/banking-api-oas30.json") map { parseResult =>
      val transformed =
        client.transform(parseResult.bu, Oas30TransformationPipeline.name)
      val document = transformed.bu.asInstanceOf[Document]
      val allOperations = document.encodes.asInstanceOf[WebApi].endPoints.flatMap(_.operations)
      assert(allOperations.forall(_.servers.nonEmpty),"servers should be resolved to operations")
    }
  }

  it should "apply a RAML 1.0 Overlay to an api" in {
    val client = RAMLConfiguration.RAML10().createClient()
    client.parse(
      "file://resources/examples/raml-overlay/test-overlay.raml"
    ) map { parseResult =>
      assert(
        parseResult.bu.references.size == 1,
        "unresolved overlay should reference main API"
      )
      val transformed =
        client.transform(parseResult.bu, Raml10TransformationPipeline.name)

      assert(
        transformed.bu.references.isEmpty,
        "transformed model shouldn't reference anything"
      )
      val webapi = transformed.bu.asInstanceOf[Document].encodes.asInstanceOf[WebApi]
      assert(webapi.endPoints.size > 1)
    }
  }
}
