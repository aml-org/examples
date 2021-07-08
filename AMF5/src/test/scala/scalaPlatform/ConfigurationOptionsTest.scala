package scalaPlatform

import amf.apicontract.client.scala.OASConfiguration
import amf.core.client.scala.config.{ParsingOptions, RenderOptions}
import org.scalatest.flatspec.AsyncFlatSpec
import org.scalatest.matchers.should

class ConfigurationOptionsTest extends AsyncFlatSpec with should.Matchers {

  "Configuration" should "be configurable with options" in {
    val client = OASConfiguration.OAS20()
      .withParsingOptions(ParsingOptions().setMaxYamlReferences(20))
      .withRenderOptions(RenderOptions().withSourceMaps).createClient()
    client.parse("file://resources/examples/banking-api.json") map { result =>
      val jsonld = client.render(result.bu)
      jsonld should include("[(87,12)-(87,39)]")
    }
  }
}
