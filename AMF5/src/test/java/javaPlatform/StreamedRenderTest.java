package javaPlatform;

import amf.apicontract.client.platform.AMFBaseUnitClient;
import amf.apicontract.client.platform.AMFDocumentResult;
import amf.apicontract.client.platform.OASConfiguration;
import org.junit.Test;
import org.yaml.builder.JsonOutputBuilder;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.util.Date;
import java.util.concurrent.ExecutionException;

import static org.junit.Assert.assertEquals;

public class StreamedRenderTest implements FileReader{

    @Test
    public void streamedTest() throws ExecutionException, InterruptedException, IOException {
        AMFBaseUnitClient client = OASConfiguration.OAS20().baseUnitClient();
        File tmpFile = File.createTempFile("banking-api.json", String.valueOf(new Date().getTime()));
        AMFDocumentResult result = client.parseDocument("file://src/test/resources/examples/banking-api.json").get();
        assert(result.conforms());
        Writer writer = new FileWriter(tmpFile);
        JsonOutputBuilder<Writer> jsonBuilder = JsonOutputBuilder.apply(writer, false);
        client.renderGraphToBuilder(result.baseUnit(), jsonBuilder);
        writer.flush();
        writer.close();
        String goldenContent = readResource("/examples/banking-api.flattened.jsonld");
        String writtenContent = String.join("", Files.readAllLines(tmpFile.toPath()));
        assertEquals(removeWhiteSpaces(writtenContent), removeWhiteSpaces(goldenContent));
    }

    private String removeWhiteSpaces(String input) {
        return input.replaceAll("\\s+", "");
    }
}
