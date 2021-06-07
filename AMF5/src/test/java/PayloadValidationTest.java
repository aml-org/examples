import amf.client.exported.AMFClient;
import amf.client.exported.RAMLConfiguration;
import amf.client.model.document.BaseUnit;
import amf.client.model.document.Document;
import amf.client.model.domain.*;
import amf.client.remod.amfcore.resolution.PipelineName;
import amf.client.validate.AMFValidationReport;
import amf.client.validate.PayloadValidator;
import amf.core.remote.Raml;
import amf.core.remote.Raml10;
import amf.core.resolution.pipelines.TransformationPipeline;
import amf.remod.ClientShapePayloadValidatorFactory;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.concurrent.ExecutionException;

import static org.junit.Assert.assertFalse;

public class PayloadValidationTest {

    private static PayloadValidator payloadValidator;

    @BeforeClass
    public static void setup() throws ExecutionException, InterruptedException {
        final AMFClient client = RAMLConfiguration.RAML10().createClient();

        final BaseUnit unresolvedModel = client.parse("file://resources/examples/simple-api.raml").get().baseUnit();
        final String pipelineName = PipelineName.from(Raml10.name(), TransformationPipeline.DEFAULT_PIPELINE());
        final BaseUnit resolvedModel = client.transform(unresolvedModel, pipelineName).baseUnit();

        // get the model.encodes() to isolate the WebApi model
        final WebApi webApi = (WebApi) ((Document) resolvedModel).encodes();
        final EndPoint usersEndpoint = webApi.endPoints().get(0);
        final Operation postMethod = usersEndpoint.operations().get(0);
        final Request request = postMethod.requests().get(0);
        final Payload userPayload = request.payloads().get(0);
        final AnyShape userSchema = (AnyShape) userPayload.schema();

        // TODO: createPayloadValidator should receive client configuration
        payloadValidator = ClientShapePayloadValidatorFactory.createPayloadValidator(userSchema, client.getConfiguration()._internal());
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
        final AMFValidationReport validationReport = payloadValidator.validate("application/json", invalidUserPayload).get();
        assertFalse(validationReport.conforms());
    }

    @Test
    public void syncValidateTest() {
        final String invalidUserPayload = "{\"name\": \"firstname and lastname\"}";
        final AMFValidationReport validationReport = payloadValidator.syncValidate("application/json", invalidUserPayload);
        assertFalse(validationReport.conforms());
    }
}
