import amf.client.exported.*;
import amf.client.model.document.BaseUnit;
import amf.client.model.document.Document;
import amf.client.model.domain.DomainElement;
import amf.core.remote.Amf;
import amf.plugins.document.WebApi;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.concurrent.ExecutionException;

import static org.junit.Assert.*;
import static org.junit.Assert.assertNotNull;

public class ParsingTest {

    @Test
    public void parseOas20() throws ExecutionException, InterruptedException {
        final AMFClient client = OASConfiguration.OAS20().createClient();

        // A BaseUnit is the return type of any parsing
        // The actual object can be many different things, depending on the content of the source file
        // https://github.com/aml-org/amf/blob/develop/documentation/model.md#baseunit
        final BaseUnit model = client.parse("file://resources/examples/banking-api.json").get().baseUnit();
        assertNotNull(model);

        // DomainElement is the base class for any element describing a domain model
        final DomainElement webApi = ((Document) model).encodes();
        assertNotNull(webApi);
    }

    @Test
    public void parseOas20String() throws ExecutionException, InterruptedException {
        final AMFClient client = OASConfiguration.OAS20().createClient();

        final String api =
                "{\n" +
                        "  \"swagger\": \"2.0\",\n" +
                        "  \"info\": {\n" +
                        "    \"title\": \"ACME Banking HTTP API\",\n" +
                        "    \"version\": \"1.0\"\n" +
                        "  },\n" +
                        "  \"host\": \"acme-banking.com\"" +
                        "}";

        final BaseUnit model = client.parseContent(api).get().baseUnit();
        assertNotNull(model);
        assertTrue(model.raw().isPresent());
        assertEquals(model.raw().get(), api);

        final DomainElement webApi = ((Document) model).encodes();
        assertNotNull(webApi);
    }

    @Test
    public void parseOas30() throws ExecutionException, InterruptedException {
        final AMFClient client = OASConfiguration.OAS30().createClient();

        final BaseUnit model = client.parse("file://resources/examples/banking-api-oas30.json").get().baseUnit();
        assertNotNull(model);

        final DomainElement webApi = ((Document) model).encodes();
        assertNotNull(webApi);
    }

    @Test
    public void parseRaml10() throws ExecutionException, InterruptedException {
        final AMFClient client = RAMLConfiguration.RAML10().createClient();

        final BaseUnit model = client.parse("file://resources/examples/banking-api.raml").get().baseUnit();
        assertNotNull(model);

        final DomainElement webApi = ((Document) model).encodes();
        assertNotNull(webApi);
    }

    @Test
    public void parseRaml10String() throws ExecutionException, InterruptedException {
        final AMFClient client = RAMLConfiguration.RAML10().createClient();

        final String api =
                "#%RAML 1.0\n" +
                        "\n" +
                        "title: ACME Banking HTTP API\n" +
                        "version: 1.0";

        final BaseUnit model = client.parseContent(api).get().baseUnit();
        assertNotNull(model);
        assertTrue(model.raw().isPresent());
        assertEquals(model.raw().get(), api);

        final DomainElement webApi = ((Document) model).encodes();
        assertNotNull(webApi);
    }

    @Test
    public void parseRaml08() throws ExecutionException, InterruptedException {
        final AMFClient client = RAMLConfiguration.RAML08().createClient();

        final BaseUnit model = client.parse("file://resources/examples/banking-api-08.raml").get().baseUnit();
        assertNotNull(model);

        final DomainElement webApi = ((Document) model).encodes();
        assertNotNull(webApi);
    }

    @Test
    public void parseRaml08String() throws ExecutionException, InterruptedException {
        final AMFClient client = RAMLConfiguration.RAML08().createClient();

        final String api =
                "#%RAML 0.8\n" +
                        "\n" +
                        "title: ACME Banking HTTP API\n" +
                        "version: 1.0";

        final BaseUnit model = client.parseContent(api).get().baseUnit();
        assertNotNull(model);
        assertTrue(model.raw().isPresent());
        assertEquals(model.raw().get(), api);

        final DomainElement webApi = ((Document) model).encodes();
        assertNotNull(webApi);
    }

    @Test
    public void parseAMFGraph() throws ExecutionException, InterruptedException {
        final AMFClient client = WebAPIConfiguration.WebAPI().createClient(); // change to GraphConfig

        final BaseUnit model = client.parse("file://resources/examples/banking-api.jsonld").get().baseUnit();
        assertNotNull(model);

        final DomainElement webApi = ((Document) model).encodes();
        assertNotNull(webApi);
    }
}
