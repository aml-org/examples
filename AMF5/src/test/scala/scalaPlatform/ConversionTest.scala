package scalaPlatform

import amf.apicontract.client.scala.{AMFBaseUnitClient, OASConfiguration, RAMLConfiguration}
import amf.core.client.common.transform.PipelineId
import org.scalatest.flatspec.AsyncFlatSpec
import org.scalatest.matchers.should

class ConversionTest extends AsyncFlatSpec with should.Matchers {

  val raml10Client: AMFBaseUnitClient = RAMLConfiguration.RAML10().baseUnitClient()
  val oas20Client: AMFBaseUnitClient = OASConfiguration.OAS20().baseUnitClient()
  val oas30Client: AMFBaseUnitClient = OASConfiguration.OAS30().baseUnitClient()

  "AMF" should "convert a RAML 1.0 API to OAS 2.0" in {
    raml10Client.parse("file://src/test/resources/examples/banking-api.raml") map { parseResult =>
      val transformResult = oas20Client.transform(parseResult.baseUnit, PipelineId.Compatibility)
      val renderResult = oas20Client.render(transformResult.baseUnit)
      val readApi = getStrFromFile("src/test/resources/expected/converted-banking-api.json")
      renderResult shouldEqual readApi
    }
  }

  "AMF" should "convert an OAS 2.0 API to RAML 1.0" in {
    oas20Client.parse("file://src/test/resources/examples/banking-api.json") map { parseResult =>
      val transformResult = raml10Client.transform(parseResult.baseUnit, PipelineId.Compatibility)
      val renderResult = raml10Client.render(transformResult.baseUnit)
      val readApi = getStrFromFile("src/test/resources/expected/converted-banking-api.raml")
      renderResult shouldEqual readApi
    }
  }

  "AMF" should "convert an OAS 3.0 API to RAML 1.0" in {
    oas30Client.parse("file://src/test/resources/examples/sample-api.yaml") map { parseResult =>
      val transformResult = raml10Client.transform(parseResult.baseUnit, PipelineId.Compatibility)
      val renderResult = raml10Client.render(transformResult.baseUnit)
      val readApi = getStrFromFile("src/test/resources/expected/converted-sample-api.raml")
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
