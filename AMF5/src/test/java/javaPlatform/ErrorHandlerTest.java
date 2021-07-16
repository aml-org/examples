package javaPlatform;

import amf.apicontract.client.platform.AMFBaseUnitClient;
import amf.apicontract.client.platform.RAMLConfiguration;
import amf.core.client.platform.AMFResult;
import amf.core.client.platform.errorhandling.ClientErrorHandler;
import amf.core.client.platform.validation.AMFValidationResult;
import org.junit.Test;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;

public class ErrorHandlerTest {


    class UnhandledErrorHandler implements ClientErrorHandler {

        @Override
        public void report(AMFValidationResult result) {
            throw new RuntimeException(result.message());
        }

        @Override
        public List<AMFValidationResult> getResults() {
            return Collections.emptyList();
        }
    }

    @Test
    public void customErrorHandler() throws ExecutionException, InterruptedException {
        final ClientErrorHandler eh = new UnhandledErrorHandler();
        final AMFBaseUnitClient client = RAMLConfiguration.RAML10()
                .withErrorHandlerProvider(() -> eh)
                .baseUnitClient();

        final AMFResult parseResult = client.parse("file://src/test/resources/examples/resolution-error.raml").get();

        assertNotNull(parseResult.baseUnit());

        assertThrows(RuntimeException.class,
                () -> client.transform(parseResult.baseUnit()));
    }
}
