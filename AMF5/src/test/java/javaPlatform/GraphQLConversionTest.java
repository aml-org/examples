package javaPlatform;

import amf.apicontract.client.platform.AMFBaseUnitClient;
import amf.apicontract.client.platform.AMFDocumentResult;
import amf.core.client.platform.AMFResult;
import amf.graphql.client.platform.GraphQLConfiguration;
import amf.graphqlfederation.client.platform.GraphQLFederationConfiguration;
import amf.graphqlfederation.internal.spec.transformation.GraphQLFederationIntrospectionPipeline;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

import static org.junit.Assert.*;

public class GraphQLConversionTest {

    final String origin_federation = "file://src/test/resources/examples/federation-origin.graphql";
    final Path converted_no_federation = Paths.get("src/test/resources/expected/federation-converted.jsonld");

    @Test
    public void transformGraphQLFederationToGraphQL() throws ExecutionException, InterruptedException, IOException {
        final AMFBaseUnitClient graphQLFedClient = GraphQLFederationConfiguration.GraphQLFederation().baseUnitClient();
        final AMFBaseUnitClient graphQLClient = GraphQLConfiguration.GraphQL().baseUnitClient();

        final AMFDocumentResult parseResult = graphQLFedClient.parseDocument(origin_federation).get();

        assertTrue(parseResult.conforms());
        assertNotNull(parseResult.document());

        final AMFResult transformResult = graphQLFedClient.transform(parseResult.document(), GraphQLFederationIntrospectionPipeline.name());

        assertTrue(transformResult.conforms());
        assertNotNull(transformResult.baseUnit());

        final String renderedGraphQL = graphQLClient.render(transformResult.baseUnit(), "application/ld+json");

        assertNotNull(renderedGraphQL);

        final String goldenGraphQL = Files.readAllLines(converted_no_federation).stream().collect(Collectors.joining(System.lineSeparator()));

        assertEquals(normalized(goldenGraphQL), normalized(renderedGraphQL));
    }

    private String normalized(String s) {
        return s.replaceAll("(?s)\\s+", " ").trim();
    }
}
