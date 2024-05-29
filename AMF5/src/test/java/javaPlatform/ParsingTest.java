package javaPlatform;

import amf.apicontract.client.platform.*;
import amf.apicontract.client.platform.model.domain.*;
import amf.apicontract.client.platform.model.domain.api.AsyncApi;
import amf.apicontract.client.platform.model.domain.bindings.ChannelBinding;
import amf.apicontract.client.platform.model.domain.bindings.MessageBinding;
import amf.apicontract.client.platform.model.domain.bindings.OperationBinding;
import amf.apicontract.client.platform.model.domain.bindings.ServerBinding;
import amf.core.client.platform.AMFParseResult;
import amf.core.client.platform.model.document.BaseUnit;
import amf.core.client.platform.model.document.Document;
import amf.core.client.platform.model.domain.DomainElement;
import amf.core.internal.remote.Spec;
import org.junit.Test;
import utils.BaseUnitUtilsJava;

import java.util.List;
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
        final BaseUnit baseUnit = parseResult.baseUnit();
        final Spec spec = parseResult.sourceSpec();
        final AsyncApi asyncApi = (AsyncApi) BaseUnitUtilsJava.getApi(baseUnit, true);
        // This version supports Mercure and IBMMQ bindings

        // Server Binding
        final Server server = BaseUnitUtilsJava.getFirstServer(baseUnit, true);
        final ServerBinding serverBinding = server.bindings().bindings().get(0);

        // Channel Binding
        final EndPoint someOtherChannel = BaseUnitUtilsJava.getLastEndPoint(baseUnit, true);
        final ChannelBinding channelBinding = someOtherChannel.bindings().bindings().get(0);

        // Message Binding
        final Request messageRequest = BaseUnitUtilsJava.getFirstRequest(baseUnit, true);
        final MessageBinding messageBinding = messageRequest.bindings().bindings().get(0);


        assertNotNull(baseUnit);
        assertTrue(parseResult.conforms());
        assertNotNull(asyncApi);
        assertTrue(spec.isAsync());
        assertEquals(spec.id(), Spec.ASYNC21().id());
        assertTrue(serverBinding.id().contains("/ibmmq-server"));
        assertTrue(channelBinding.id().contains("/ibmmq-channel"));
        assertTrue(messageBinding.id().contains("/ibmmq-message"));
    }

    @Test
    public void parseAsyncApi22() throws ExecutionException, InterruptedException {
        final AMFBaseUnitClient client = AsyncAPIConfiguration.Async20().baseUnitClient();

        final AMFParseResult parseResult = client.parse("file://src/test/resources/examples/asyncApi-2.2-all.yaml").get();
        final BaseUnit baseUnit = parseResult.baseUnit();
        final Spec spec = parseResult.sourceSpec();
        final AsyncApi asyncApi = (AsyncApi) BaseUnitUtilsJava.getApi(baseUnit, true);
        // This version adds AnypointMQ bindings


        // Channel Binding
        final List<EndPoint> endpoints = asyncApi.endPoints();
        final EndPoint anotherChannel = BaseUnitUtilsJava.getLastEndPoint(baseUnit, true);
        final ChannelBinding channelBinding = anotherChannel.bindings().bindings().get(0);

        // Message Binding
        final Request messageRequest = BaseUnitUtilsJava.getFirstRequest(baseUnit, true);
        final MessageBinding messageBinding = messageRequest.bindings().bindings().get(0);

        assertNotNull(baseUnit);
        assertTrue(parseResult.conforms());
        assertNotNull(asyncApi);
        assertTrue(spec.isAsync());
        assertEquals(spec.id(), Spec.ASYNC22().id());
        assertTrue(channelBinding.id().contains("/anypointmq-channel"));
        assertTrue(messageBinding.id().contains("/anypointmq-message"));

    }

    @Test
    public void parseAsyncApi23() throws ExecutionException, InterruptedException {
        final AMFBaseUnitClient client = AsyncAPIConfiguration.Async20().baseUnitClient();

        final AMFParseResult parseResult = client.parse("file://src/test/resources/examples/asyncApi-2.3-all.yaml").get();
        final BaseUnit baseUnit = parseResult.baseUnit();
        final Spec spec = parseResult.sourceSpec();
        final AsyncApi asyncApi = (AsyncApi) BaseUnitUtilsJava.getApi(baseUnit, true);
        // This version adds Solace bindings

        // Operation Binding
        final Operation publishOperation = BaseUnitUtilsJava.getFirstOperationFromLastEndpoint(baseUnit, true);
        final OperationBinding operationBinding = publishOperation.bindings().bindings().get(0);


        assertNotNull(baseUnit);
        assertTrue(parseResult.conforms());
        assertNotNull(asyncApi);
        assertTrue(spec.isAsync());
        assertEquals(spec.id(), Spec.ASYNC23().id());
        // Asume 0.3.0 version
        assertTrue(operationBinding.id().contains("/solace-operation-030"));
    }

    @Test
    public void parseAsyncApi24() throws ExecutionException, InterruptedException {
        final AMFBaseUnitClient client = AsyncAPIConfiguration.Async20().baseUnitClient();

        final AMFParseResult parseResult = client.parse("file://src/test/resources/examples/asyncApi-2.4-all.yaml").get();
        final BaseUnit baseUnit = parseResult.baseUnit();
        final Spec spec = parseResult.sourceSpec();
        // No new bindings in this version

        assertNotNull(baseUnit);
        assertTrue(parseResult.conforms());
        final DomainElement asyncApi = ((Document) baseUnit).encodes();
        assertNotNull(asyncApi);
        assertTrue(spec.isAsync());
        assertEquals(spec.id(), Spec.ASYNC24().id());
    }

    @Test
    public void parseAsyncApi25() throws ExecutionException, InterruptedException {
        final AMFBaseUnitClient client = AsyncAPIConfiguration.Async20().baseUnitClient();

        final AMFParseResult parseResult = client.parse("file://src/test/resources/examples/asyncApi-2.5-all.yaml").get();
        final BaseUnit baseUnit = parseResult.baseUnit();
        final Spec spec = parseResult.sourceSpec();
        final AsyncApi asyncApi = (AsyncApi) BaseUnitUtilsJava.getApi(baseUnit, true);
        // This version adds GooglePubSub binding.

        // Channel Binding
        final EndPoint topicProtoSchema = BaseUnitUtilsJava.getLastEndPoint(baseUnit, true);
        final ChannelBinding channelBinding = topicProtoSchema.bindings().bindings().get(0);

        // Message Binding
        final Message messageComponent = BaseUnitUtilsJava.getMessageComponent(baseUnit);
        final MessageBinding messageBinding = messageComponent.bindings().bindings().get(0);

        assertNotNull(baseUnit);
        assertTrue(parseResult.conforms());
        assertNotNull(asyncApi);
        assertTrue(spec.isAsync());
        assertEquals(spec.id(), Spec.ASYNC25().id());
        // By default, the version will be 0.1.0
        assertTrue(channelBinding.id().contains("/googlepubsub-channel-010"));
        assertTrue(messageBinding.id().contains("/googlepubsub-message-010"));
    }

    @Test
    public void parseAsyncApi26() throws ExecutionException, InterruptedException {
        final AMFBaseUnitClient client = AsyncAPIConfiguration.Async20().baseUnitClient();

        final AMFParseResult parseResult = client.parse("file://src/test/resources/examples/asyncApi-2.6-all.yaml").get();
        final BaseUnit baseUnit = parseResult.baseUnit();
        final Spec spec = parseResult.sourceSpec();
        final AsyncApi asyncApi = (AsyncApi) BaseUnitUtilsJava.getApi(baseUnit, true);
        // This version adds Pulsar bindings.

        // Server Binding
        final Server server = BaseUnitUtilsJava.getLastServer(baseUnit, true);
        final ServerBinding serverBinding = server.bindings().bindings().get(0);

        // Channel Binding
        final EndPoint sixthChannel = BaseUnitUtilsJava.getLastEndPoint(baseUnit, true);
        final ChannelBinding channelBinding = sixthChannel.bindings().bindings().get(0);

        assertNotNull(baseUnit);
        assertTrue(parseResult.conforms());
        assertNotNull(asyncApi);
        assertTrue(spec.isAsync());
        assertEquals(spec.id(), Spec.ASYNC26().id());
        assertTrue(serverBinding.id().contains("/pulsar-server"));
        // Uncomment this line when bug is fixed
//        assertTrue(channelBinding.id().contains("/pulsar-channel"));
    }
}
