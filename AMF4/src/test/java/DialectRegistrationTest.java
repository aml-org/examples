import amf.client.AMF;
import amf.client.model.document.Dialect;
import amf.client.model.document.DialectInstance;
import amf.client.model.domain.DialectDomainElement;
import amf.client.parse.Aml10Parser;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.concurrent.ExecutionException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class DialectRegistrationTest {

    @BeforeClass
    public static void setup() throws ExecutionException, InterruptedException {
        // initializes all AMF plugins
        AMF.init().get();
    }

    @Test
    public void parseDialectInstance() throws ExecutionException, InterruptedException {
        final Aml10Parser parser = new Aml10Parser();

        // Parse and register dialect
        final Dialect dialect = (Dialect) parser.parseFileAsync("file://resources/aml-movies/dialect.yaml").get();
        assertNotNull(dialect);

        // Parse dialect instance
        final DialectInstance instance = (DialectInstance) parser.parseFileAsync("file://resources/aml-movies/instance.yaml").get();

        // Get elements from instance
        DialectDomainElement movie = instance.encodes();
        String director = movie.getScalarByPropertyUri("http://movies.org#director").get(0).toString();
        String title = movie.getScalarByPropertyUri("http://movies.org#title").get(0).toString();

        assertEquals(director, "George Lucas");
        assertEquals(title, "Star Wars Episode IV: A New Hope");
    }

}
