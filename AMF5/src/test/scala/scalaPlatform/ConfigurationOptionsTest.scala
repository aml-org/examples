package scalaPlatform

import amf.apicontract.client.scala.OASConfiguration
import amf.core.client.scala.config.{ParsingOptions, RenderOptions}
import amf.core.internal.remote.Mimes
import amf.core.internal.remote.Mimes.`application/ld+json`
import org.junit.runner.RunWith
import org.scalatest.flatspec.AsyncFlatSpec
import org.scalatest.matchers.should
import org.scalatestplus.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class ConfigurationOptionsTest extends AsyncFlatSpec with should.Matchers {

  "Configuration" should "be configurable with options" in {
    val client = OASConfiguration.OAS20()
      .withParsingOptions(ParsingOptions().setMaxYamlReferences(20))
      .withRenderOptions(RenderOptions().withSourceMaps).baseUnitClient()
    client.parse("file://src/test/resources/examples/banking-api.json") map { result =>
      val jsonld = client.render(result.baseUnit, `application/ld+json`)
      jsonld should include("[(87,12)-(87,39)]")
    }
  }
}
