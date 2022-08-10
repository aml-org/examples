package javaPlatform;

import amf.apicontract.client.platform.AMFBaseUnitClient;
import amf.apicontract.client.platform.AMFConfiguration;
import amf.core.client.platform.AMFParseResult;
import amf.core.client.platform.model.document.BaseUnit;
import amf.core.client.platform.model.document.Document;
import amf.core.client.platform.model.domain.DomainElement;
import amf.graphql.client.platform.GraphQLConfiguration;
import amf.graphqlfederation.client.platform.GraphQLFederationConfiguration;
import org.junit.Test;

import java.util.concurrent.ExecutionException;

import static org.junit.Assert.*;

public class GraphQLParsingTest {

    @Test
    public void parseGraphQL() throws ExecutionException, InterruptedException {
        final AMFConfiguration configuration = GraphQLConfiguration.GraphQL();
        final AMFBaseUnitClient client = configuration.baseUnitClient();

        // A BaseUnit is the return type of any parsing
        // The actual object can be many things, depending on the content of the source file
        // https://github.com/aml-org/amf/blob/develop/documentation/model.md#baseunit
        final AMFParseResult parseResult = client.parse("file://src/test/resources/examples/simple.graphql").get();
        final BaseUnit model = parseResult.baseUnit();

        assertTrue(parseResult.conforms());
        assertNotNull(model);

        // In this case the BaseUnit is a Document
        assertSame(model.getClass(), Document.class);

        final Document document = (Document) model;

        // DomainElement is the base class for any element describing a domain model
        final DomainElement graphQLApi = document.encodes();
        assertNotNull(graphQLApi);
    }

    @Test
    public void parseGraphQLFederation() throws ExecutionException, InterruptedException {
        final AMFConfiguration configuration = GraphQLFederationConfiguration.GraphQLFederation();
        final AMFBaseUnitClient client = configuration.baseUnitClient();

        // A BaseUnit is the return type of any parsing
        // The actual object can be many things, depending on the content of the source file
        // https://github.com/aml-org/amf/blob/develop/documentation/model.md#baseunit
        final AMFParseResult parseResult = client.parse("file://src/test/resources/examples/simple-federation.graphql").get();
        final BaseUnit model = parseResult.baseUnit();

        assertTrue(parseResult.conforms());
        assertNotNull(model);

        // In this case the BaseUnit is a Document
        assertSame(model.getClass(), Document.class);

        final Document document = (Document) model;

        // DomainElement is the base class for any element describing a domain model
        final DomainElement graphQLFederationApi = document.encodes();
        assertNotNull(graphQLFederationApi);
    }
}
