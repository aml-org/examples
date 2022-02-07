import {
    AMLConfiguration, Dialect, DialectInstance, Vocabulary, SemanticJsonSchemaConfiguration
} from 'amf-client-js';
import {expect} from 'chai';
import * as fs from 'fs';
import * as path from 'path';

describe('Convert JSON Schema in AML Dialect', () => {
    const INPUT_JSON_SCHEMA_SIMPLE = path.join(__dirname, '../resources/examples/json-schema-dialect-simple.json');
    const OUTPUT_AML_DIALECT_SIMPLE = path.join(__dirname, '../resources/examples/json-schema-dialect-simple.yaml');
    const JSON_SCHEMA_INSTANCE_SIMPLE = path.join(__dirname, '../resources/examples/json-instance-simple.json');

    const INPUT_JSON_SCHEMA_COMPLEX = path.join(__dirname, '../resources/examples/json-schema-dialect-complex.json');
    const OUTPUT_AML_DIALECT_COMPLEX = path.join(__dirname, '../resources/examples/json-schema-dialect-complex.yaml');
    const OUTPUT_AML_VOCABULARY_COMPLEX = path.join(__dirname, '../resources/examples/json-schema-vocab-complex.yaml');
    const JSON_SCHEMA_INSTANCE_COMPLEX = path.join(__dirname, '../resources/examples/json-instance-complex.json');

    it('convert JSON Schema simple to Dialect', async () => {
        const client = SemanticJsonSchemaConfiguration.predefined().baseUnitClient();
        const parseResult = await client.parseSemanticSchema("file://" + INPUT_JSON_SCHEMA_SIMPLE);

        expect(parseResult.conforms).to.be.true;
        expect(parseResult.baseUnit).to.be.instanceof(Dialect);
        expect(parseResult.vocabulary).to.be.undefined;

        const generatedDialect = parseResult.baseUnit;
        const readDialect = fs.readFileSync(OUTPUT_AML_DIALECT_SIMPLE, {encoding: 'utf8'}).trim();
        const renderDialect = client.render(generatedDialect).trim();

        expect(readDialect).to.be.equal(renderDialect);
    });

    it('validate JSON instance with generated simple Dialect', async () => {
        const semanticJsonSchemaClient = SemanticJsonSchemaConfiguration.predefined().baseUnitClient();
        const dialectResult = await semanticJsonSchemaClient.parseSemanticSchema("file://" + INPUT_JSON_SCHEMA_SIMPLE);

        expect(dialectResult.conforms).to.be.true;
        expect(dialectResult.baseUnit).to.be.instanceof(Dialect);
        expect(dialectResult.vocabulary).to.be.undefined;

        const amlClient = AMLConfiguration.predefined().withDialect(dialectResult.baseUnit).baseUnitClient();
        const instanceResult = await amlClient.parseDialectInstance("file://" + JSON_SCHEMA_INSTANCE_SIMPLE);

        expect(instanceResult.conforms).to.be.true;
        expect(instanceResult.baseUnit).to.be.instanceof(DialectInstance);
    });

    it('convert JSON Schema complex to Dialect & Vocabulary', async () => {
        const client = SemanticJsonSchemaConfiguration.predefined().baseUnitClient();
        const parseResult = await client.parseSemanticSchema("file://" + INPUT_JSON_SCHEMA_COMPLEX);

        expect(parseResult.conforms).to.be.true;
        expect(parseResult.baseUnit).to.be.instanceof(Dialect);
        expect(parseResult.vocabulary).to.be.instanceof(Vocabulary);

        const generatedDialect = parseResult.baseUnit;
        const readDialect = fs.readFileSync(OUTPUT_AML_DIALECT_COMPLEX, {encoding: 'utf8'}).trim();
        const renderDialect = client.render(generatedDialect).trim();
        expect(readDialect).to.be.equal(renderDialect);

        const generatedVocab = parseResult.vocabulary;
        const readVocab = fs.readFileSync(OUTPUT_AML_VOCABULARY_COMPLEX, {encoding: 'utf8'}).trim();
        const renderVocab = client.render(generatedVocab).trim();
        expect(readVocab).to.be.equal(renderVocab);

    });

    it('validate JSON instance with generated complex Dialect', async () => {
        const semanticJsonSchemaClient = SemanticJsonSchemaConfiguration.predefined().baseUnitClient();
        const dialectResult = await semanticJsonSchemaClient.parseSemanticSchema("file://" + INPUT_JSON_SCHEMA_COMPLEX);

        expect(dialectResult.conforms).to.be.true;
        expect(dialectResult.baseUnit).to.be.instanceof(Dialect);
        expect(dialectResult.vocabulary).to.be.instanceof(Vocabulary);

        const amlClient = AMLConfiguration.predefined().withDialect(dialectResult.baseUnit).baseUnitClient();
        const instanceResult = await amlClient.parseDialectInstance("file://" + JSON_SCHEMA_INSTANCE_COMPLEX);

        console.log(instanceResult.results)
        expect(instanceResult.conforms).to.be.true;
        expect(instanceResult.baseUnit).to.be.instanceof(DialectInstance);
    });

});
