import amf.client.environment.{OASConfiguration, RAMLConfiguration}
import org.junit.Assert.assertNotNull
import org.junit.Test

import scala.concurrent.Await
import scala.concurrent.duration.DurationInt

class TransformationTestScala {

  @Test
  def transformRaml10(): Unit = {
    val client = RAMLConfiguration.RAML10().createClient()
    val result = Await.result(
      client.parse("file://resources/examples/banking-api.raml"), 1 second)
    val transformed = client.transform(result.bu)
    assertNotNull(transformed)
    // TODO: add COMPATIBILITY_PIPELINE and render example with amf-specific fields like in ResolutionTest.java
  }

  @Test
  def transformOas(): Unit = {
    val client = OASConfiguration.OAS30().createClient()
    val result = Await.result(
      client.parse("file://resources/examples/banking-api-oas30.json"), 1 second)
    val transformed = client.transform(result.bu)
    assertNotNull(transformed)
  }

}
