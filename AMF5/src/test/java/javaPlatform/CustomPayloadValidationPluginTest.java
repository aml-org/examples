package javaPlatform;

import amf.apicontract.client.platform.AMFConfiguration;
import amf.apicontract.client.platform.OASConfiguration;
import amf.core.client.common.validation.ValidationMode;
import amf.core.client.platform.model.DataTypes;
import amf.core.client.platform.validation.AMFValidationReport;
import amf.core.client.platform.validation.payload.AMFShapePayloadValidator;
import amf.shapes.client.platform.model.domain.ScalarShape;
import org.junit.Test;

public class CustomPayloadValidationPluginTest {

    final String CUSTOM_MEDIATYPE = "application/my-cool-mediatype";

    @Test
    public void customPluginTest() {
        AMFConfiguration config = OASConfiguration.OAS30()
                .withShapePayloadPlugin(new CustomShapePayloadValidationPlugin(CUSTOM_MEDIATYPE));
        ScalarShape booleanOnlyShape = new ScalarShape().withDataType(DataTypes.Boolean());
        AMFShapePayloadValidator validator = config.elementClient().payloadValidatorFor(booleanOnlyShape, CUSTOM_MEDIATYPE, ValidationMode.StrictValidationMode());
        AMFValidationReport report = validator.syncValidate("somethingNotRelevant");
        assert(report.conforms());
    }
}
