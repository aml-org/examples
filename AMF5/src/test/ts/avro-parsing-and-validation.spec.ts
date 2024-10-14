import {
  AMFBaseUnitClient,
  AMFConfiguration,
  AMFDocumentResult,
  AsyncApi,
  AsyncAPIConfiguration,
  AvroConfiguration,
  Document,
  Shape,
  StrictValidationMode
} from "amf-client-js";
import { expect } from "chai";
import { describe } from "mocha";

/** <a href="https://a.ml/docs/related-docs/avro_schema_document">AVRO Support Guide</a> **/
describe('Avro Schema Document', () => {
  const config: AMFConfiguration = AvroConfiguration.Avro();
  const client: AMFBaseUnitClient = config.baseUnitClient();

  it('parse an Async API with an AVRO Schema in it', async () => {
    const asyncClient: AMFBaseUnitClient = AsyncAPIConfiguration.Async20().baseUnitClient();
    const parsingResult: AMFDocumentResult = await asyncClient.parseDocument(
      'file://src/test/resources/examples/avro/asyncapi.yaml'
    );
    expect(parsingResult.conforms).to.be.true;

    const asyncApi = (parsingResult.baseUnit as Document).encodes as AsyncApi;
    const channel = asyncApi.endPoints[0];
    const avroMessagePayload = channel.operations[0].requests[0].payloads[0].schema;
    expect(avroMessagePayload).to.not.be.null;
  });

  it('parse document from a JSON file', async () => {
    const parsingResult: AMFDocumentResult = await client.parseDocument(
      'file://src/test/resources/examples/avro/record.json'
    );
    expect(parsingResult.conforms).to.be.true;
  });

  it('parse document from an AVSC file', async () => {
    const parsingResult: AMFDocumentResult = await client.parseDocument(
      'file://src/test/resources/examples/avro/record.avsc'
    );
    expect(parsingResult.conforms).to.be.true;
  });

  it('transform and validate an AVRO Document', async () => {
    const parsingResult: AMFDocumentResult = await client.parseDocument(
      'file://src/test/resources/examples/avro/record.avsc'
    );
    expect(parsingResult.conforms).to.be.true;

    const transformationResult = client.transform(parsingResult.baseUnit);
    expect(transformationResult.conforms).to.be.true;

    const validationResult = await client.validate(transformationResult.baseUnit);
    expect(validationResult.conforms).to.be.true;
  });

  it('create a payload validator for an AVRO', async () => {
    const parsingResult: AMFDocumentResult = await client.parseDocument(
      'file://src/test/resources/examples/avro/record.avsc'
    );
    expect(parsingResult.conforms).to.be.true;

    const avroShape = (parsingResult.baseUnit as Document).encodes as Shape;
    const payloadValidator = config
      .elementClient()
      .payloadValidatorFor(avroShape, 'application/json', StrictValidationMode);

    const report = payloadValidator.syncValidate('{}'); // invalid payload
    console.log(report.toString());
    expect(report.conforms).to.be.false;
  });
});
