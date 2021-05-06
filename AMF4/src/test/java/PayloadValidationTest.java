import amf.client.AMF;
import amf.client.model.document.BaseUnit;
import amf.client.model.document.Document;
import amf.client.model.domain.*;
import amf.client.parse.Raml10Parser;
import amf.client.resolve.Raml10Resolver;
import amf.client.validate.PayloadValidator;
import amf.client.validate.ValidationReport;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.concurrent.ExecutionException;

import static org.junit.Assert.assertFalse;

public class PayloadValidationTest {

    private static PayloadValidator payloadValidator;

    @BeforeClass
    public static void setup() throws ExecutionException, InterruptedException {
        AMF.init().get();

        final Raml10Parser parser = new Raml10Parser();
        final Raml10Resolver resolver = new Raml10Resolver();

        final BaseUnit unresolvedModel = parser.parseFileAsync("file://resources/examples/simple-api.raml").get();
        final BaseUnit resolvedModel = resolver.resolve(unresolvedModel);

        // get the model.encodes() to isolate the WebApi model
        final WebApi webApi = (WebApi) ((Document) resolvedModel).encodes();
        final EndPoint usersEndpoint = webApi.endPoints().get(0);
        final Operation postMethod = usersEndpoint.operations().get(0);
        final Request request = postMethod.requests().get(0);
        final Payload userPayload = request.payloads().get(0);
        final AnyShape userSchema = (AnyShape) userPayload.schema();

        payloadValidator = userSchema.payloadValidator("application/json").get();
    }

    @Test
    public void isValidTest() throws ExecutionException, InterruptedException {
        final String invalidUserPayload = "{\"name\": \"firstname and lastname\"}";
        final Boolean result = (Boolean) payloadValidator.isValid("application/json", invalidUserPayload).get();
        assertFalse(result);
    }

    @Test
    public void validateTest() throws ExecutionException, InterruptedException {
        final String invalidUserPayload = "{\"name\": \"firstname and lastname\"}";
        final ValidationReport validationReport = payloadValidator.validate("application/json", invalidUserPayload).get();
        assertFalse(validationReport.conforms());
    }

    @Test
    public void syncValidateTest() {
        final String invalidUserPayload = "{\"name\": \"firstname and lastname\"}";
        final ValidationReport validationReport = payloadValidator.syncValidate("application/json", invalidUserPayload);
        assertFalse(validationReport.conforms());
    }
}
