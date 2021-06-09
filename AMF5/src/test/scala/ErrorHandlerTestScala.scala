import amf.client.environment.RAMLConfiguration
import amf.core.errorhandling.UnhandledErrorHandler
import amf.plugins.document.apicontract.resolution.pipelines.Raml10TransformationPipeline
import org.scalatest.flatspec.AsyncFlatSpec
import org.scalatest.matchers.should

class ErrorHandlerTestScala extends AsyncFlatSpec with should.Matchers {

  "AMF client" should "use a custom error handler provider" in {
    val client =
      RAMLConfiguration
        .RAML10()
        .withErrorHandlerProvider(() =>
          UnhandledErrorHandler
        ) // throws an exception when an error is found
        .createClient()

    client.parse("file://AMF5/resources/examples/resolution-error.raml") map { parseResult =>
      assertThrows[java.lang.Exception] {
        client.transform(parseResult.bu, Raml10TransformationPipeline.name)
      }
    }
  }
}
