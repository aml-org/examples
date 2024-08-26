package javaPlatform;

import amf.apicontract.client.platform.AMFBaseUnitClient;
import amf.apicontract.client.platform.AMFConfiguration;
import amf.apicontract.client.platform.AvroConfiguration;
import amf.core.client.platform.AMFParseResult;
import amf.core.client.platform.model.document.BaseUnit;
import amf.core.client.platform.model.domain.Shape;
import amf.shapes.client.platform.model.document.AvroSchemaDocument;
import amf.shapes.client.platform.model.domain.AnyShape;
import org.junit.Test;

import java.util.concurrent.ExecutionException;

import static org.junit.Assert.*;

public class AVROSchemaParsingTest {

    @Test
    public void parseAVROSchema() throws ExecutionException, InterruptedException {
        AMFConfiguration avroConfiguration = AvroConfiguration.Avro();
        final AMFBaseUnitClient client = avroConfiguration.baseUnitClient();

        // A BaseUnit is the return type of any parsing
        // The actual object can be many different things, depending on the content of the source file
        // https://github.com/aml-org/amf/blob/develop/documentation/model.md#baseunit
        final AMFParseResult parseResult = client.parse("file://src/test/resources/examples/schema.avsc").get();
        final BaseUnit model = parseResult.baseUnit();

        assertTrue(parseResult.conforms());
        assertNotNull(model);

        // In this case the BaseUnit is a AvroSchemaDocument
        assertSame(model.getClass(), AvroSchemaDocument.class);

        final AvroSchemaDocument document = (AvroSchemaDocument) model;

        // A AvroSchemaDocument encodes a Shape, which is the root schema of the JSON Schema
        final Shape rootShape = (Shape) document.encodes();
        assertNotNull(rootShape);

        // A AvroSchemaDocument shape should an AnyShape
        assertTrue(rootShape instanceof AnyShape);
        final AnyShape anyShape = (AnyShape) rootShape;
        // It also should be of AVRO kind and have an AVRO type
        assertTrue(anyShape.isAvroSchema());
        assertTrue(anyShape.avroSchemaType().isPresent());
        assertEquals(anyShape.avroSchemaType().get(), "record");
    }
}
