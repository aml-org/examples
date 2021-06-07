import amf.client.exported.AMFClient;
import amf.client.exported.AMFResult;
import amf.client.exported.ErrorHandlerProvider;
import amf.client.exported.RAMLConfiguration;
import amf.client.model.document.BaseUnit;
import amf.client.remod.amfcore.resolution.PipelineName;
import amf.client.resolve.ClientErrorHandler;
import amf.client.validate.ValidationResult;
import amf.core.errorhandling.UnhandledErrorHandler;
import amf.core.parser.Range;
import amf.core.remote.Raml10;
import amf.core.resolution.pipelines.TransformationPipeline;
import amf.plugins.document.apicontract.resolution.pipelines.Raml10TransformationPipeline;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;

public class ErrorHandlerTest {


    class UnhandledErrorHandler implements ClientErrorHandler {

        @Override
        public void report(ValidationResult result) {
            throw new RuntimeException(result.message());
        }

        @Override
        public List<ValidationResult> getResults() {
            return Collections.emptyList();
        }
    }

    @Test
    public void customErrorHandler() throws ExecutionException, InterruptedException {
        final ClientErrorHandler eh = new UnhandledErrorHandler();
        final AMFClient client = RAMLConfiguration.RAML10()
                .withErrorHandlerProvider(() -> eh)
                .createClient();

        final AMFResult parseResult = client.parse("file://resources/examples/resolution-error.raml").get();

        assertNotNull(parseResult.baseUnit());

        final String pipelineName = PipelineName.from(Raml10.name(), TransformationPipeline.DEFAULT_PIPELINE());
        assertThrows(RuntimeException.class,
                () -> client.transform(parseResult.baseUnit(), pipelineName));
    }
}
