package scalaPlatform

import amf.apicontract.client.scala.APIConfiguration
import amf.core.client.common.validation.ProfileNames
import org.junit.runner.RunWith
import org.scalatest.flatspec.AsyncFlatSpec
import org.scalatest.matchers.should.Matchers
import org.scalatestplus.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class UnknownConfigCreationTest extends AsyncFlatSpec with Matchers{

  it should "be able to create a config from a parsed API result" in {
    val client = APIConfiguration.API().baseUnitClient()
    client
      .parse("file://src/test/resources/examples/banking-api-error.raml")
      .flatMap { result =>
        val nextConfig = APIConfiguration.fromSpec(result.sourceSpec)
        nextConfig.baseUnitClient().validate(result.baseUnit)
      }
      .map { report =>
        report.conforms shouldBe false
        report.profile shouldBe ProfileNames.RAML10
        report.results.head.message shouldBe "Protocols must have a case insensitive value matching http or https"
      }
  }
}
