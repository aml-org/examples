package javaPlatform;

import amf.apicontract.client.common.ProvidedMediaType;
import amf.apicontract.client.platform.AMFBaseUnitClient;
import amf.apicontract.client.platform.OASConfiguration;
import amf.apicontract.client.platform.WebAPIConfiguration;
import amf.core.client.platform.AMFResult;
import amf.core.client.platform.config.ParsingOptions;
import amf.core.client.platform.config.RenderOptions;
import amf.core.client.platform.model.document.BaseUnit;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;

public class ConfigurationOptionsTest {

    @Test
    public void configurationWithOptions() throws ExecutionException, InterruptedException, IOException {
        AMFBaseUnitClient client = OASConfiguration.OAS20()
                .withParsingOptions(new ParsingOptions().setMaxYamlReferences(20))
                .withRenderOptions(new RenderOptions().withSourceMaps())
                .baseUnitClient();

        final AMFResult parseResult = client.parse("file://src/test/resources/examples/banking-api.json").get();
        final String result = client.render(parseResult.baseUnit()).trim();
        assert(result.contains("[(87,12)-(87,39)]"));
    }
}
