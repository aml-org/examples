package scalaPlatform

import amf.apicontract.client.scala.WebAPIConfiguration
import org.scalatest.flatspec.AsyncFlatSpec
import org.scalatest.matchers.should

import java.util.regex.Pattern

class ResourceLoaderTest extends AsyncFlatSpec with should.Matchers {

  private val CUSTOM_PATH_PATTERN = Pattern.compile("^CustomProtocol/")

  "AMF custom resource loader" should "be created and used in the configuration" in {
    val rl = new CustomResourceLoader(CUSTOM_PATH_PATTERN)
    val client = WebAPIConfiguration
      .WebAPI()
      .withResourceLoader(rl)
      .baseUnitClient()

    client.parse(
      "CustomProtocol/src/test/resources/examples/banking-api.raml"
    ) map (_.conforms shouldBe true)
  }
}
