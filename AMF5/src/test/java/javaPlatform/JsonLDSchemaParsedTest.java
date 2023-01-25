package javaPlatform;

import amf.shapes.client.platform.JsonLDInstanceResult;
import amf.shapes.client.platform.JsonLDSchemaResult;
import amf.shapes.client.platform.config.JsonLDSchemaConfiguration;
import amf.shapes.client.platform.config.JsonLDSchemaConfigurationClient;
import amf.shapes.client.platform.model.domain.jsonldinstance.JsonLDObject;
import org.junit.Test;

import java.util.concurrent.ExecutionException;

import static org.junit.Assert.assertTrue;

public class JsonLDSchemaParsedTest {
    @Test
    public void parseJsonSchema() throws ExecutionException, InterruptedException{
        final JsonLDSchemaConfigurationClient client = JsonLDSchemaConfiguration.JsonLDSchema().baseUnitClient();

        JsonLDSchemaResult jsonLDSchemaResult = client.parseJsonLDSchema("file://src/test/resources/jsonld-schemas/schema.json").get();
        JsonLDInstanceResult jsonLDInstanceResult = client.parseJsonLDInstance("file://src/test/resources/jsonld-schemas/instance.json", jsonLDSchemaResult.jsonDocument()).get();
        JsonLDObject jsonLDObject = (JsonLDObject) jsonLDInstanceResult.instance().encodes().get(0);
        assertTrue(jsonLDObject.graph().containsProperty("anypoint://vocabulary/security.yaml#sensitive"));
    }


}
