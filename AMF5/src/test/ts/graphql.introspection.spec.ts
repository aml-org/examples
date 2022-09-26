import {
    AMFParseResult,
    AMLBaseUnitClient,
    GraphQLFederationConfiguration,
    GraphQLConfiguration, AMFResult, PipelineId
} from 'amf-client-js';
import {expect} from 'chai';
import * as fileSystem from 'fs';

describe('GraphQL introspection', () => {

    const ORIGINAL = "file://src/test/resources/examples/federation-origin.graphql"
    const INTROSPECTED = "src/test/resources/expected/federation-introspected.graphql"

    it('Introspect Federation API', async () => {
        const fedClient: AMLBaseUnitClient = GraphQLFederationConfiguration.GraphQLFederation().baseUnitClient();
        const graphQLClient: AMLBaseUnitClient = GraphQLConfiguration.GraphQL().baseUnitClient();

        const parseResult: AMFParseResult = await fedClient.parse(ORIGINAL);

        expect(parseResult.conforms).to.be.true;

        const transformResult: AMFResult = fedClient.transform(parseResult.baseUnit, PipelineId.Introspection);

        expect(transformResult.conforms).to.be.true;

        const renderedGraphQL: string = graphQLClient.render(transformResult.baseUnit, "application/graphql");
        const goldenGraphQL: string = fileSystem.readFileSync(INTROSPECTED, {
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
