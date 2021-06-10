import amf.client.exported.AMFClient;
import amf.client.exported.WebAPIConfiguration;
import amf.client.model.document.BaseUnit;
import amf.client.remod.amfcore.resolution.PipelineName;
import amf.core.remote.Oas20;
import amf.core.remote.Raml10;
import amf.core.resolution.pipelines.TransformationPipeline;
import amf.plugins.document.apicontract.resolution.pipelines.compatibility.Oas20CompatibilityPipeline;
import org.junit.BeforeClass;
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
        AMFClient client = WebAPIConfiguration.WebAPI().createClient();

        final BaseUnit ramlApi = client.parse("file://resources/examples/banking-api.raml").get().baseUnit();
        final String pipelineName = PipelineName.from(Oas20.name(), TransformationPipeline.COMPATIBILITY_PIPELINE());
        final BaseUnit convertedOas = client.transform(ramlApi, pipelineName).baseUnit();
        final String result = client.render(convertedOas,"application/oas20+json").trim();

        Path path = Paths.get("resources/expected/converted-banking-api.json");
        String read = Files.readAllLines(path).stream().collect(Collectors.joining(System.lineSeparator()));
        assertEquals(read, result);
    }

    @Test
    public void Oas20ToRaml10Conversion() throws ExecutionException, InterruptedException, IOException {
        AMFClient client = WebAPIConfiguration.WebAPI().createClient();

        final BaseUnit oasApi = client.parse("file://resources/examples/banking-api.json").get().baseUnit();
        final String pipelineName = PipelineName.from(Raml10.name(), TransformationPipeline.COMPATIBILITY_PIPELINE());
        final BaseUnit convertedRaml = client.transform(oasApi, pipelineName).baseUnit();
        final String result = client.render(convertedRaml,"application/raml10+yaml").trim();

        Path path = Paths.get("resources/expected/converted-banking-api.raml");
        String read = Files.readAllLines(path).stream().collect(Collectors.joining(System.lineSeparator()));
        assertEquals(read, result);
    }
}
