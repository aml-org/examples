package javaPlatform;

import amf.aml.client.platform.AMLBaseUnitClient;
import amf.aml.client.platform.AMLConfiguration;
import amf.aml.client.platform.AMLDialectInstanceResult;
import amf.aml.client.platform.AMLDialectResult;
import amf.aml.client.platform.model.document.Dialect;
import amf.aml.client.platform.model.document.DialectInstance;
import amf.shapes.client.platform.config.SemanticJsonSchemaConfiguration;
import org.junit.Test;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

import static javaPlatform.StringEquals.assertIgnoringWhitespace;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertTrue;

public class JSONSchemaToDialectTest implements FileReader {

    final String inputJsonSchema = "src/test/resources/examples/json-schema-dialect.json";
    final String outputAmlDialect = "/examples/json-schema-dialect.yaml";
    final String jsonSchemaInstance = "src/test/resources/examples/json-instance.json";

    @Test
    public void convertJSONSchemaToDialect() throws IOException, ExecutionException, InterruptedException {
        AMLBaseUnitClient client = SemanticJsonSchemaConfiguration.predefined().baseUnitClient();
        AMLDialectResult parseResult = client.parseDialect("file://" + inputJsonSchema).get();

        assertTrue(parseResult.conforms());
        assertThat(parseResult.baseUnit(), instanceOf(Dialect.class));

        Dialect generatedDialect = parseResult.dialect();
        String readDialect = readResource(outputAmlDialect);
        String renderDialect = client.render(generatedDialect);

        assertIgnoringWhitespace(readDialect, renderDialect);
    }

    @Test
    public void validateJSONInstanceWithGeneratedDialect() throws ExecutionException, InterruptedException {
        AMLBaseUnitClient semanticJsonSchemaClient = SemanticJsonSchemaConfiguration.predefined().baseUnitClient();
        AMLDialectResult dialectResult = semanticJsonSchemaClient.parseDialect("file://" + inputJsonSchema).get();

        assertTrue(dialectResult.conforms());
        assertThat(dialectResult.baseUnit(), instanceOf(Dialect.class));

        AMLBaseUnitClient amlClient = AMLConfiguration.predefined().withDialect(dialectResult.dialect()).baseUnitClient();
        AMLDialectInstanceResult instanceResult = amlClient.parseDialectInstance("file://" + jsonSchemaInstance).get();

        assertTrue(instanceResult.conforms());
        assertThat(instanceResult.baseUnit(), instanceOf(DialectInstance.class));
    }

}
