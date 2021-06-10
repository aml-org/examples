import amf.client.exported.*;
import amf.client.model.document.BaseUnit;
import amf.client.model.document.Document;
import amf.client.model.domain.DomainElement;
import amf.core.remote.Amf;
import amf.plugins.document.WebApi;
import org.junit.BeforeClass;
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
