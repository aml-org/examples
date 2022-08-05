package scalaPlatform

import amf.core.client.scala.model.domain.Shape
import amf.shapes.client.scala.config.JsonSchemaConfiguration
import amf.shapes.client.scala.model.document.JsonSchemaDocument
import org.junit.Assert.{assertEquals, assertNotNull, assertTrue}
import org.junit.runner.RunWith
import org.scalatest.flatspec.AsyncFlatSpec
import org.scalatest.matchers.must.Matchers.convertToAnyMustWrapper
import org.scalatest.matchers.should
import org.scalatestplus.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class JSONSchemaParsingTest extends AsyncFlatSpec with should.Matchers {

  it should "parse JSON Schema file" in {
    val client = JsonSchemaConfiguration.JsonSchema().baseUnitClient()

    // A BaseUnit is the return type of any parsing
    // The actual object can be many different things, depending on the content of the source file
    // https://github.com/aml-org/amf/blob/develop/documentation/model.md#baseunit
    client.parse("file://src/test/resources/examples/schema.json") map { result =>

      assertNotNull(result.baseUnit)

      // In this case the BaseUnit is a JsonSchemaDocument
      result.baseUnit mustBe a[JsonSchemaDocument]
      val document = result.baseUnit.asInstanceOf[JsonSchemaDocument]

      // A JsonSchemaDocument encodes a Shape, which is the root schema of the JSON Schema
      val rootShape: Shape = document.encodes
      assertNotNull(rootShape)

      // A JsonSchemaDocument could declares a list of Shapes, which are the schemas inside the `definitions` or `$defs` key
      val declarations: Seq[Shape] = document.declares.map(de => de.asInstanceOf[Shape])

      assertTrue(declarations.nonEmpty)
      assertEquals(declarations.size, 1)

      result.conforms shouldBe true
    }
  }
}
