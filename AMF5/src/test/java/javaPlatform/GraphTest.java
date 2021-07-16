package javaPlatform;

import amf.apicontract.client.platform.AMFBaseUnitClient;
import amf.apicontract.client.platform.WebAPIConfiguration;
import amf.core.client.platform.AMFResult;
import amf.core.client.platform.model.document.BaseUnit;
import amf.core.client.platform.model.document.Document;
import amf.core.client.platform.model.domain.DomainElement;
import org.junit.Test;

import java.util.concurrent.ExecutionException;

import static org.junit.Assert.*;
import static org.junit.Assert.assertNotNull;

public class GraphTest {

    @Test
    public void parseAMFGraph() throws ExecutionException, InterruptedException {
        final AMFBaseUnitClient client = WebAPIConfiguration.WebAPI().baseUnitClient();

        final AMFResult parseResult = client.parse("file://src/test/resources/examples/banking-api.flattened.jsonld").get();

        final BaseUnit model = parseResult.baseUnit();
        assertNotNull(model);
        assertTrue(parseResult.conforms());

        final DomainElement webApi = ((Document) model).encodes();
        assertNotNull(webApi);
    }
}
