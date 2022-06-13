package scalaPlatform

import amf.apicontract.client.scala.OASConfiguration
import amf.core.client.platform.model.DataTypes
import amf.shapes.client.scala.model.domain.ScalarShape
import org.junit.runner.RunWith
import org.scalatest.flatspec.AsyncFlatSpec
import org.scalatest.matchers.should.Matchers
import org.scalatestplus.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class ShapeSerializationTest extends AsyncFlatSpec with Matchers with FileReader {

  "AMFElementClient" should "serialize to json schema" in {
    val shape = ScalarShape()
      .withId("someId")
      .withDataType(DataTypes.Double)
      .withMinimum(2)
      .withMaximum(7)
    val client = OASConfiguration.OAS30().elementClient()
    val jsonSchemaString = client.toJsonSchema(shape)
    val expected = readResource("/expected/draft4-scalar-schema.json")
    jsonSchemaString shouldEqual expected
  }
}
