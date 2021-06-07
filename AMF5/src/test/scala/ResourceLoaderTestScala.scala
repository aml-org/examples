import amf.client.environment.WebAPIConfiguration
import amf.client.remote.Content
import amf.internal.resource.ResourceLoader
import org.junit.Assert.assertTrue
import org.junit.Test
import java.util.regex.Pattern
import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future}
import scala.io.Source

// TODO: check tests work when interfaces are implemented
class ResourceLoaderTestScala {

  private class CustomResourceLoader extends ResourceLoader {
    private val CUSTOM_PATH_PATTERN = Pattern.compile("^CustomProtocol/")

    /** Fetch specified resource and return associated content. Resource should have benn previously accepted. */
    override def fetch(resource: String): Future[Content] = {
      val normalizedPath = resource.substring(CUSTOM_PATH_PATTERN.pattern.length - 1)
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

  @Test
  def withResourceLoader(): Unit = {
    val rl = new CustomResourceLoader
    val client = WebAPIConfiguration
      .WebAPI()
      .withResourceLoader(rl)
      .createClient()
    val result = Await.result(client.parse("CustomProtocol/resources/examples/banking-api.raml"), Duration.Inf)
    assertTrue(result.conforms)
  }

}
