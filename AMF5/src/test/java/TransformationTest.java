import amf.apicontract.client.platform.AMFClient;
import amf.apicontract.client.platform.OASConfiguration;
import amf.apicontract.client.platform.RAMLConfiguration;
import amf.apicontract.client.platform.model.domain.EndPoint;
import amf.apicontract.client.platform.model.domain.Operation;
import amf.apicontract.client.platform.model.domain.api.WebApi;
import amf.core.client.platform.model.document.BaseUnit;
import amf.core.client.platform.model.document.Document;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

import static org.junit.Assert.*;

public class TransformationTest {

    @Test
    public void resolveRaml10() throws ExecutionException, InterruptedException {
        final AMFClient client = RAMLConfiguration.RAML10().createClient();

        final BaseUnit unresolvedModel = client.parse("file://resources/examples/raml-resource-type.raml").get().baseUnit();
        assertNotNull(unresolvedModel);

        final BaseUnit resolvedModel = client.transform(unresolvedModel).baseUnit();
        assertNotNull(resolvedModel);
        final WebApi api = (WebApi) ((Document) resolvedModel).encodes();
        assertFalse(api.endPoints().get(0).operations().isEmpty());
    }

    @Test
    public void resolveOas30() throws ExecutionException, InterruptedException {
        final AMFClient client = OASConfiguration.OAS30().createClient();

        final BaseUnit unresolvedModel = client.parse("file://resources/examples/banking-api-oas30.json").get().baseUnit();
        assertNotNull(unresolvedModel);

        final BaseUnit resolvedModel = client.transform(unresolvedModel).baseUnit();
        assertNotNull(resolvedModel);

        final Document document = (Document) resolvedModel;
        final WebApi api = (WebApi) document.encodes();
        final List<Operation> operations = api.endPoints().stream().map(EndPoint::operations).flatMap(Collection::stream).collect(Collectors.toList());
        final List<Operation> operationWithoutServers = operations.stream().filter(op -> op.servers().isEmpty()).collect(Collectors.toList());
        assertTrue(operationWithoutServers.isEmpty());
        assertFalse(operations.isEmpty());
    }

    @Test
    public void resolveRamlOverlay() throws ExecutionException, InterruptedException {
        final AMFClient client = RAMLConfiguration.RAML10().createClient();

        final Document unresolvedModel = client.parseDocument("file://resources/examples/raml-overlay/test-overlay.raml").get().document();
        assertTrue("unresolved overlay should reference main API", unresolvedModel.references().size() == 1);

        final Document resolvedModel = (Document) client.transform(unresolvedModel).baseUnit();
        assertTrue("resolved model shouldn't reference anything", resolvedModel.references().size() == 0);
        final WebApi api = (WebApi) ((Document) resolvedModel).encodes();
        assertTrue(api.endPoints().size() > 1);

    }
}
