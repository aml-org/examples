package scala

import amf.apicontract.client.scala.WebAPIConfiguration
import amf.apicontract.client.scala.model.domain.api.WebApi
import amf.core.client.common.validation.ValidationMode
import amf.core.client.scala.model.document.Document
import org.scalatest.flatspec.AsyncFlatSpec
import org.scalatest.matchers.should

class PayloadValidationTest extends AsyncFlatSpec with should.Matchers {

  "AMF payload validation" should "create and use a user schema payload validator" in {
    val configuration = WebAPIConfiguration.WebAPI()
    val client = configuration.createClient()
    client.parse("file://resources/examples/simple-api.raml") map { parseResult =>
      val transformationResult = client.transform(parseResult.bu)

      // get the model.encodes() to isolate the WebApi model
      val webApi = transformationResult.bu
        .asInstanceOf[Document]
        .encodes
        .asInstanceOf[WebApi]
      val usersEndpoint = webApi.endPoints.head
      val postMethod = usersEndpoint.operations.head
      val request = postMethod.requests.head
      val userPayload = request.payloads.head
      val userSchema = userPayload.schema

      // create payload validator
      val payloadValidator =
        configuration.payloadValidatorFactory().createFor(
          userSchema,
          "application/json",
          ValidationMode.StrictValidationMode
        )

      // invalid payload to validate against
      val invalidUserPayload = "{\"name\": \"firstname and lastname\"}"

      // .validate returns a Validation report with all results found
      payloadValidator.validate(
        invalidUserPayload
      ) map (_.conforms shouldBe false)

      // .syncValidate validates synchronously
      payloadValidator.syncValidate(invalidUserPayload).conforms shouldBe false
    }

  }
}
