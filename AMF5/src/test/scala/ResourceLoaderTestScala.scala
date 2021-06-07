import amf.client.convert.CoreClientConverters.ClientFuture
import amf.client.exported.WebAPIConfiguration
import amf.client.remote.Content
import amf.client.resource.{FileResourceLoader, ResourceLoader}
import org.junit.Assert.assertTrue
import org.junit.Test

import java.io.File
import java.util.regex.Pattern

// TODO: check tests work when interfaces are implemented
class ResourceLoaderTestScala {

  private class CustomResourceLoader extends ResourceLoader {
    private val resourceLoader = new FileResourceLoader
    private val CUSTOM_PATH_PATTERN = Pattern.compile("^CustomProtocol/")

    /** Fetch specified resource and return associated content. Resource should have benn previously accepted. */
    override def fetch(resource: String): ClientFuture[Content] = {
      val normalizedPath =
        resource.substring(CUSTOM_PATH_PATTERN.pattern.length - 1)
      resourceLoader.fetch(new File(normalizedPath).getAbsolutePath)
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
    val result = client.parse("CustomProtocol/resources/examples/banking-api.raml").get()
    assertTrue(result.conforms)
  }

}
