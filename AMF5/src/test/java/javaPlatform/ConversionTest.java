package javaPlatform;

import amf.apicontract.client.common.ProvidedMediaType;
import amf.apicontract.client.platform.AMFBaseUnitClient;
import amf.apicontract.client.platform.WebAPIConfiguration;
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

    @Test
    public void Raml10ToOas20Conversion() throws ExecutionException, InterruptedException, IOException {
        AMFBaseUnitClient client = WebAPIConfiguration.WebAPI().baseUnitClient();

        final BaseUnit ramlApi = client.parse("file://src/test/resources/examples/banking-api.raml").get().baseUnit();
        final BaseUnit convertedOas = client.transformCompatibility(ramlApi, ProvidedMediaType.Oas20()).baseUnit();
        final String result = client.render(convertedOas,"application/oas20+json").trim();

        Path path = Paths.get("src/test/resources/expected/converted-banking-api.json");
        String read = Files.readAllLines(path).stream().collect(Collectors.joining(System.lineSeparator()));
        assertEquals(read, result);
    }

    @Test
    public void Oas20ToRaml10Conversion() throws ExecutionException, InterruptedException, IOException {
        AMFBaseUnitClient client = WebAPIConfiguration.WebAPI().baseUnitClient();

        final BaseUnit oasApi = client.parse("file://src/test/resources/examples/banking-api.json").get().baseUnit();
        final BaseUnit convertedRaml = client.transformCompatibility(oasApi, ProvidedMediaType.Raml10()).baseUnit();
        final String result = client.render(convertedRaml,"application/raml10+yaml").trim();

        Path path = Paths.get("src/test/resources/expected/converted-banking-api.raml");
        String read = Files.readAllLines(path).stream().collect(Collectors.joining(System.lineSeparator()));
        assertEquals(read, result);
    }
}
