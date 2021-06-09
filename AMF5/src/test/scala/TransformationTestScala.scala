import amf.client.environment.{OASConfiguration, RAMLConfiguration}
import amf.plugins.document.apicontract.resolution.pipelines.{Oas30TransformationPipeline, Raml10TransformationPipeline}
import amf.plugins.document.apicontract.resolution.pipelines.compatibility.Raml10CompatibilityPipeline
import org.scalatest.flatspec.AsyncFlatSpec
import org.scalatest.matchers.should

class TransformationTestScala extends AsyncFlatSpec with should.Matchers {

  "AMF transformation" should "transform a RAML 1.0 api with compatibility pipeline" in {
    val client = RAMLConfiguration.RAML10().createClient()
    client.parse("file://AMF5/resources/examples/banking-api.raml") map { parseResult =>
      val transformed =
        client.transform(parseResult.bu, Raml10CompatibilityPipeline.name)
      // has amf-specific fields for cross-spec conversion support
      println(client.render(transformed.bu, "application/raml10"))
      transformed should not be null
    }
  }

  it should "transform an OAS 3.0 api with default pipeline" in {
    val client = OASConfiguration.OAS30().createClient()
    client.parse("file://AMF5/resources/examples/banking-api-oas30.json") map { parseResult =>
      val transformed =
        client.transform(parseResult.bu, Oas30TransformationPipeline.name)
      println(client.render(transformed.bu, "application/oas30+json"))
      transformed should not be null
    }
  }

  it should "apply a RAML 1.0 Overlay to an api" in {
    val client = RAMLConfiguration.RAML10().createClient()
    client.parse(
      "file://AMF5/resources/examples/raml-overlay/test-overlay.raml"
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
    }
  }
}
