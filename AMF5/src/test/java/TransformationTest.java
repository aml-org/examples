import amf.Core;
import amf.client.exported.AMFClient;
import amf.client.exported.AMFConfiguration;
import amf.client.exported.OASConfiguration;
import amf.client.exported.RAMLConfiguration;
import amf.client.model.document.BaseUnit;
import amf.client.model.document.Document;
import amf.client.remod.amfcore.resolution.PipelineName;
import amf.core.remote.Oas30;
import amf.core.remote.Raml10;
import amf.core.resolution.pipelines.TransformationPipeline;
import amf.plugins.document.WebApi;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.concurrent.ExecutionException;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class TransformationTest {

    @Test
    public void resolveRaml10Compatibility() throws ExecutionException, InterruptedException {
        final AMFClient client = RAMLConfiguration.RAML10().createClient();

        final BaseUnit unresolvedModel = client.parse("file://resources/examples/banking-api.raml").get().baseUnit();
        assertNotNull(unresolvedModel);

        final String pipelineName = PipelineName.from(Raml10.name(), TransformationPipeline.COMPATIBILITY_PIPELINE());
        final BaseUnit resolvedModel = client.transform(unresolvedModel, pipelineName).baseUnit();
        assertNotNull(resolvedModel);

        // has amf-specific fields for cross-spec conversion support
//        System.out.println(client.render(resolvedModel, "application/raml10+yaml"));
    }

    @Test
    public void resolveOas30() throws ExecutionException, InterruptedException {
        final AMFClient client = OASConfiguration.OAS30().createClient();

        final BaseUnit unresolvedModel = client.parse("file://resources/examples/banking-api-oas30.json").get().baseUnit();
        assertNotNull(unresolvedModel);

        final String pipelineName = PipelineName.from(Oas30.name(), TransformationPipeline.DEFAULT_PIPELINE());
        final BaseUnit resolvedModel = client.transform(unresolvedModel, pipelineName).baseUnit();
        assertNotNull(resolvedModel);

        // it's identical to the source file because all schemas and parameters where already inlined and the default pipeline was used
//        System.out.println(client.render(resolvedModel, "application/oas30+json"));
    }

    @Test
    public void resolveRamlOverlay() throws ExecutionException, InterruptedException {
        final AMFClient client = RAMLConfiguration.RAML10().createClient();

        final Document unresolvedModel = client.parseDocument("file://resources/examples/raml-overlay/test-overlay.raml").get().document();
        assertTrue("unresolved overlay should reference main API", unresolvedModel.references().size() == 1);

        final String pipelineName = PipelineName.from(Raml10.name(), TransformationPipeline.DEFAULT_PIPELINE());
        final Document resolvedModel = (Document) client.transform(unresolvedModel, pipelineName).baseUnit();
        assertTrue("resolved model shouldn't reference anything", resolvedModel.references().size() == 0);

//        System.out.println(client.render(resolvedModel, "application/raml10+yaml"));

    }
}
