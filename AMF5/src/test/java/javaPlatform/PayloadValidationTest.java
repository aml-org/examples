package javaPlatform;

import amf.apicontract.client.platform.AMFClient;
import amf.apicontract.client.platform.RAMLConfiguration;
import amf.apicontract.client.platform.model.domain.EndPoint;
import amf.apicontract.client.platform.model.domain.Operation;
import amf.apicontract.client.platform.model.domain.Payload;
import amf.apicontract.client.platform.model.domain.Request;
import amf.apicontract.client.platform.model.domain.api.WebApi;
import amf.core.client.common.validation.ValidationMode;
import amf.core.client.platform.model.document.BaseUnit;
import amf.core.client.platform.model.document.Document;
import amf.core.client.platform.validation.payload.AMFShapePayloadValidator;
import amf.core.client.platform.validation.AMFValidationReport;
import amf.shapes.client.platform.model.domain.AnyShape;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.concurrent.ExecutionException;

import static org.junit.Assert.assertFalse;

public class PayloadValidationTest {

    private static AMFShapePayloadValidator payloadValidator;

    @BeforeClass
    public static void setup() throws ExecutionException, InterruptedException {
        final AMFClient client = RAMLConfiguration.RAML10().createClient();

        final BaseUnit unresolvedModel = client.parse("file://resources/examples/simple-api.raml").get().baseUnit();
        final BaseUnit resolvedModel = client.transform(unresolvedModel).baseUnit();

        // get the model.encodes() to isolate the WebApi model
        final WebApi webApi = (WebApi) ((Document) resolvedModel).encodes();
        final EndPoint usersEndpoint = webApi.endPoints().get(0);
        final Operation postMethod = usersEndpoint.operations().get(0);
        final Request request = postMethod.requests().get(0);
        final Payload userPayload = request.payloads().get(0);
        final AnyShape userSchema = (AnyShape) userPayload.schema();

        payloadValidator = client.getConfiguration()
                .payloadValidatorFactory().createFor(userSchema, "application/json", ValidationMode.StrictValidationMode());
    }

    @Test
    public void validateTest() throws ExecutionException, InterruptedException {
        final String invalidUserPayload = "{\"name\": \"firstname and lastname\"}";
        final AMFValidationReport validationReport = payloadValidator.validate(invalidUserPayload).get();
        assertFalse(validationReport.conforms());
    }

    @Test
    public void syncValidateTest() {
        final String invalidUserPayload = "{\"name\": \"firstname and lastname\"}";
        final AMFValidationReport validationReport = payloadValidator.syncValidate(invalidUserPayload);
        assertFalse(validationReport.conforms());
    }
}
