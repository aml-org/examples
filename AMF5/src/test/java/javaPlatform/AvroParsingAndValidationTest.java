package javaPlatform;

import amf.apicontract.client.platform.AMFBaseUnitClient;
import amf.apicontract.client.platform.AMFConfiguration;
import amf.apicontract.client.platform.AsyncAPIConfiguration;
import amf.apicontract.client.platform.AvroConfiguration;
import amf.apicontract.client.platform.model.domain.api.AsyncApi;
import amf.core.client.common.validation.ValidationMode;
import amf.core.client.platform.AMFParseResult;
import amf.core.client.platform.AMFResult;
import amf.core.client.platform.model.document.BaseUnit;
import amf.core.client.platform.model.document.Document;
import amf.core.client.platform.model.domain.DomainElement;
import amf.core.client.platform.model.domain.Shape;
import amf.core.client.platform.validation.AMFValidationReport;
import amf.core.client.platform.validation.payload.AMFShapePayloadValidator;
import amf.shapes.client.platform.model.document.AvroSchemaDocument;
import org.junit.Test;

import java.util.concurrent.ExecutionException;

import static org.junit.Assert.*;

/**
 * <a href="https://a.ml/docs/related-docs/avro_schema_document">AVRO Support Guide</a>
 **/
public class AvroParsingAndValidationTest {
    final AMFConfiguration config = AvroConfiguration.Avro();
    final AMFBaseUnitClient client = config.baseUnitClient();

    @Test
    public void parseAsyncAPIWithAvroMessagePayload() throws ExecutionException, InterruptedException {
        final AMFBaseUnitClient client = AsyncAPIConfiguration.Async20().baseUnitClient();
        final AMFParseResult parseResult = client.parse("file://src/test/resources/examples/avro/asyncapi.yaml").get();
        final BaseUnit model = parseResult.baseUnit();
        assertNotNull(model);
        assertTrue(parseResult.conforms());

        final AsyncApi asyncApi = ((AsyncApi) ((Document) model).encodes());
        final Shape avroMessagePayload = asyncApi.endPoints().get(0).operations().get(0).requests().get(0).payloads().get(0).schema();
        assertNotNull(avroMessagePayload);
    }

    @Test
    public void parseAvroJsonFile() throws ExecutionException, InterruptedException {
        final AMFParseResult parseResult = client.parse("file://src/test/resources/examples/avro/record.json").get();
        final BaseUnit model = parseResult.baseUnit();
        assertNotNull(model);
        assertTrue(parseResult.conforms());
        final DomainElement webApi = ((Document) model).encodes();
        assertNotNull(webApi);
    }

    @Test
    public void parseAvroAvscFile() throws ExecutionException, InterruptedException {
        final AMFParseResult parseResult = client.parse("file://src/test/resources/examples/avro/record.avsc").get();
        final BaseUnit model = parseResult.baseUnit();
        assertNotNull(model);
        assertTrue(parseResult.conforms());
        final DomainElement webApi = ((Document) model).encodes();
        assertNotNull(webApi);
    }

    @Test
    public void transformAndValidateAvroDocument() throws ExecutionException, InterruptedException {
        final AMFParseResult parseResult = client.parse("file://src/test/resources/examples/avro/record.avsc").get();
        final BaseUnit model = parseResult.baseUnit();
        assertNotNull(model);
        assertTrue(parseResult.conforms());
        final DomainElement webApi = ((Document) model).encodes();
        assertNotNull(webApi);

        final AMFResult transformResult = client.transform(parseResult.baseUnit());
        assertTrue(transformResult.conforms());

        AMFValidationReport validationReport = client.validate(transformResult.baseUnit()).get();
        assertTrue(validationReport.conforms());
    }

    @Test
    public void payloadValidatorForAvroShape() throws ExecutionException, InterruptedException {
        final AMFParseResult parseResult = client.parse("file://src/test/resources/examples/avro/record.avsc").get();
        final AvroSchemaDocument avroSchemaDocument = (AvroSchemaDocument) parseResult.baseUnit();
        Shape avroShape = (Shape) avroSchemaDocument.encodes();
        final AMFShapePayloadValidator payloadValidator = config.elementClient().payloadValidatorFor(avroShape, "application/json", ValidationMode.StrictValidationMode());
        AMFValidationReport validationReport = payloadValidator.syncValidate("{}"); // invalid payload
        assertFalse(validationReport.conforms());
    }
}
