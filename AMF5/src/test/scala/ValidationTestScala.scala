import amf.ProfileName
import amf.client.environment.RAMLConfiguration
import org.scalatest.flatspec._
import org.scalatest.matchers._

class ValidationTestScala extends AsyncFlatSpec with should.Matchers {

  "Raml Validation" should "not conform when the api has a validation error" in {
    val client = RAMLConfiguration.RAML().createClient()
    client.parse("file://AMF5/resources/examples/banking-api-error.raml") flatMap { result =>
      result.conforms shouldBe true
      client.validate(result.bu) map { validationResult =>
        validationResult.conforms shouldBe false
      }
    }
  }

  it should "conform when validating with a custom validation profile" in {
    RAMLConfiguration.RAML().withCustomValidationsEnabled flatMap (_.withCustomProfile(
      "file://AMF5/resources/validation_profile.raml"
    )) flatMap { configuration =>
      val client = configuration.createClient()
      client.parse("file://AMF5/resources/examples/banking-api-error.raml") flatMap { parseResult =>
        client.validate(parseResult.bu, ProfileName("Banking")) map (_.conforms shouldBe true)
      }
    }
  }
}
