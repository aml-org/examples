import amf.client.exported.AMFClient;
import amf.client.exported.AMFConfiguration;
import amf.client.exported.OASConfiguration;
import amf.client.exported.RAMLConfiguration;
import amf.client.model.document.BaseUnit;
import amf.client.model.document.Document;
import amf.client.model.domain.EndPoint;
import amf.client.model.domain.Operation;
import amf.client.model.domain.WebApi;
import amf.client.remod.amfcore.resolution.PipelineName;
import amf.core.remote.Oas30;
import amf.core.remote.Raml10;
import amf.core.resolution.pipelines.TransformationPipeline;
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

        final String pipelineName = PipelineName.from(Raml10.name(), TransformationPipeline.DEFAULT_PIPELINE());
        final BaseUnit resolvedModel = client.transform(unresolvedModel, pipelineName).baseUnit();
        assertNotNull(resolvedModel);
        final WebApi api = (WebApi) ((Document) resolvedModel).encodes();
        assertFalse(api.endPoints().get(0).operations().isEmpty());
    }

    @Test
    public void resolveOas30() throws ExecutionException, InterruptedException {
        final AMFClient client = OASConfiguration.OAS30().createClient();

        final BaseUnit unresolvedModel = client.parse("file://resources/examples/banking-api-oas30.json").get().baseUnit();
        assertNotNull(unresolvedModel);

        final String pipelineName = PipelineName.from(Oas30.name(), TransformationPipeline.DEFAULT_PIPELINE());
        final BaseUnit resolvedModel = client.transform(unresolvedModel, pipelineName).baseUnit();
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

        final String pipelineName = PipelineName.from(Raml10.name(), TransformationPipeline.DEFAULT_PIPELINE());
        final Document resolvedModel = (Document) client.transform(unresolvedModel, pipelineName).baseUnit();
        assertTrue("resolved model shouldn't reference anything", resolvedModel.references().size() == 0);
        final WebApi api = (WebApi) ((Document) resolvedModel).encodes();
        assertTrue(api.endPoints().size() > 1);

    }
}
