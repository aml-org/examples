package javaPlatform;

import amf.apicontract.client.platform.AMFBaseUnitClient;
import amf.apicontract.client.platform.OASConfiguration;
import amf.apicontract.client.platform.RAMLConfiguration;
import amf.core.client.platform.AMFResult;
import amf.core.client.platform.model.document.BaseUnit;
import amf.core.client.platform.model.document.Document;
import amf.core.client.platform.model.domain.DomainElement;
import org.junit.Test;

import java.util.concurrent.ExecutionException;

import static org.junit.Assert.*;
import static org.junit.Assert.assertNotNull;

public class ParsingTest {

    @Test
    public void parseOas20() throws ExecutionException, InterruptedException {
        final AMFBaseUnitClient client = OASConfiguration.OAS20().baseUnitClient();

        // A BaseUnit is the return type of any parsing
        // The actual object can be many different things, depending on the content of the source file
        // https://github.com/aml-org/amf/blob/develop/documentation/model.md#baseunit
        final AMFResult parseResult = client.parse("file://src/test/resources/examples/banking-api.json").get();
        final BaseUnit model = parseResult.baseUnit();

        assertNotNull(model);
        assertTrue(parseResult.conforms());

        // DomainElement is the base class for any element describing a domain model
        final DomainElement webApi = ((Document) model).encodes();
        assertNotNull(webApi);
    }

    @Test
    public void parseOas20String() throws ExecutionException, InterruptedException {
        final AMFBaseUnitClient client = OASConfiguration.OAS20().baseUnitClient();

        final String api =
                "{\n" +
                        "  \"swagger\": \"2.0\",\n" +
                        "  \"info\": {\n" +
                        "    \"title\": \"ACME Banking HTTP API\",\n" +
                        "    \"version\": \"1.0\"\n" +
                        "  },\n" +
                        "  \"host\": \"acme-banking.com\",\n" +
                        "  \"paths\": {}\n" +
                        "}";

        final AMFResult parseResult = client.parseContent(api).get();
        final BaseUnit model = parseResult.baseUnit();

        assertNotNull(model);
        assertTrue(parseResult.conforms());
        assertTrue(model.raw().isPresent());
        assertEquals(model.raw().get(), api);

        final DomainElement webApi = ((Document) model).encodes();
        assertNotNull(webApi);
    }

    @Test
    public void parseOas30() throws ExecutionException, InterruptedException {
        final AMFBaseUnitClient client = OASConfiguration.OAS30().baseUnitClient();

        final AMFResult parseResult = client.parse("file://src/test/resources/examples/banking-api-oas30.json").get();
        final BaseUnit model = parseResult.baseUnit();

        assertNotNull(model);
        assertTrue(parseResult.conforms());
        final DomainElement webApi = ((Document) model).encodes();
        assertNotNull(webApi);
    }

    @Test
    public void parseOas30String() throws ExecutionException, InterruptedException {
        final AMFBaseUnitClient client = OASConfiguration.OAS30().baseUnitClient();

        final String api = "{\n" +
                "  \"openapi\": \"3.0.0\",\n" +
                "  \"info\": {\n" +
                "    \"title\": \"Basic content\",\n" +
                "    \"version\": \"0.1\"\n" +
                "  },\n" +
                "  \"paths\": {}\n" +
                "}";

        final AMFResult parseResult = client.parseContent(api).get();
        final BaseUnit model = parseResult.baseUnit();

        assertNotNull(model);
        assertTrue(parseResult.conforms());
        assertTrue(model.raw().isPresent());
        assertEquals(model.raw().get(), api);

        final DomainElement webApi = ((Document) model).encodes();
        assertNotNull(webApi);
    }

    @Test
    public void parseRaml10() throws ExecutionException, InterruptedException {
        final AMFBaseUnitClient client = RAMLConfiguration.RAML10().baseUnitClient();

        final AMFResult parseResult = client.parse("file://src/test/resources/examples/banking-api.raml").get();
        final BaseUnit model = parseResult.baseUnit();

        assertNotNull(model);
        assertTrue(parseResult.conforms());

        final DomainElement webApi = ((Document) model).encodes();
        assertNotNull(webApi);
    }

    @Test
    public void parseRaml10String() throws ExecutionException, InterruptedException {
        final AMFBaseUnitClient client = RAMLConfiguration.RAML10().baseUnitClient();

        final String api =
                "#%RAML 1.0\n" +
                        "\n" +
                        "title: ACME Banking HTTP API\n" +
                        "version: 1.0";

        final AMFResult parseResult = client.parseContent(api).get();
        final BaseUnit model = parseResult.baseUnit();

        assertNotNull(model);
        assertTrue(parseResult.conforms());
        assertTrue(model.raw().isPresent());
        assertEquals(model.raw().get(), api);

        final DomainElement webApi = ((Document) model).encodes();
        assertNotNull(webApi);
    }

    @Test
    public void parseRaml08() throws ExecutionException, InterruptedException {
        final AMFBaseUnitClient client = RAMLConfiguration.RAML08().baseUnitClient();

        final AMFResult parseResult = client.parse("file://src/test/resources/examples/banking-api-08.raml").get();
        final BaseUnit model = parseResult.baseUnit();

        assertNotNull(model);
        assertTrue(parseResult.conforms());

        final DomainElement webApi = ((Document) model).encodes();
        assertNotNull(webApi);
    }

    @Test
    public void parseRaml08String() throws ExecutionException, InterruptedException {
        final AMFBaseUnitClient client = RAMLConfiguration.RAML08().baseUnitClient();

        final String api =
                "#%RAML 0.8\n" +
                        "\n" +
                        "title: ACME Banking HTTP API\n" +
                        "version: 1.0";

        final AMFResult parseResult = client.parseContent(api).get();
        final BaseUnit model = parseResult.baseUnit();

        assertNotNull(model);
        assertTrue(parseResult.conforms());
        assertTrue(model.raw().isPresent());
        assertEquals(model.raw().get(), api);

        final DomainElement webApi = ((Document) model).encodes();
        assertNotNull(webApi);
    }
}
