package java;

import amf.apicontract.client.platform.AMFClient;
import amf.apicontract.client.platform.RAMLConfiguration;
import amf.core.client.platform.AMFResult;
import amf.core.client.platform.errorhandling.ClientErrorHandler;
import amf.core.client.platform.validation.ValidationResult;
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

        assertThrows(RuntimeException.class,
                () -> client.transform(parseResult.baseUnit()));
    }
}
