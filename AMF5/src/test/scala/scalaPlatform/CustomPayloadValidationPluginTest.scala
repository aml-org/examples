package scalaPlatform

import amf.apicontract.client.scala.OASConfiguration
import amf.core.client.common.validation.StrictValidationMode
import amf.core.client.platform.model.DataTypes
import amf.shapes.client.scala.model.domain.ScalarShape
import org.scalatest.flatspec.AsyncFlatSpec
import org.scalatest.matchers.should.Matchers

class CustomPayloadValidationPluginTest extends AsyncFlatSpec with Matchers {

  val CUSTOM_MEDIATYPE = "application/my-cool-mediatype"

  "AMF" should "let the user plugin and use a custom payload validation plugin" in {

    val config = OASConfiguration.OAS30().withPlugin(new CustomShapePayloadPlugin(CUSTOM_MEDIATYPE))
    val booleanOnlyShape = ScalarShape().withDataType(DataTypes.Boolean);
    val validator = config.elementClient().payloadValidatorFor(booleanOnlyShape, CUSTOM_MEDIATYPE, StrictValidationMode);
    validator.validate("somethingNotRelevant").map { report =>
      report.conforms shouldBe true
      validator shouldBe a[MySpyShapeValidator]
      validator.asInstanceOf[MySpyShapeValidator].isExecuted shouldBe true
    }
  }
}
