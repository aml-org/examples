package scalaPlatform

import amf.apicontract.client.common.ProvidedMediaType
import amf.apicontract.client.scala.WebAPIConfiguration
import org.scalatest.flatspec.AsyncFlatSpec
import org.scalatest.matchers.should

class ConversionTest extends AsyncFlatSpec with should.Matchers {

  "AMF" should "convert a RAML 1.0 API to OAS 2.0" in {
    val client = WebAPIConfiguration.WebAPI().baseUnitClient()
    client.parse("file://src/test/resources/examples/banking-api.raml") map { parseResult =>
      val transformResult = client.transformCompatibility(parseResult.baseUnit, ProvidedMediaType.Oas20)
      val renderResult = client.render(transformResult.baseUnit, "application/oas20+json")
      val readApi = getStrFromFile("src/test/resources/expected/converted-banking-api.json")
      renderResult shouldEqual readApi
    }
  }

  "AMF" should "convert an OAS 2.0 API to RAML 1.0" in {
    val client = WebAPIConfiguration.WebAPI().baseUnitClient()
    client.parse("file://src/test/resources/examples/banking-api.json") map { parseResult =>
      val transformResult = client.transformCompatibility(parseResult.baseUnit, ProvidedMediaType.Raml10)
      val renderResult = client.render(transformResult.baseUnit, "application/raml10+yaml")
      val readApi = getStrFromFile("src/test/resources/expected/converted-banking-api.raml")
      renderResult shouldEqual readApi
    }
  }

  private def getStrFromFile(path: String): String = {
    val source = scala.io.Source.fromFile(path)
    val read =
      try source.mkString
      finally source.close()
    read
  }
}
