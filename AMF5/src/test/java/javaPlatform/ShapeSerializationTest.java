package javaPlatform;

import amf.apicontract.client.platform.OASConfiguration;
import amf.apicontract.client.platform.AMFElementClient;
import amf.core.client.platform.model.DataTypes;
import amf.core.client.platform.model.domain.Shape;
import amf.shapes.client.platform.model.domain.AnyShape;
import amf.shapes.client.platform.model.domain.ScalarShape;
import org.junit.Test;

import java.io.IOException;

import static javaPlatform.StringEquals.*;

public class ShapeSerializationTest implements FileReader {

    @Test
    public void serializeToJsonSchema() throws IOException {
        AnyShape shape = (AnyShape) new ScalarShape()
                .withMinimum(2)
                .withMaximum(7)
                .withDataType(DataTypes.Double())
                .withId("something");
        AMFElementClient client = OASConfiguration.OAS20().elementClient();
        String jsonSchemaShape = client.toJsonSchema(shape);
        String expected = readResource("/expected/draft4-scalar-schema.json");
        assertIgnoringWhitespace(jsonSchemaShape, expected);
    }
}
