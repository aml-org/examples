package javaPlatform;

import amf.apicontract.client.platform.*;
import amf.core.client.platform.AMFParseResult;
import amf.core.client.platform.model.document.BaseUnit;
import amf.core.client.platform.model.document.Document;
import amf.core.client.platform.model.domain.DomainElement;
import amf.core.internal.remote.Spec;
import org.junit.Test;

import java.util.concurrent.ExecutionException;

import static org.junit.Assert.*;

public class ParsingTest {

    @Test
    public void parseOas20() throws ExecutionException, InterruptedException {
        final AMFBaseUnitClient client = OASConfiguration.OAS20().baseUnitClient();

        // A BaseUnit is the return type of any parsing
        // The actual object can be many different things, depending on the content of the source file
        // https://github.com/aml-org/amf/blob/develop/documentation/model.md#baseunit
        final AMFParseResult parseResult = client.parse("file://src/test/resources/examples/banking-api.json").get();
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

        final AMFParseResult parseResult = client.parseContent(api).get();
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

        final AMFParseResult parseResult = client.parse("file://src/test/resources/examples/banking-api-oas30.json").get();
        final BaseUnit model = parseResult.baseUnit();

        assertNotNull(model);
        assertTrue(parseResult.conforms());
        final DomainElement webApi = ((Document) model).encodes();
        assertNotNull(webApi);
    }

    @Test
    public void parseUnknownWebAPI() throws ExecutionException, InterruptedException {
        final AMFBaseUnitClient client = WebAPIConfiguration.WebAPI().baseUnitClient();

        final AMFParseResult parseResult = client.parse("file://src/test/resources/examples/banking-api-oas30.json").get();
        final BaseUnit model = parseResult.baseUnit();
        final Spec spec = parseResult.sourceSpec();

        assertNotNull(model);
        assertTrue(parseResult.conforms());
        final DomainElement webApi = ((Document) model).encodes();
        assertNotNull(webApi);
        assertTrue(spec.isOas());
        assertEquals(spec.id(), Spec.OAS30().id());
    }

    @Test
    public void parseUnknownAPI() throws ExecutionException, InterruptedException {
        final AMFBaseUnitClient client = APIConfiguration.API().baseUnitClient();

        final AMFParseResult parseResult = client.parse("file://src/test/resources/examples/async.yaml").get();
        final BaseUnit model = parseResult.baseUnit();
        final Spec spec = parseResult.sourceSpec();

        assertNotNull(model);
        assertTrue(parseResult.conforms());
        final DomainElement asyncApi = ((Document) model).encodes();
        assertNotNull(asyncApi);
        assertTrue(spec.isAsync());
        assertEquals(spec.id(), Spec.ASYNC20().id());
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

        final AMFParseResult parseResult = client.parseContent(api).get();
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

        final AMFParseResult parseResult = client.parse("file://src/test/resources/examples/banking-api.raml").get();
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

        final AMFParseResult parseResult = client.parseContent(api).get();
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

        final AMFParseResult parseResult = client.parse("file://src/test/resources/examples/banking-api-08.raml").get();
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

        final AMFParseResult parseResult = client.parseContent(api).get();
        final BaseUnit model = parseResult.baseUnit();

        assertNotNull(model);
        assertTrue(parseResult.conforms());
        assertTrue(model.raw().isPresent());
        assertEquals(model.raw().get(), api);

        final DomainElement webApi = ((Document) model).encodes();
        assertNotNull(webApi);
    }

    @Test
    public void parseUnknownWebAPIString() throws ExecutionException, InterruptedException {
        final AMFBaseUnitClient client = WebAPIConfiguration.WebAPI().baseUnitClient();

        final String api =
                "#%RAML 0.8\n" +
                        "\n" +
                        "title: ACME Banking HTTP API\n" +
                        "version: 1.0";

        final AMFParseResult parseResult = client.parseContent(api).get();
        final BaseUnit model = parseResult.baseUnit();
        final Spec spec = parseResult.sourceSpec();

        assertNotNull(model);
        assertTrue(parseResult.conforms());
        assertTrue(model.raw().isPresent());
        assertEquals(model.raw().get(), api);
        assertTrue(spec.isRaml());
        assertEquals(spec.id(), Spec.RAML08().id());

        final DomainElement webApi = ((Document) model).encodes();
        assertNotNull(webApi);
    }

    @Test
    public void parseUnknownAPIString() throws ExecutionException, InterruptedException {
        final AMFBaseUnitClient client = APIConfiguration.API().baseUnitClient();

        final String api =
                "asyncapi: \"2.0.0\"\n" +
                        "info:\n" +
                        "  title: \"Something\"\n" +
                        "  version: \"1.0\"\n" +
                        "channels: {}";

        final AMFParseResult parseResult = client.parseContent(api).get();
        final BaseUnit model = parseResult.baseUnit();
        final Spec spec = parseResult.sourceSpec();

        assertNotNull(model);
        assertTrue(parseResult.conforms());
        assertTrue(model.raw().isPresent());
        assertEquals(model.raw().get(), api);
        assertTrue(spec.isAsync());
        assertEquals(spec.id(), Spec.ASYNC20().id());

        final DomainElement asyncApi = ((Document) model).encodes();
        assertNotNull(asyncApi);
    }

    @Test
    public void parseAsyncApi21() throws ExecutionException, InterruptedException {
        final AMFBaseUnitClient client = AsyncAPIConfiguration.Async20().baseUnitClient();

        final AMFParseResult parseResult = client.parse("file://src/test/resources/examples/asyncApi-2.1-all.yaml").get();
        final BaseUnit model = parseResult.baseUnit();
        final Spec spec = parseResult.sourceSpec();

        assertNotNull(model);
        assertTrue(parseResult.conforms());
        final DomainElement asyncApi = ((Document) model).encodes();
        assertNotNull(asyncApi);
        assertTrue(spec.isAsync());
        assertEquals(spec.id(), Spec.ASYNC21().id());
    }

    @Test
    public void parseAsyncApi22() throws ExecutionException, InterruptedException {
        final AMFBaseUnitClient client = AsyncAPIConfiguration.Async20().baseUnitClient();

        final AMFParseResult parseResult = client.parse("file://src/test/resources/examples/asyncApi-2.2-all.yaml").get();
        final BaseUnit model = parseResult.baseUnit();
        final Spec spec = parseResult.sourceSpec();

        assertNotNull(model);
        assertTrue(parseResult.conforms());
        final DomainElement asyncApi = ((Document) model).encodes();
        assertNotNull(asyncApi);
        assertTrue(spec.isAsync());
        assertEquals(spec.id(), Spec.ASYNC22().id());
    }

    @Test
    public void parseAsyncApi23() throws ExecutionException, InterruptedException {
        final AMFBaseUnitClient client = AsyncAPIConfiguration.Async20().baseUnitClient();

        final AMFParseResult parseResult = client.parse("file://src/test/resources/examples/asyncApi-2.3-all.yaml").get();
        final BaseUnit model = parseResult.baseUnit();
        final Spec spec = parseResult.sourceSpec();

        assertNotNull(model);
        assertTrue(parseResult.conforms());
        final DomainElement asyncApi = ((Document) model).encodes();
        assertNotNull(asyncApi);
        assertTrue(spec.isAsync());
        assertEquals(spec.id(), Spec.ASYNC23().id());
    }

    @Test
    public void parseAsyncApi24() throws ExecutionException, InterruptedException {
        final AMFBaseUnitClient client = AsyncAPIConfiguration.Async20().baseUnitClient();

        final AMFParseResult parseResult = client.parse("file://src/test/resources/examples/asyncApi-2.4-all.yaml").get();
        final BaseUnit model = parseResult.baseUnit();
        final Spec spec = parseResult.sourceSpec();

        assertNotNull(model);
        assertTrue(parseResult.conforms());
        final DomainElement asyncApi = ((Document) model).encodes();
        assertNotNull(asyncApi);
        assertTrue(spec.isAsync());
        assertEquals(spec.id(), Spec.ASYNC24().id());
    }

    @Test
    public void parseAsyncApi25() throws ExecutionException, InterruptedException {
        final AMFBaseUnitClient client = AsyncAPIConfiguration.Async20().baseUnitClient();

        final AMFParseResult parseResult = client.parse("file://src/test/resources/examples/asyncApi-2.5-all.yaml").get();
        final BaseUnit model = parseResult.baseUnit();
        final Spec spec = parseResult.sourceSpec();

        assertNotNull(model);
        assertTrue(parseResult.conforms());
        final DomainElement asyncApi = ((Document) model).encodes();
        assertNotNull(asyncApi);
        assertTrue(spec.isAsync());
        assertEquals(spec.id(), Spec.ASYNC25().id());
    }

    @Test
    public void parseAsyncApi26() throws ExecutionException, InterruptedException {
        final AMFBaseUnitClient client = AsyncAPIConfiguration.Async20().baseUnitClient();

        final AMFParseResult parseResult = client.parse("file://src/test/resources/examples/asyncApi-2.6-all.yaml").get();
        final BaseUnit model = parseResult.baseUnit();
        final Spec spec = parseResult.sourceSpec();

        assertNotNull(model);
        assertTrue(parseResult.conforms());
        final DomainElement asyncApi = ((Document) model).encodes();
        assertNotNull(asyncApi);
        assertTrue(spec.isAsync());
        assertEquals(spec.id(), Spec.ASYNC26().id());
    }
}
