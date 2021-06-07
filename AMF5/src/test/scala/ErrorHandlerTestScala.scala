import amf.client.environment.RAMLConfiguration
import amf.core.errorhandling.UnhandledErrorHandler
import amf.plugins.document.apicontract.resolution.pipelines.Raml10TransformationPipeline
import org.junit.Test

import scala.concurrent.Await
import scala.concurrent.duration.Duration

class ErrorHandlerTestScala {

  @Test(expected = classOf[java.lang.Exception])
  def customErrorHandler(): Unit = {
    val client =
      RAMLConfiguration
        .RAML10()
        .withErrorHandlerProvider(() => UnhandledErrorHandler)
        .createClient()

    val parseResult = Await.result(
      client.parse("file://resources/examples/resolution-error.raml"),
      Duration.Inf
    )

    client.transform(parseResult.bu, Raml10TransformationPipeline.name)
  }
}
