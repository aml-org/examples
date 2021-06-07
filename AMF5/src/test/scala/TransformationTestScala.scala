import amf.client.exported.{OASConfiguration, RAMLConfiguration}
import amf.core.resolution.pipelines.TransformationPipeline
import amf.plugins.document.apicontract.resolution.pipelines.{Oas30TransformationPipeline, Raml10TransformationPipeline}
import org.junit.Assert.{assertNotNull, assertTrue}
import org.junit.Test

class TransformationTestScala {

  @Test def transformRaml10Compatibility(): Unit = {
    val client = RAMLConfiguration.RAML10().createClient()
    val parseResult =
      client.parse("file://resources/examples/banking-api.raml").get()
    val transformed = client.transform(
      parseResult.baseUnit,
      TransformationPipeline.COMPATIBILITY_PIPELINE
    )
    assertNotNull(transformed)
    // has amf-specific fields for cross-spec conversion support
    println(client.render(transformed.baseUnit))
  }

  @Test def transformOas30(): Unit = {
    val client = OASConfiguration.OAS30().createClient()
    val parseResult =
      client.parse("file://resources/examples/banking-api-oas30.json").get()
    val transformed =
      client.transform(parseResult.baseUnit, Oas30TransformationPipeline.name) // uses default pipeline
    assertNotNull(transformed)
    println(client.render(transformed.baseUnit))
  }

  @Test def resolveRamlOverlay(): Unit = {
    val client = RAMLConfiguration.RAML10().createClient()

    val parseResult = client
      .parse("file://resources/examples/raml-overlay/test-overlay.raml")
      .get()
    assertTrue(
      "unresolved overlay should reference main API",
      parseResult.baseUnit.references.size == 1
    )

    val transformResult = client.transform(
      parseResult.baseUnit,
      Raml10TransformationPipeline.name
    )
    assertTrue(
      "transformed model shouldn't reference anything",
      transformResult.baseUnit.references.size == 0
    )

    println(client.render(transformResult.baseUnit))
  }

}
