import amf.ProfileName
import amf.client.environment.RAMLConfiguration
import org.junit.Assert.{assertFalse, assertTrue}
import org.junit.Test

import scala.concurrent.Await
import scala.concurrent.duration.Duration

class ValidationTestScala {

  @Test def validateRaml(): Unit = {
    val client = RAMLConfiguration.RAML().createClient()
    val parsingResult = Await.result(client.parse("file://resources/examples/banking-api-error.raml"), Duration.Inf)
    assertTrue(parsingResult.conforms)
    val validationResult = Await.result(client.validate(parsingResult.bu), Duration.Inf)
    assertFalse(validationResult.conforms)
  }

  @Test def validateRamlWithCustomValidation(): Unit = {
    val configuration = Await.result(RAMLConfiguration.RAML().withCustomProfile("file://resources/validation_profile.raml"), Duration.Inf)
    val client = configuration.createClient()
    val parsingResult = Await.result(client.parse("file://resources/examples/banking-api-error.raml"), Duration.Inf)
    val validationResult = Await.result(client.validate(parsingResult.bu, ProfileName("Banking")), Duration.Inf)
    assertTrue(validationResult.conforms)
  }

}
