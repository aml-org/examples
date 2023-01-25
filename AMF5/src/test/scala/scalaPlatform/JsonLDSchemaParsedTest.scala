package scalaPlatform

import amf.shapes.client.scala.config.{JsonLDSchemaConfiguration, JsonLDSchemaConfigurationClient}
import amf.shapes.client.scala.model.domain.jsonldinstance.JsonLDObject
import org.junit.runner.RunWith
import org.scalatest.funsuite.AsyncFunSuite
import org.scalatest.matchers.should.Matchers
import org.scalatestplus.junit.JUnitRunner

import scala.concurrent.ExecutionContext

@RunWith(classOf[JUnitRunner])
class JsonLDSchemaParsedTest extends AsyncFunSuite with Matchers{
  override implicit def executionContext: ExecutionContext = ExecutionContext.Implicits.global


  val client: JsonLDSchemaConfigurationClient =
    JsonLDSchemaConfiguration.JsonLDSchema().baseUnitClient()


  test("Parse JsonLDInstances with schema") {
    for {
      schema     <- client.parseJsonLDSchema(s"file://src/test/resources/jsonld-schemas/schema.json").map(_.jsonDocument)
      instance   <- client.parseJsonLDInstance(s"file://src/test/resources/jsonld-schemas/instance.json", schema)
    } yield {
      val graph = instance.instance.encodes.head.asInstanceOf[JsonLDObject].graph
      graph.containsProperty("anypoint://vocabulary/security.yaml#sensitive") shouldBe true
    }
  }
}
