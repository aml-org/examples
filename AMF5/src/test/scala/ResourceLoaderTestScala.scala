import amf.client.environment.WebAPIConfiguration
import amf.client.remote.Content
import amf.internal.resource.ResourceLoader
import org.scalatest.flatspec.AsyncFlatSpec
import org.scalatest.matchers.should

import java.util.regex.Pattern
import scala.concurrent.Future
import scala.io.Source

// TODO: check tests work when interfaces are implemented
class ResourceLoaderTestScala extends AsyncFlatSpec with should.Matchers {

  private class CustomResourceLoader extends ResourceLoader {
    private val CUSTOM_PATH_PATTERN = Pattern.compile("^CustomProtocol/")

    /** Fetch specified resource and return associated content. Resource should have been previously accepted. */
    override def fetch(resource: String): Future[Content] = {
      val normalizedPath =
        resource.substring(CUSTOM_PATH_PATTERN.pattern.length - 1)
      val content = Source.fromFile(normalizedPath).mkString
      Future.successful(new Content(content, resource))
    }

    /** Accepts specified resource. */
    override def accepts(resource: String): Boolean = {
      if (resource == null || resource.isEmpty) return false
      val matcher = CUSTOM_PATH_PATTERN.matcher(resource)
      matcher.find
    }
  }

  "AMF custom resource loader" should "be created and used in the configuration" in {
    val rl = new CustomResourceLoader
    val client = WebAPIConfiguration
      .WebAPI()
      .withResourceLoader(rl)
      .createClient()

    client.parse(
      "CustomProtocol/AMF5/resources/examples/banking-api.raml"
    ) map (_.conforms shouldBe true)
  }
}
