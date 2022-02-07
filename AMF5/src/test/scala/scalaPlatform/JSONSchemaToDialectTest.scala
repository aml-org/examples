package scalaPlatform

import amf.aml.client.scala.AMLConfiguration
import amf.aml.client.scala.model.document.{Dialect, DialectInstance}
import amf.shapes.client.scala.config.SemanticJsonSchemaConfiguration
import org.scalatest.flatspec.AsyncFlatSpec
import org.scalatest.matchers.should

import scala.concurrent.Future

class JSONSchemaToDialectTest extends AsyncFlatSpec with should.Matchers with FileUtils {

  val inputJsonSchema = "src/test/resources/examples/json-schema-dialect.json"
  val outputAmlDialect = "src/test/resources/examples/json-schema-dialect.yaml"
  val jsonSchemaInstance = "src/test/resources/examples/json-instance.json"

  it should "Convert JSON Schema to Dialect" in {
    val client = SemanticJsonSchemaConfiguration.predefined().baseUnitClient()
    client.parseDialect(addPrefix(inputJsonSchema)) map { parseResult =>
      parseResult.conforms shouldBe true
      parseResult.baseUnit.isInstanceOf[Dialect] shouldBe true

      val generatedDialect = parseResult.dialect
      val readDialect = getStrFromFile(outputAmlDialect)
      val renderDialect = client.render(generatedDialect)

      renderDialect.trim shouldEqual readDialect.trim
    }
  }

  it should "Validate JSON instance with generated Dialect" in {
    val semanticJsonSchemaClient = SemanticJsonSchemaConfiguration.predefined().baseUnitClient()
    for {
      dialectResult <- semanticJsonSchemaClient.parseDialect(addPrefix(inputJsonSchema))
      amlClient <-
        Future(AMLConfiguration.predefined().withDialect(dialectResult.dialect).baseUnitClient())
      instanceResult <- amlClient.parseDialectInstance(addPrefix(jsonSchemaInstance))
    } yield {
      dialectResult.conforms shouldBe true
      dialectResult.baseUnit.isInstanceOf[Dialect] shouldBe true
      instanceResult.conforms shouldBe true
      instanceResult.baseUnit.isInstanceOf[DialectInstance] shouldBe true
    }
  }

}
