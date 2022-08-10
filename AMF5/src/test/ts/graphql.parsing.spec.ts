import {
    AMFParseResult,
    AMLBaseUnitClient,
    Document,
    GraphQLFederationConfiguration,
    GraphQLConfiguration
} from 'amf-client-js';
import {expect} from 'chai';
import {WebApi} from "amf-client-js";

describe('GraphQL Operations', () => {

    const graphql = 'file://src/test/resources/examples/simple.graphql';
    const graphqld_federation = 'file://src/test/resources/examples/simple-federation.graphql';

    it('parses a GraphQL file', async () => {
        const config = GraphQLConfiguration.GraphQL();
        const client: AMLBaseUnitClient = config.baseUnitClient();
        const parseResult: AMFParseResult = await client.parse(graphql);
        expect(parseResult.conforms).to.be.true;
        expect(parseResult.baseUnit instanceof Document);
        const document: Document = parseResult.baseUnit as Document
        // DomainElement is the base class for any element describing a domain model
        expect(document.encodes != null)
        expect(document.encodes instanceof WebApi)
    });

    it('parses a GraphQL Federation file', async () => {
        const config = GraphQLFederationConfiguration.GraphQLFederation();
        const client: AMLBaseUnitClient = config.baseUnitClient();
        const parseResult: AMFParseResult = await client.parse(graphqld_federation);
        expect(parseResult.conforms).to.be.true;
        expect(parseResult.baseUnit instanceof Document);
        const document: Document = parseResult.baseUnit as Document
        // DomainElement is the base class for any element describing a domain model
        expect(document.encodes != null)
        expect(document.encodes instanceof WebApi)
    });

});
