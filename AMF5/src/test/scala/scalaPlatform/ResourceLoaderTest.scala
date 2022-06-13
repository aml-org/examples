package scalaPlatform

import amf.apicontract.client.scala.{RAMLConfiguration, WebAPIConfiguration}
import org.junit.runner.RunWith
import org.scalatest.flatspec.AsyncFlatSpec
import org.scalatest.matchers.should
import org.scalatestplus.junit.JUnitRunner

import java.util.regex.Pattern

@RunWith(classOf[JUnitRunner])
class ResourceLoaderTest extends AsyncFlatSpec with should.Matchers {

  private val CUSTOM_PATH_PATTERN = Pattern.compile("^CustomProtocol/")

  "AMF custom resource loader" should "be created and used in the configuration" in {
    val rl = new CustomResourceLoader(CUSTOM_PATH_PATTERN)
    val client = RAMLConfiguration
      .RAML10()
      .withResourceLoader(rl)
      .baseUnitClient()

    client.parse(
      "CustomProtocol/src/test/resources/examples/banking-api.raml"
    ) map (_.conforms shouldBe true)
  }
}
