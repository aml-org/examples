import amf.client.AMF;
import amf.client.model.document.BaseUnit;
import amf.client.parse.Raml10Parser;
import amf.client.resolve.ClientErrorHandler;
import amf.client.resolve.Raml10Resolver;
import amf.core.parser.Range;
import amf.core.resolution.pipelines.ResolutionPipeline;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Optional;
import java.util.concurrent.ExecutionException;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;

public class ErrorHandlerTest {

    @BeforeClass
    public static void setup() throws ExecutionException, InterruptedException {
        AMF.init().get();
    }


    class UnhandledErrorHandler implements ClientErrorHandler {

        @Override
        public void reportConstraint(String id,
                                     String node,
                                     Optional<String> property,
                                     String message,
                                     Optional<Range> range,
                                     String level,
                                     Optional<String> location) {

            final String msg = String.format(
                    "Message: %s\nTarget: %s\nProperty: %s\nPosition: %s\n at location: %s",
                    message, node, property.orElse(""), range.isPresent() ? range : "", location.orElse("")
            );

            throw new RuntimeException(msg);
        }
    }

    @Test
    public void customErrorHandler() throws ExecutionException, InterruptedException {

        final Raml10Parser parser = new Raml10Parser();
        final Raml10Resolver resolver = new Raml10Resolver();

        final BaseUnit unresolvedModel = parser.parseFileAsync("file://src/test/resources/examples/resolution-error.raml").get();
        assertNotNull(unresolvedModel);

        assertThrows(RuntimeException.class,
                () -> resolver.resolve(unresolvedModel, ResolutionPipeline.DEFAULT_PIPELINE(), new UnhandledErrorHandler()));
    }
}
