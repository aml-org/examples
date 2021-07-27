package javaPlatform;

import amf.apicontract.client.platform.AMFBaseUnitClient;
import amf.apicontract.client.platform.OASConfiguration;
import amf.core.client.platform.AMFResult;
import amf.core.client.platform.config.ParsingOptions;
import amf.core.client.platform.config.RenderOptions;
import amf.core.internal.remote.Mimes;
import org.junit.Test;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

public class ConfigurationOptionsTest {

    @Test
    public void configurationWithOptions() throws ExecutionException, InterruptedException, IOException {
        AMFBaseUnitClient client = OASConfiguration.OAS20()
                .withParsingOptions(new ParsingOptions().setMaxYamlReferences(20))
                .withRenderOptions(new RenderOptions().withSourceMaps())
                .baseUnitClient();

        final AMFResult parseResult = client.parse("file://src/test/resources/examples/banking-api.json").get();
        final String result = client.render(parseResult.baseUnit(), "application/ld+json").trim();
        assert(result.contains("[(87,12)-(87,39)]"));
    }
}
