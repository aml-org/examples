import {
  AMLBaseUnitClient,
  AMLConfiguration,
  AMLDialectInstanceResult,
  AMLDialectResult,
  AMLVocabularyResult,
  Dialect,
  DialectDomainElement,
  DialectInstance,
  Vocabulary
} from 'amf-client-js';
import { expect } from 'chai';

describe('AML Operations', () => {
  const simpleDialectWithVocab = 'file://src/test/resources/examples/dialect-vocab.yaml';
  const simpleDialect = 'file://src/test/resources/examples/dialect.yaml';
  const simpleVocabulary = 'file://src/test/resources/examples/vocabulary.yaml';
  const simpleDialectInstance = 'file://src/test/resources/examples/dialect-instance.yaml';
  const simpleNodeTypeUri = 'file://src/test/resources/examples/dialect.yaml#/declarations/Simple';

  it('parses a dialect', async () => {
    const amlConfig: AMLConfiguration = AMLConfiguration.predefined();
    const client: AMLBaseUnitClient = amlConfig.baseUnitClient();
    const parseResult: AMLDialectResult = await client.parseDialect(simpleDialect);
    expect(parseResult.conforms).to.be.true;
    const dialect: Dialect = parseResult.dialect;
    const dialectElementId: string = dialect.documents().root().encoded().value();
    expect(dialectElementId).to.be.equal(
      'file://src/test/resources/examples/dialect.yaml#/declarations/Simple'
    );
  });

  it('parse a dialect instance', async () => {
    const amlConfig: AMLConfiguration = await AMLConfiguration.predefined().withDialect(
      simpleDialect
    );
    const client: AMLBaseUnitClient = amlConfig.baseUnitClient();
    const parseResult: AMLDialectInstanceResult = await client.parseDialectInstance(
      simpleDialectInstance
    );
    expect(parseResult.conforms).to.be.true;
    const instance: DialectInstance = parseResult.dialectInstance;
    const instanceElement: DialectDomainElement = instance.encodes;
    expect(instanceElement.getTypeIris()).to.contain(simpleNodeTypeUri);
  });

  it('parses a vocabulary', async () => {
    const amlConfig: AMLConfiguration = AMLConfiguration.predefined();
    const client: AMLBaseUnitClient = amlConfig.baseUnitClient();
    const parseResult: AMLVocabularyResult = await client.parseVocabulary(simpleVocabulary);
    expect(parseResult.conforms).to.be.true;
    const vocabulary: Vocabulary = parseResult.vocabulary;
    const vocabularyBase: string = vocabulary.base.value();
    expect(vocabularyBase).to.be.equal('http://simple.org/vocabulary#');
  });

  it('parses an instance with dialect and vocabulary', async () => {
    const amlConfig: AMLConfiguration = await AMLConfiguration.predefined().withDialect(
      simpleDialectWithVocab
    );
    const client: AMLBaseUnitClient = amlConfig.baseUnitClient();
    const parseResult: AMLDialectInstanceResult = await client.parseDialectInstance(
      simpleDialectInstance
    );
    expect(parseResult.conforms).to.be.true;
    const instance: DialectInstance = parseResult.dialectInstance;
    const propertyUris: string[] = instance.encodes.getPropertyIris();
    expect(propertyUris).to.contain('http://simple.org/vocabulary#simpleA');
  });
});
