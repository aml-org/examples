import amf.client.model.document.BaseUnit;
import amf.client.model.document.Document;
import amf.client.parse.*;
import amf.client.render.Oas30Renderer;
import amf.client.render.Raml10Renderer;
import amf.client.resolve.Oas30Resolver;
import amf.client.resolve.Raml10Resolver;
import amf.core.resolution.pipelines.ResolutionPipeline;
import org.junit.BeforeClass;
import org.junit.Test;
import amf.plugins.document.WebApi;
import amf.Core;

import java.util.concurrent.ExecutionException;

import static org.junit.Assert.*;
import static org.hamcrest.MatcherAssert.*;

public class ResolutionTest {

    @BeforeClass
    public static void setup() throws ExecutionException, InterruptedException {
        WebApi.register();
        Core.init().get();
    }


    @Test
    public void resolveRaml() throws ExecutionException, InterruptedException {
        final Raml10Parser parser = new Raml10Parser();
        final Raml10Resolver resolver = new Raml10Resolver();

        final BaseUnit unresolvedModel = parser.parseFileAsync("file://resources/examples/banking-api.raml").get();
        assertNotNull(unresolvedModel);

        final BaseUnit resolvedModel = resolver.resolve(unresolvedModel, ResolutionPipeline.COMPATIBILITY_PIPELINE());
        assertNotNull(resolvedModel);

        // has amf-specific fields for cross-spec conversion support
        System.out.println(new Raml10Renderer().generateString(resolvedModel).get());
    }

    @Test
    public void resolveOas() throws ExecutionException, InterruptedException {
        final Oas30Parser parser = new Oas30Parser();
        final Oas30Resolver resolver = new Oas30Resolver();

        final BaseUnit unresolvedModel = parser.parseFileAsync("file://resources/examples/banking-api-oas30.json").get();
        assertNotNull(unresolvedModel);

        final BaseUnit resolvedModel = resolver.resolve(unresolvedModel);
        assertNotNull(resolvedModel);

        // it's identical to the source file because all schemas and parameters where already inlined and the default pipeline was used
        System.out.println(new Oas30Renderer().generateString(resolvedModel).get());
    }

    @Test
    public void resolveRamlOverlay() throws ExecutionException, InterruptedException {
        final Raml10Parser parser = new Raml10Parser();
        final Raml10Resolver resolver = new Raml10Resolver();

        final Document unresolvedModel = (Document) parser.parseFileAsync("file://resources/examples/raml-overlay/test-overlay.raml").get();
        assertThat("unresolved overlay should reference main API", unresolvedModel.references().size() == 1);

        final Document resolvedModel = (Document) resolver.resolve(unresolvedModel, ResolutionPipeline.EDITING_PIPELINE());
        assertThat("resolved model shouldn't reference anything", resolvedModel.references().size() == 0);

        System.out.println(new Raml10Renderer().generateString(resolvedModel).get());
    }
}
