package scalaPlatform

import amf.apicontract.client.scala.RAMLConfiguration
import amf.core.client.scala.errorhandling.UnhandledErrorHandler
import org.junit.runner.RunWith
import org.scalatest.flatspec.AsyncFlatSpec
import org.scalatest.matchers.should
import org.scalatestplus.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class ErrorHandlerTest extends AsyncFlatSpec with should.Matchers {

  "AMF client" should "use a custom error handler provider" in {
    val client =
      RAMLConfiguration
        .RAML10()
        .withErrorHandlerProvider(() =>
          UnhandledErrorHandler
        ) // throws an exception when an error is found
        .baseUnitClient()

    client.parse("file://src/test/resources/examples/resolution-error.raml") map { parseResult =>
      assertThrows[java.lang.Exception] {
        client.transform(parseResult.baseUnit)
      }
    }
  }
}
