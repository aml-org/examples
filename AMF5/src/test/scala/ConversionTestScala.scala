import amf.plugins.document.apicontract.resolution.pipelines.compatibility.{Oas20CompatibilityPipeline, Raml10CompatibilityPipeline}
import org.junit.Assert._
import org.junit.Test

import scala.concurrent.Await
import scala.concurrent.duration.Duration

class ConversionTestScala {

  import amf.client.environment.WebAPIConfiguration

  import scala.concurrent.ExecutionContext.Implicits.global

  @Test def Raml10ToOas20Conversion(): Unit = {
    val client = WebAPIConfiguration.WebAPI().createClient()
    val parseResult = Await.result(
      client.parse("file://resources/examples/banking-api.raml"),
      Duration.Inf
    )
    val transformResult =
      client.transform(parseResult.bu, Oas20CompatibilityPipeline.name)
    val renderResult =
      client.render(transformResult.bu, "application/oas20+json")
    val readApi = getStrFromFile(
      "resources/expected/converted-banking-api.json"
    )
    assertEquals(readApi, renderResult)
  }

  @Test def Oas20ToRaml10Conversion(): Unit = {
    val client = WebAPIConfiguration.WebAPI().createClient()
    client
      .parse("file://resources/examples/banking-api.json")
      .map(parseResult => {
        val transformResult =
          client.transform(parseResult.bu, Raml10CompatibilityPipeline.name)
        val renderResult =
          client.render(transformResult.bu, "application/raml10+yaml")
        val readApi =
          getStrFromFile("resources/expected/converted-banking-api.raml")
        assertEquals(readApi, renderResult)
      })
  }

  private def getStrFromFile(path: String): String = {
    val source = scala.io.Source.fromFile(path)
    val read =
      try source.mkString
      finally source.close()
    read
  }
}
