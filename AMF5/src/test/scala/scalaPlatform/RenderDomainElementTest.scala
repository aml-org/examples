package scalaPlatform

import amf.apicontract.client.scala.model.domain.{Payload, Response}
import amf.apicontract.client.scala.OASConfiguration
import amf.core.client.platform.model.DataTypes
import amf.shapes.client.scala.model.domain.ScalarShape
import org.scalatest.flatspec.AsyncFlatSpec
import org.scalatest.matchers.should.Matchers
import org.yaml.model.YDocument
import org.yaml.render.JsonRender

import java.util.Arrays.asList

class RenderDomainElementTest extends AsyncFlatSpec with Matchers with FileReader {

  "AMFElementClient" should "emit a domain element" in {
    val payload = Payload()
      .withMediaType("application/json")
      .withSchema(
        ScalarShape()
          .withDataType(DataTypes.Boolean)
          .withId("aScalar"))
      .withId("somethingElse")
    val response = Response()
      .withStatusCode("401")
      .withDisplayName("My Example Response")
      .withDescription("An example response")
      .withPayloads(List(payload))
      .withId("someId")
    val client = OASConfiguration.OAS30().elementClient()
    val emittedNode = client.renderElement(response)
    val writtenString = JsonRender.render(emittedNode)
    val expected = readResource("/expected/emitted-response.json")
    writtenString shouldEqual expected
  }
}
