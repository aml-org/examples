package javaPlatform;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;

import amf.aml.client.platform.AMLBaseUnitClient;
import amf.aml.client.platform.AMLConfiguration;
import amf.aml.client.platform.AMLDialectInstanceResult;
import amf.aml.client.platform.AMLDialectResult;
import amf.aml.client.platform.AMLVocabularyResult;
import amf.aml.client.platform.model.document.Dialect;
import amf.aml.client.platform.model.domain.DialectDomainElement;
import amf.core.client.platform.AMFParseResult;
import amf.core.client.platform.resource.FileResourceLoader;
import java.util.List;
import java.util.concurrent.ExecutionException;
import org.junit.Test;

public class AMLTest {

    private final static String simpleDialectWithVocabulary = "file://src/test/resources/examples/dialect-vocab.yaml";
    private final static String simpleVocabulary = "file://src/test/resources/examples/vocabulary.yaml";
    private final static String simpleDialectYaml = "file://src/test/resources/examples/dialect.yaml";
    private final static String simpleDialectJson = "file://src/test/resources/examples/dialect.json";
    private final static String simpleDialectInstance = "file://src/test/resources/examples/dialect-instance.yaml";
    private final static String simpleNodeTypeUri = "file://src/test/resources/examples/dialect.yaml#/declarations/Simple";

    @Test
    public void parseDialect() throws ExecutionException, InterruptedException {
        final AMLBaseUnitClient client = AMLConfiguration.predefined().baseUnitClient();
        final AMLDialectResult parseResult = client.parseDialect(simpleDialectYaml).get();
        assertTrue(parseResult.conforms());
        final Dialect dialect = (Dialect) parseResult.baseUnit();
        final String dialectElementId = dialect.documents().root().encoded().value();
        assertEquals("file://src/test/resources/examples/dialect.yaml#/declarations/Simple", dialectElementId);
    }

    @Test
    public void testParsingDialectStringAsJsonAndAsYaml() throws ExecutionException, InterruptedException {
        final AMLBaseUnitClient client = AMLConfiguration.predefined().baseUnitClient();

        //This one passes successfully
        final String yaml = new FileResourceLoader().fetch(simpleDialectYaml).get().toString();
        final AMFParseResult amfParseResultDialectStringYaml = client.parseContent(yaml).get();
        assertTrue(amfParseResultDialectStringYaml.conforms());

        //This one fails. This is the case that we implemented in CFM.
        final String json = new FileResourceLoader().fetch(simpleDialectJson).get().toString();
        final AMFParseResult amfParseResultDialectStringJson = client.parseContent(json).get();
        assertTrue(amfParseResultDialectStringJson.conforms());
    }

    @Test
    public void parseDialectInstance() throws ExecutionException, InterruptedException {
        final AMLBaseUnitClient client = AMLConfiguration.predefined().withDialect(simpleDialectYaml).get().baseUnitClient();
        final AMLDialectInstanceResult parseResult = client.parseDialectInstance(simpleDialectInstance).get();
        assertTrue(parseResult.conforms());
        final DialectDomainElement instanceElement = parseResult.dialectInstance().encodes();
        assertTrue(instanceElement.getTypeIris().contains(simpleNodeTypeUri));
    }

    @Test
    public void parseVocabulary() throws ExecutionException, InterruptedException {
        final AMLBaseUnitClient client = AMLConfiguration.predefined().baseUnitClient();
        final AMLVocabularyResult parseResult = client.parseVocabulary(simpleVocabulary).get();
        assertTrue(parseResult.conforms());
        final String vocabularyBase = parseResult.vocabulary().base().value();
        assertEquals(vocabularyBase, "http://simple.org/vocabulary#");
    }

    @Test
    public void parseDialectInstanceWithDialectAndVocabulary() throws ExecutionException, InterruptedException {
        final AMLBaseUnitClient client = AMLConfiguration.predefined().withDialect(simpleDialectWithVocabulary).get().baseUnitClient();
        final AMLDialectInstanceResult parseResult = client.parseDialectInstance(simpleDialectInstance).get();
        assertTrue(parseResult.conforms());
        final List<String> propertyUris = parseResult.dialectInstance().encodes().getPropertyIris();
        assertTrue(propertyUris.contains("http://simple.org/vocabulary#simpleA"));
    }
}
