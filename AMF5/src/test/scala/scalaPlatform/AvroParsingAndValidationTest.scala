package scalaPlatform

import amf.apicontract.client.scala.model.domain.api.AsyncApi
import amf.apicontract.client.scala.{AsyncAPIConfiguration, AvroConfiguration}
import amf.core.client.common.validation.StrictValidationMode
import amf.core.client.scala.model.document.Document
import amf.shapes.client.scala.model.document.AvroSchemaDocument
import amf.shapes.internal.annotations.AVROSchemaType
import org.junit.runner.RunWith
import org.scalatest.flatspec.AsyncFlatSpec
import org.scalatest.matchers.must.Matchers.convertToAnyMustWrapper
import org.scalatest.matchers.should
import org.scalatestplus.junit.JUnitRunner

/** <a href="https://a.ml/docs/related-docs/avro_schema_document">AVRO Support Guide</a> **/
@RunWith(classOf[JUnitRunner])
class AvroParsingAndValidationTest extends AsyncFlatSpec with should.Matchers {
  private val config = AvroConfiguration.Avro()
  private val client = config.baseUnitClient()

  it should "parse an Async API with an AVRO Schema in it" in {
    val asyncClient = AsyncAPIConfiguration.Async20().baseUnitClient()
    asyncClient.parse("file://src/test/resources/examples/avro/asyncapi.yaml") map { result =>
      result.baseUnit mustBe a[Document]
      result.conforms shouldBe true

      val asyncApi = result.baseUnit.asInstanceOf[Document].encodes.asInstanceOf[AsyncApi]
      val channel = asyncApi.endPoints.head
      val request = channel.operations.head.requests.head
      val messagePayload = request.payloads.head.schema
      messagePayload.annotations.contains(classOf[AVROSchemaType]) shouldBe true
    }
  }

  it should "parse an AVRO json file and generate an AVRO Document" in {
    client.parse("file://src/test/resources/examples/avro/record.json") map { result =>
      result.baseUnit mustBe a[AvroSchemaDocument]
      result.conforms shouldBe true
    }
  }

  it should "parse an AVRO avsc and generate an AVRO Document" in {
    client.parse("file://src/test/resources/examples/avro/record.avsc") map { result =>
      result.baseUnit mustBe a[AvroSchemaDocument]
      result.conforms shouldBe true
    }
  }

  it should "transform and validate an AVRO Document" in {
    client.parse("file://src/test/resources/examples/avro/record.avsc") flatMap { result =>
      result.baseUnit mustBe a[AvroSchemaDocument]
      result.conforms shouldBe true

      val transformResult = client.transform(result.baseUnit)
      transformResult.conforms shouldBe true

      client.validate(transformResult.baseUnit) map { validationResult =>
        validationResult.conforms shouldBe true
      }
    }
  }

  it should "create a payload validator for an AVRO" in {
    client.parse("file://src/test/resources/examples/avro/record.avsc") map { result =>
      result.baseUnit mustBe a[AvroSchemaDocument]
      result.conforms shouldBe true
      val avroShape = result.baseUnit.asInstanceOf[AvroSchemaDocument].encodes
      val payloadValidator =
        config.elementClient().payloadValidatorFor(avroShape, "application/json", StrictValidationMode)

      val report = payloadValidator.syncValidate("{}") // invalid payload
      report.conforms shouldBe false
    }
  }

}
