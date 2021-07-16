package scalaPlatform

import amf.apicontract.client.scala.RAMLConfiguration
import org.scalatest.flatspec._
import org.scalatest.matchers._

class ValidationTest extends AsyncFlatSpec with should.Matchers {

  "Raml Validation" should "not conform when the api has a validation error" in {
    val client = RAMLConfiguration.RAML().baseUnitClient()
    client.parse("file://src/test/resources/examples/banking-api-error.raml") flatMap { result =>
      result.conforms shouldBe true
      client.validate(result.baseUnit) map { validationResult =>
        validationResult.conforms shouldBe false
      }
    }
  }
}
