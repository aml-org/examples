import {
    AMFParseResult,
    AMLBaseUnitClient,
    JsonSchemaConfiguration,
    JsonSchemaDocument,
    ShapesConfiguration,
    AnyShape
} from 'amf-client-js';
import {expect} from 'chai';

describe('JSON Schema Operations', () => {

    const simpleSchema = 'file://src/test/resources/examples/dialect.yaml';

    it('parses a JSON Schema', async () => {
        const config: ShapesConfiguration = JsonSchemaConfiguration.JsonSchema();
        const client: AMLBaseUnitClient = config.baseUnitClient();
        const parseResult: AMFParseResult = await client.parse(simpleSchema);
        expect(parseResult.conforms).to.be.true;
        expect(parseResult.baseUnit instanceof JsonSchemaDocument);
        const document: JsonSchemaDocument = parseResult.baseUnit as JsonSchemaDocument
        expect(document.encodes != null)
        expect(document.encodes instanceof AnyShape)
        expect(document.declares.length == 1)
        expect(document.declares[0] instanceof AnyShape)
    });

});
