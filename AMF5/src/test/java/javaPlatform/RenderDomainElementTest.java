package javaPlatform;

import amf.apicontract.client.platform.AMFElementClient;
import amf.apicontract.client.platform.OASConfiguration;
import amf.apicontract.client.platform.model.domain.Payload;
import amf.apicontract.client.platform.model.domain.Response;
import amf.core.client.platform.model.DataTypes;
import amf.shapes.client.platform.model.domain.ScalarShape;
import org.junit.Test;
import org.yaml.builder.JsonOutputBuilder;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.util.Date;

import static java.util.Collections.*;
import static javaPlatform.StringEquals.assertIgnoringWhitespace;

public class RenderDomainElementTest implements FileReader{

    @Test
    public void renderDomainElement() throws IOException {
        File tmpFile = File.createTempFile("rendered-response.json", String.valueOf(new Date().getTime()));
        Writer writer = new FileWriter(tmpFile);
        JsonOutputBuilder<Writer> jsonBuilder = JsonOutputBuilder.apply(writer, false);
        AMFElementClient client = OASConfiguration.OAS30().elementClient();
        Payload payload = new Payload()
                .withMediaType("application/json")
                .withSchema(new ScalarShape().withDataType(DataTypes.Boolean()).withId("aScalar"))
                .withId("somethingElse");
        Response response = (Response) new Response()
                .withStatusCode("401")
                .withDisplayName("My Example Response")
                .withDescription("An example response")
                .withPayloads(singletonList(payload))
                .withId("someId");
        client.renderToBuilder(response, jsonBuilder);
        writer.flush();
        writer.close();
        String goldenContent = readResource("/expected/rendered-response.json");
        String writtenContent = String.join("", Files.readAllLines(tmpFile.toPath()));
        assertIgnoringWhitespace(writtenContent, goldenContent);
    }
}
