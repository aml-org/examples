import {
    AMFParseResult,
    AMLBaseUnitClient,
    GraphQLFederationConfiguration,
    GraphQLConfiguration, AMFResult, PipelineId
} from 'amf-client-js';
import {expect} from 'chai';
import * as fileSystem from 'fs';

describe('GraphQL Conversion', () => {

    const ORIGIN_FEDERATION = "file://src/test/resources/examples/federation-origin.graphql"
    const CONVERTED_NO_FEDERATION = "src/test/resources/expected/federation-converted.jsonld"

    it('Convert GraphQL Federation in GraphQL', async () => {
        const graphQLFedClient: AMLBaseUnitClient = GraphQLFederationConfiguration.GraphQLFederation().baseUnitClient();
        const graphQLClient: AMLBaseUnitClient = GraphQLConfiguration.GraphQL().baseUnitClient();
        const parseResult: AMFParseResult = await graphQLFedClient.parse(ORIGIN_FEDERATION);
        expect(parseResult.conforms).to.be.true;
        // @ts-ignore TODO: remove ignored
        const transformResult: AMFResult = graphQLFedClient.transform(parseResult.baseUnit, PipelineId.Introspection);
        expect(transformResult.conforms).to.be.true;
        const renderedGraphQL: string = graphQLClient.render(transformResult.baseUnit, "application/ld+json");
        const goldenGraphQL: string = fileSystem.readFileSync(CONVERTED_NO_FEDERATION, {
            encoding: 'utf8'
        });
        const normalizedRendered = ignoreSpaces(renderedGraphQL)
        const normalizedGolden = ignoreSpaces(goldenGraphQL)
        expect(normalizedRendered).to.be.equal(normalizedGolden, `Rendered: ${renderedGraphQL} \nGolden: ${goldenGraphQL}`);
    });

    function ignoreSpaces(s: string): string {
        return s.replace(/ /g, '').trim()
    }

});
