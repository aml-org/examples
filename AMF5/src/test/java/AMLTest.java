import amf.client.exported.AMLClient;
import amf.client.exported.AMLConfiguration;
import amf.client.exported.AMLDialectInstanceResult;
import amf.client.exported.AMLDialectResult;
import amf.client.model.document.Dialect;
import amf.client.model.domain.DialectDomainElement;
import org.junit.Test;

import java.util.concurrent.ExecutionException;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;

public class AMLTest {

    private final String simpleDialect = "file://resources/examples/dialect.yaml";
    private final String simpleDialectInstance = "file://resources/examples/dialect-instance.yaml";
    private final String simpleNodeTypeUri = "file://resources/examples/dialect.yaml#/declarations/Simple";

    @Test
    public void parseDialect() throws ExecutionException, InterruptedException {
        final AMLClient client = AMLConfiguration.predefined().createClient();
        final AMLDialectResult parseResult = client.parseDialect(simpleDialect).get();
        assertTrue(parseResult.conforms());
        final Dialect dialect = (Dialect) parseResult.baseUnit();
        final String dialectElementId = dialect.documents().root().encoded().value();
        assertEquals("file://resources/examples/dialect.yaml#/declarations/Simple", dialectElementId);
    }

    @Test
    public void parseDialectInstance() throws ExecutionException, InterruptedException {
        final AMLClient client = AMLConfiguration.predefined().withDialect(simpleDialect).get().createClient();
        final AMLDialectInstanceResult parseResult = client.parseDialectInstance(simpleDialectInstance).get();
        assertTrue(parseResult.conforms());
        final DialectDomainElement instanceElement = parseResult.dialectInstance().encodes();
        assertTrue(instanceElement.getTypeUris().contains(simpleNodeTypeUri));
    }
}
