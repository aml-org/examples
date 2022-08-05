package javaPlatform;

import amf.aml.client.platform.AMLBaseUnitClient;
import amf.core.client.platform.AMFParseResult;
import amf.core.client.platform.model.document.BaseUnit;
import amf.core.client.platform.model.domain.Shape;
import amf.shapes.client.platform.ShapesConfiguration;
import amf.shapes.client.platform.config.JsonSchemaConfiguration;
import amf.shapes.client.platform.model.document.JsonSchemaDocument;
import org.junit.Test;

import java.util.List;
import java.util.concurrent.ExecutionException;

import static org.junit.Assert.*;

public class JSONSchemaParsingTest {

    @Test
    public void parseJSONSchema() throws ExecutionException, InterruptedException {
        ShapesConfiguration shapesConfiguration = JsonSchemaConfiguration.JsonSchema();
        final AMLBaseUnitClient client = shapesConfiguration.baseUnitClient();

        // A BaseUnit is the return type of any parsing
        // The actual object can be many different things, depending on the content of the source file
        // https://github.com/aml-org/amf/blob/develop/documentation/model.md#baseunit
        final AMFParseResult parseResult = client.parse("file://src/test/resources/examples/schema.json").get();
        final BaseUnit model = parseResult.baseUnit();

        assertTrue(parseResult.conforms());
        assertNotNull(model);

        // In this case the BaseUnit is a JsonSchemaDocument
        assertSame(model.getClass(), JsonSchemaDocument.class);

        final JsonSchemaDocument document = (JsonSchemaDocument) model;

        // A JsonSchemaDocument encodes a Shape, which is the root schema of the JSON Schema
        final Shape rootShape = (Shape) document.encodes();
        assertNotNull(rootShape);

        // A JsonSchemaDocument could declares a list of Shapes, which are the schemas inside the `definitions` or `$defs` key
        @SuppressWarnings("unchecked")
        final List<Shape> declarations = (List<Shape>) (Object) document.declares();

        assertFalse(declarations.isEmpty());
    }
}
