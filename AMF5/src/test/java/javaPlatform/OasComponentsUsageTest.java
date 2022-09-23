package javaPlatform;

import amf.apicontract.client.platform.AMFBaseUnitClient;
import amf.apicontract.client.platform.OASConfiguration;
import amf.apicontract.client.platform.model.document.ComponentModule;
import amf.apicontract.client.platform.model.domain.Payload;
import amf.apicontract.client.platform.model.domain.api.Api;
import amf.core.client.platform.AMFParseResult;
import amf.core.client.platform.model.document.Document;
import org.junit.Test;

import java.util.concurrent.ExecutionException;

import static org.junit.Assert.assertTrue;

public class OasComponentsUsageTest {

    private final String apiPath = "file://src/test/resources/examples/components/api.yaml";
    private final String componentsPath = "file://src/test/resources/examples/components/oas-3-component.yaml";
    private final String aliasPath = "file://src/test/resources/examples/components/myComponents.yaml";

    @Test
    public void parseAndUseOasComponent() throws ExecutionException, InterruptedException {
        AMFBaseUnitClient client = OASConfiguration.OAS30Component().baseUnitClient();
        AMFParseResult componentResult = client.parse(componentsPath).get();
        ComponentModule component = (ComponentModule) componentResult.baseUnit();
        assertTrue(componentResult.conforms());

        ImmutableCache cache = new ImmutableCache().add(aliasPath, componentsPath, component);
        AMFBaseUnitClient apiClient = OASConfiguration.OAS30().withUnitCache(cache).baseUnitClient();
        AMFParseResult apiResult = apiClient.parse(apiPath).get();
        assertTrue(apiResult.conforms());

        Document doc = (Document) apiResult.baseUnit();
        Api<?> api = (Api<?>) doc.encodes();
        Payload payload = api.endPoints().get(0).operations().get(0).responses().get(0).payloads().get(0);
        assertTrue(payload.schema().isLink());
        assertTrue(payload.examples().get(0).isLink());
    }
}
