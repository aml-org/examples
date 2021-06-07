import amf.client.environment.WebAPIConfiguration
import amf.core.model.document.Document
import amf.core.validation.PayloadValidator
import amf.plugins.document.apicontract.resolution.pipelines.Raml10TransformationPipeline
import amf.plugins.domain.apicontract.models.api.WebApi
import org.junit.Test

import scala.concurrent.Await
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration


class PayloadValidationTestScala {
  private var payloadValidator: PayloadValidator = null


  @Test
  def PayloadValidationTest(): Unit = {
    val client = WebAPIConfiguration.WebAPI().createClient()
    val parsingResult = Await.result(client.parse("file://resources/examples/simple-api.raml"), Duration.Inf)
    val transformationResult = client.transform(parsingResult.bu, Raml10TransformationPipeline.name)

    // get the model.encodes() to isolate the WebApi model
    val webApi = transformationResult.bu.asInstanceOf[Document].encodes.asInstanceOf[WebApi]
    val usersEndpoint = webApi.endPoints.head
    val postMethod = usersEndpoint.operations.head
    val request = postMethod.requests.head
    val userPayload = request.payloads.head
    val userSchema = userPayload.schema
//
//    payloadValidator = ShapePayloadValidatorFactory.createPayloadValidator(userSchema)
//    val invalidUserPayload = "{\"name\": \"firstname and lastname\"}"
//
//    val isValid = Await.result(payloadValidator.isValid("application/json", invalidUserPayload), Duration.Inf)
//    assertFalse(isValid)
//
//    val validateReport = Await.result(payloadValidator.validate("application/json", invalidUserPayload), Duration.Inf)
//    assertFalse(validateReport.conforms)
//
//    val syncValidateReport = payloadValidator.syncValidate("application/json", invalidUserPayload)
//    assertFalse(syncValidateReport.conforms)

  }
}
