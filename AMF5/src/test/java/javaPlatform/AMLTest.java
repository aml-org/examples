package javaPlatform;

import amf.aml.client.platform.*;
import amf.aml.client.platform.model.document.Dialect;
import amf.aml.client.platform.model.domain.DialectDomainElement;
import org.junit.Test;

import java.util.List;
import java.util.concurrent.ExecutionException;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;

public class AMLTest {

    private final static String simpleDialectWithVocabulary = "file://resources/examples/dialect-vocab.yaml";
    private final static String simpleVocabulary = "file://resources/examples/vocabulary.yaml";
    private final static String simpleDialect = "file://resources/examples/dialect.yaml";
    private final static String simpleDialectInstance = "file://resources/examples/dialect-instance.yaml";
    private final static String simpleNodeTypeUri = "file://resources/examples/dialect.yaml#/declarations/Simple";

    @Test
    public void parseDialect() throws ExecutionException, InterruptedException {
        final AMLClient client = AMLConfiguration.predefined().createClient();
        final AMLDialectResult parseResult = client.parseDialect(simpleDialect).get();
        assertTrue(parseResult.conforms());
        final Dialect dialect = (Dialect) parseResult.baseUnit();
        final String dialectElementId = dialect.documents().root().encoded().value();
        assertEquals("file://resources/examples/dialect.yaml#/declarations/Simple", dialectElementId);
    }

    @Test
    public void parseDialectInstance() throws ExecutionException, InterruptedException {
        final AMLClient client = AMLConfiguration.predefined().withDialect(simpleDialect).get().createClient();
        final AMLDialectInstanceResult parseResult = client.parseDialectInstance(simpleDialectInstance).get();
        assertTrue(parseResult.conforms());
        final DialectDomainElement instanceElement = parseResult.dialectInstance().encodes();
        assertTrue(instanceElement.getTypeUris().contains(simpleNodeTypeUri));
    }

    @Test
    public void parseVocabulary() throws ExecutionException, InterruptedException {
        final AMLClient client = AMLConfiguration.predefined().createClient();
        final AMLVocabularyResult parseResult = client.parseVocabulary(simpleVocabulary).get();
        assertTrue(parseResult.conforms());
        final String vocabularyBase = parseResult.vocabulary().base().value();
        assertEquals(vocabularyBase, "http://simple.org/vocabulary#");
    }

    @Test
    public void parseDialectInstanceWithDialectAndVocabulary() throws ExecutionException, InterruptedException {
        final AMLClient client = AMLConfiguration.predefined().withDialect(simpleDialectWithVocabulary).get().createClient();
        final AMLDialectInstanceResult parseResult = client.parseDialectInstance(simpleDialectInstance).get();
        assertTrue(parseResult.conforms());
        final List<String> propertyUris = parseResult.dialectInstance().encodes().getPropertyUris();
        assertTrue(propertyUris.contains("http://simple.org/vocabulary#simpleA"));
    }
}
