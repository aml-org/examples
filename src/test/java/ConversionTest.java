import amf.client.AMF;
import amf.client.model.document.BaseUnit;
import amf.client.parse.Oas20Parser;
import amf.client.parse.Raml10Parser;
import amf.client.render.Oas20Renderer;
import amf.client.render.Raml10Renderer;
import amf.client.resolve.Oas20Resolver;
import amf.client.resolve.Raml10Resolver;
import amf.core.resolution.pipelines.ResolutionPipeline;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class ConversionTest {

    @BeforeClass
    public static void setup() throws ExecutionException, InterruptedException {
        AMF.init().get();
    }

    @Test
    public void Raml10ToOas20Conversion() throws ExecutionException, InterruptedException, IOException {
        final Raml10Parser parser = new Raml10Parser();
        final Oas20Resolver resolver = new Oas20Resolver();
        final Oas20Renderer renderer = new Oas20Renderer();

        final BaseUnit ramlApi = parser.parseFileAsync("file://resources/examples/banking-api.raml").get();
        final BaseUnit convertedOasApi = resolver.resolve(ramlApi, ResolutionPipeline.COMPATIBILITY_PIPELINE());

        final String result = renderer.generateString(convertedOasApi).get().trim();
        Path path = Paths.get("resources/expected/converted-banking-api.json");
        String read = Files.readAllLines(path).stream().collect(Collectors.joining(System.lineSeparator()));
        assertEquals(read, result);
    }

    @Test
    public void Oas20ToRaml10Conversion() throws ExecutionException, InterruptedException, IOException {
        final Oas20Parser parser = new Oas20Parser();
        final Raml10Resolver resolver = new Raml10Resolver();
        final Raml10Renderer renderer = new Raml10Renderer();

        final BaseUnit oasApi = parser.parseFileAsync("file://resources/examples/banking-api.json").get();
        final BaseUnit convertedRamlApi = resolver.resolve(oasApi, ResolutionPipeline.COMPATIBILITY_PIPELINE());

        // has amf-specific fields for cross-spec conversion support
        final String result = renderer.generateString(convertedRamlApi).get().trim();
        Path path = Paths.get("resources/expected/converted-banking-api.raml");
        String read = Files.readAllLines(path).stream().collect(Collectors.joining(System.lineSeparator()));
        assertEquals(read, result);
    }
}
