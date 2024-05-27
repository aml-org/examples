package javaPlatform;

import amf.apicontract.client.platform.*;
import amf.apicontract.client.platform.model.domain.EndPoint;
import amf.apicontract.client.platform.model.domain.Message;
import amf.apicontract.client.platform.model.domain.Operation;
import amf.apicontract.client.platform.model.domain.Server;
import amf.apicontract.client.platform.model.domain.api.AsyncApi;
import amf.apicontract.client.platform.model.domain.bindings.ChannelBinding;
import amf.apicontract.client.platform.model.domain.bindings.MessageBinding;
import amf.apicontract.client.platform.model.domain.bindings.OperationBinding;
import amf.apicontract.client.platform.model.domain.bindings.ServerBinding;
import amf.apicontract.internal.metamodel.domain.ServerModel;
import amf.core.client.platform.AMFParseResult;
import amf.core.client.platform.model.document.BaseUnit;
import amf.core.client.platform.model.document.Document;
import amf.core.client.platform.model.domain.DomainElement;
import amf.core.client.scala.model.domain.AmfArray;
import amf.core.client.scala.model.domain.AmfElement;
import amf.core.client.scala.vocabulary.ValueType;
import amf.core.internal.parser.domain.FieldEntry;
import amf.core.internal.remote.Spec;
import org.junit.Test;
import scala.collection.mutable.Iterable;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
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
        final BaseUnit model = parseResult.baseUnit();
        final Spec spec = parseResult.sourceSpec();
        final Document encodes = (Document) model;
        final AsyncApi asyncApi = ((AsyncApi) encodes.encodes());
        // This version supports Mercure and IBMMQ bindings

        // Server Binding
        final List<amf.apicontract.client.platform.model.domain.Server> serverField = asyncApi.servers();
        final Server server = serverField.get(0);
        final ServerBinding serverBinding = server.bindings().bindings().get(0);

        // Channel Binding
        final List<EndPoint> endpoints = (asyncApi).endPoints();
        final EndPoint someOtherChannel = endpoints.get(2);
        final ChannelBinding channelBinding = someOtherChannel.bindings().bindings().get(0);

        // Message Binding
        final Operation publishOperation = someOtherChannel.operations().get(0);
        final MessageBinding messageBinding = publishOperation.request().bindings().bindings().get(0);


        assertNotNull(model);
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
        final BaseUnit model = parseResult.baseUnit();
        final Spec spec = parseResult.sourceSpec();
        final Document encodes = (Document) model;
        final AsyncApi asyncApi = ((AsyncApi) encodes.encodes());
        // This version adds AnypointMQ bindings


        // Channel Binding
        final List<EndPoint> endpoints = asyncApi.endPoints();
        final EndPoint anotherChannel = endpoints.get(3);
        final ChannelBinding channelBinding = anotherChannel.bindings().bindings().get(0);

        // Message Binding
        final Operation publishOperation = anotherChannel.operations().get(0);
        final MessageBinding messageBinding = publishOperation.request().bindings().bindings().get(0);

        assertNotNull(model);
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
        final BaseUnit model = parseResult.baseUnit();
        final Spec spec = parseResult.sourceSpec();
        final Document encodes = (Document) model;
        final AsyncApi asyncApi = ((AsyncApi) encodes.encodes());
        // This version adds Solace bindings

        // Operation Binding
        final List<EndPoint> endPoints = asyncApi.endPoints();
        System.out.println(endPoints.size());
        final EndPoint fourthChannel = endPoints.get(3);
        final Operation publishOperation = fourthChannel.operations().get(0);
        final OperationBinding operationBinding = publishOperation.bindings().bindings().get(0);


        assertNotNull(model);
        assertTrue(parseResult.conforms());
        assertNotNull(asyncApi);
        assertTrue(spec.isAsync());
        assertEquals(spec.id(), Spec.ASYNC23().id());
        assertTrue(operationBinding.id().contains("/solace-operation"));
    }

    @Test
    public void parseAsyncApi24() throws ExecutionException, InterruptedException {
        final AMFBaseUnitClient client = AsyncAPIConfiguration.Async20().baseUnitClient();

        final AMFParseResult parseResult = client.parse("file://src/test/resources/examples/asyncApi-2.4-all.yaml").get();
        final BaseUnit model = parseResult.baseUnit();
        final Spec spec = parseResult.sourceSpec();
        // No new bindings in this version

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
        final Document encodes = (Document) model;
        final AsyncApi asyncApi = ((AsyncApi) encodes.encodes());
        final List<DomainElement> declares = ((Document) model).declares();
        // This version adds GooglePubSub binding.

        // Channel Binding
        final List<EndPoint> endPoints = asyncApi.endPoints();
        final EndPoint topicProtoSchema = endPoints.get(4);
        final ChannelBinding channelBinding = topicProtoSchema.bindings().bindings().get(0);

        // Message Binding
        final Message messageComponent = (Message) declares.get(2);
        final MessageBinding messageBinding = messageComponent.bindings().bindings().get(0);

        assertNotNull(model);
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
        final BaseUnit model = parseResult.baseUnit();
        final Spec spec = parseResult.sourceSpec();
        final Document encodes = (Document) model;
        final AsyncApi asyncApi = ((AsyncApi) encodes.encodes());
        // This version adds Pulsar bindings.

        // Server Binding
        final List<Server> servers = asyncApi.servers();
        final Server server = servers.get(2);
        final ServerBinding serverBinding = server.bindings().bindings().get(0);

        // Channel Binding
        final List<EndPoint> endPoints = asyncApi.endPoints();
        final EndPoint sixthChannel = endPoints.get(5);
        final ChannelBinding channelBinding = sixthChannel.bindings().bindings().get(0);

        assertNotNull(model);
        assertTrue(parseResult.conforms());
        assertNotNull(asyncApi);
        assertTrue(spec.isAsync());
        assertEquals(spec.id(), Spec.ASYNC26().id());
        assertTrue(serverBinding.id().contains("/pulsar-server"));
        // Uncomment this line when bug is fixed
//        assertTrue(channelBinding.id().contains("/pulsar-channel"));
    }
}
