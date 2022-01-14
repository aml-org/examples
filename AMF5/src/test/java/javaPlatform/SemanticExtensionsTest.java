package javaPlatform;

import amf.aml.client.platform.model.domain.DialectDomainElement;
import amf.apicontract.client.platform.AMFBaseUnitClient;
import amf.apicontract.client.platform.AMFConfiguration;
import amf.apicontract.client.platform.OASConfiguration;
import amf.apicontract.client.platform.model.domain.api.Api;
import amf.core.client.common.transform.PipelineId;
import amf.core.client.platform.AMFParseResult;
import amf.core.client.platform.AMFResult;
import amf.core.client.platform.model.document.Document;
import amf.core.client.platform.validation.AMFValidationReport;
import org.junit.Test;

import java.util.Objects;
import java.util.concurrent.ExecutionException;

import static org.junit.Assert.*;

public class SemanticExtensionsTest {

    private String EXTENSION_DIALECT = "file://src/test/resources/examples/semantic/extensions.yaml";
    private String VALID_EXTENDED_API_SPEC = "file://src/test/resources/examples/semantic/api.oas30.yaml";
    private String INVALID_EXTENDED_API_SPEC = "file://src/test/resources/examples/semantic/invalid-api.oas30.yaml";

    @Test
    public void can_read_applied_extensions_from_endpoint_and_response() throws ExecutionException, InterruptedException {

        // Register my extensions into my configuration
        final AMFConfiguration config = OASConfiguration.OAS30().withDialect(EXTENSION_DIALECT).get();
        final AMFBaseUnitClient client = config.baseUnitClient();

        // Parse and transform the API
        AMFParseResult parsed = client.parse(VALID_EXTENDED_API_SPEC).get();
        assertTrue(parsed.conforms());

        // Default, Editing or Cache pipeline can be used
        AMFResult transformed = client.transform(parsed.baseUnit(), PipelineId.Editing());
        assertTrue(transformed.conforms());

        Document unit = (Document) transformed.baseUnit();
        Api<?> api = (Api<?>) unit.encodes();

        // Check on the "deprecated" extension
        // Extension access is done via the "graph" access. The "graph" method exposes the underlying graph.

        Boolean endpointIsDeprecated = api.endPoints().get(0).graph().containsProperty("http://a.ml/vocabularies/apiContract#deprecated");
        assertTrue(endpointIsDeprecated);

        /*
         * Extension property access is done the same as in AML
         * As the 'replaceFor' property doens't have a property term, the base of its uri is ""http://a.ml/vocabularies/data#""
         */
        DialectDomainElement deprecatedExtension = (DialectDomainElement) api.endPoints().get(0).graph().getObjectByProperty("http://a.ml/vocabularies/apiContract#deprecated").get(0);
        String replaceForValue = (String) deprecatedExtension.graph().scalarByProperty("http://a.ml/vocabularies/data#replaceFor").get(0);
        assertEquals(replaceForValue, "v2/paginated");

        // Check for the page-size extension in the second endpoint
        int pageSize = (int) api.endPoints().get(1)
                .operations().stream()
                .filter(x -> Objects.equals(x.method().value(), "get"))
                .findFirst().get()
                .responses().get(0)
                .graph().scalarByProperty("http://a.ml/vocabularies/apiContract#pageSize").get(0);
        assertEquals(pageSize, 35);
    }

    @Test
    public void can_validate_applied_extensions() throws ExecutionException, InterruptedException {
        final AMFConfiguration config = OASConfiguration.OAS30().withDialect(EXTENSION_DIALECT).get();
        final AMFBaseUnitClient client = config.baseUnitClient();

        // Parse and validate the API (this API should be valid)
        AMFParseResult validParsed = client.parse(VALID_EXTENDED_API_SPEC).get();
        assertTrue(validParsed.conforms());

        AMFValidationReport emptyReport = client.validate(validParsed.baseUnit()).get();
        assertTrue(emptyReport.conforms());

        // Parse and validate the API (this API should NOT be valid)
        AMFParseResult invalid = client.parse(INVALID_EXTENDED_API_SPEC).get();
        // Parsing conforms as the API doesn't have syntax errors
        assertTrue(invalid.conforms());

        AMFValidationReport report = client.validate(invalid.baseUnit()).get();
        assertFalse(report.conforms());
        // One validation error for each invalid aspect of each extension. There is one for each.
        assertEquals(report.results().size(), 2);
    }
}
