package javaPlatform;

import amf.apicontract.client.platform.AMFClient;
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
        final AMFClient client = WebAPIConfiguration.WebAPI().createClient();

        final AMFResult parseResult = client.parse("file://resources/examples/banking-api.jsonld").get();

        final BaseUnit model = parseResult.baseUnit();
        assertNotNull(model);
        assertTrue(parseResult.conforms());

        final DomainElement webApi = ((Document) model).encodes();
        assertNotNull(webApi);
    }
}
