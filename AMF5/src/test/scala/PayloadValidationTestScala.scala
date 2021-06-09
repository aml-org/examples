import amf.client.environment.WebAPIConfiguration
import amf.client.remod.amfcore.plugins.validate.ValidationConfiguration
import amf.core.model.document.Document
import amf.plugins.document.apicontract.resolution.pipelines.Raml10TransformationPipeline
import amf.plugins.domain.apicontract.models.api.WebApi
import amf.remod.ShapePayloadValidatorFactory
import org.scalatest.flatspec.AsyncFlatSpec
import org.scalatest.matchers.should

class PayloadValidationTestScala extends AsyncFlatSpec with should.Matchers {

  "AMF payload validation" should "create and use a user schema payload validator" in {
    val configuration = WebAPIConfiguration.WebAPI()
    val client = configuration.createClient()
    client.parse("file://AMF5/resources/examples/simple-api.raml") map { parseResult =>
      val transformationResult = client.transform(parseResult.bu, Raml10TransformationPipeline.name)

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
        ShapePayloadValidatorFactory.createPayloadValidator(
          userSchema,
          new ValidationConfiguration(configuration)
        )

      // invalid payload to validate against
      val invalidUserPayload = "{\"name\": \"firstname and lastname\"}"

      // .isValid is a fail-fast method useful for just checking validity
      payloadValidator.isValid("application/json", invalidUserPayload) map (_ shouldBe false)

      // .validate returns a Validation report with all results found
      payloadValidator.validate(
        "application/json",
        invalidUserPayload
      ) map (_.conforms shouldBe false)

      // .syncValidate validates synchronously
      payloadValidator.syncValidate("application/json", invalidUserPayload).conforms shouldBe false
    }

  }
}
