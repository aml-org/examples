package javaPlatform;

import amf.apicontract.client.platform.AMFBaseUnitClient;
import amf.apicontract.client.platform.OASConfiguration;
import amf.apicontract.client.platform.RAMLConfiguration;
import amf.core.client.common.transform.PipelineId;
import amf.core.client.platform.model.document.BaseUnit;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;

public class ConversionTest {

    AMFBaseUnitClient oas20Client = OASConfiguration.OAS20().baseUnitClient();
    AMFBaseUnitClient oas30Client = OASConfiguration.OAS30().baseUnitClient();
    AMFBaseUnitClient raml10Client = RAMLConfiguration.RAML10().baseUnitClient();

    @Test
    public void Raml10ToOas20Conversion() throws ExecutionException, InterruptedException, IOException {

        final BaseUnit ramlApi = raml10Client.parse("file://src/test/resources/examples/banking-api.raml").get().baseUnit();
        final BaseUnit convertedOas = oas20Client.transform(ramlApi, PipelineId.Compatibility()).baseUnit();
        final String result = oas20Client.render(convertedOas).trim();

        Path path = Paths.get("src/test/resources/expected/converted-banking-api.json");
        String read = Files.readAllLines(path).stream().collect(Collectors.joining(System.lineSeparator()));
        assertEquals(read, result);
    }

    @Test
    public void Oas20ToRaml10Conversion() throws ExecutionException, InterruptedException, IOException {
        final BaseUnit oasApi = oas20Client.parse("file://src/test/resources/examples/banking-api.json").get().baseUnit();
        final BaseUnit convertedRaml = raml10Client.transform(oasApi, PipelineId.Compatibility()).baseUnit();
        final String result = raml10Client.render(convertedRaml).trim();

        Path path = Paths.get("src/test/resources/expected/converted-banking-api.raml");
        String read = Files.readAllLines(path).stream().collect(Collectors.joining(System.lineSeparator()));
        assertEquals(read, result);
    }

    @Test
    public void Oas30ToRaml10Conversion() throws ExecutionException, InterruptedException, IOException {
        final BaseUnit oasApi = oas30Client.parse("file://src/test/resources/examples/sample-api.yaml").get().baseUnit();
        final BaseUnit convertedRaml = raml10Client.transform(oasApi, PipelineId.Compatibility()).baseUnit();
        final String result = raml10Client.render(convertedRaml).trim();

        Path path = Paths.get("src/test/resources/expected/converted-sample-api.raml");
        String read = Files.readAllLines(path).stream().collect(Collectors.joining(System.lineSeparator()));
        assertEquals(read, result);
    }
}
