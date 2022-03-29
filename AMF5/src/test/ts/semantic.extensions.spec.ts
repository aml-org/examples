import { Document, OASConfiguration, PipelineId, WebApi } from 'amf-client-js';
import { expect } from 'chai';

describe('Semantic Extensions', () => {
  const EXTENSION_DIALECT = 'file://src/test/resources/examples/semantic/extensions.yaml';
  const VALID_EXTENDED_API_SPEC = 'file://src/test/resources/examples/semantic/api.oas30.yaml';
  const INVALID_EXTENDED_API_SPEC =
    'file://src/test/resources/examples/semantic/invalid-api.oas30.yaml';

  it('can read applied extensions from an Endpoint and Response Object', async () => {
    // Register my extensions into my configuration
    const config = await OASConfiguration.OAS30().withDialect(EXTENSION_DIALECT);
    const client = config.baseUnitClient();

    // Parse and transform the API
    const parsed = await client.parse(VALID_EXTENDED_API_SPEC);
    expect(parsed.conforms).to.be.true;

    // Default, Editing or Cache pipeline can be used
    const transformed = await client.transform(parsed.baseUnit, PipelineId.Editing);
    expect(transformed.conforms).to.be.true;

    const unit: Document = transformed.baseUnit as Document;
    const api = unit.encodes as WebApi;

    // Check on the "deprecated" extension
    // Extension access is done via the "graph" access. The "graph" method exposes the underlying graph.

    const endpointIsDeprecated = api.endPoints[0]
      .graph()
      .containsProperty('http://a.ml/vocabularies/apiContract#deprecated');
    expect(endpointIsDeprecated).to.be.true;

    /*
     * Extension property access is done the same as in AML
     * As the 'replaceFor' property doens't have a property term, the base of its uri is ""http://a.ml/vocabularies/data#""
     */
    const deprecatedExtension = api.endPoints[0]
      .graph()
      .getObjectByProperty('http://a.ml/vocabularies/apiContract#deprecated')[0];
    const replaceForValue = deprecatedExtension
      .graph()
      .scalarByProperty('http://a.ml/vocabularies/data#replaceFor')[0] as string;
    expect(replaceForValue).to.equal('v2/paginated');

    // Check for the page-size extension in the second endpoint
    const pageSize = api.endPoints[1].operations
      .find((x) => x.method.value() == 'get')
      .responses[0].graph()
      .scalarByProperty('http://a.ml/vocabularies/apiContract#pageSize')[0];
    expect(pageSize).to.equal(35);
  });

  it('can validate applied extensions', async () => {
    const config = await OASConfiguration.OAS30().withDialect(EXTENSION_DIALECT);
    const client = config.baseUnitClient();

    // Parse and validate the API (this API should be valid)
    const validParsed = await client.parse(VALID_EXTENDED_API_SPEC);
    expect(validParsed.conforms).to.be.true;

    const emptyReport = await client.validate(validParsed.baseUnit);
    expect(emptyReport.conforms).to.be.true;

    // Parse and validate the API (this API should NOT be valid)
    const invalid = await client.parse(INVALID_EXTENDED_API_SPEC);
    // Parsing conforms as the API doesn't have syntax errors
    expect(invalid.conforms).to.be.true;

    const report = await client.validate(invalid.baseUnit);
    expect(report.conforms).to.be.false;
    // One validation error for each invalid aspect of each extension. There is one for each.
    expect(report.results).to.have.length(2);
  });
});
