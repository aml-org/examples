import {
    AMFBaseUnitClient,
    AMFDocumentResult,
    AMFParseResult,
    APIConfiguration,
    AsyncApi, AsyncAPIConfiguration, ChannelBinding,
    Document, DomainElement, EndPoint, Message, MessageBinding,
    OASConfiguration, Operation, OperationBinding,
    RAMLConfiguration, Request, Server, ServerBinding,
    Spec,
    WebApi,
    WebAPIConfiguration
} from 'amf-client-js';
import {expect} from 'chai';
import {describe} from "mocha";
import doc = Mocha.reporters.doc;
import List = Mocha.reporters.List;
import {Channel} from "node:diagnostics_channel";

describe('Parsing', () => {
    let client: AMFBaseUnitClient;

    describe('OAS 2.0', () => {
        beforeEach(() => {
            client = OASConfiguration.OAS20().baseUnitClient();
        });

        it('parse document from file', async () => {
            const parsingResult: AMFDocumentResult = await client.parseDocument(
                'file://src/test/resources/examples/banking-api.json'
            );
            expect(parsingResult.results).to.be.empty;
            expect(parsingResult.conforms).to.be.true;
        });

        it('parse document from string', async () => {
            const api = `{
                "swagger": "2.0",
                "info": {
                    "title": "ACME Banking HTTP API",
                    "version": "1.0"
                },
                "paths": {},
                "host": "acme-banking.com"
            }`;
            const result: AMFParseResult = await client.parseContent(api);
            expect(result.results).to.be.empty;
            expect(result.conforms).to.be.true;
            const document: Document = result.baseUnit as Document;
            const webApi: WebApi = document.encodes as WebApi;
            expect(webApi.name.value()).to.be.equal('ACME Banking HTTP API');
        });
    });
    describe('OAS 3.0', () => {
        beforeEach(() => {
            client = OASConfiguration.OAS30().baseUnitClient();
        });

        it('parse document from file', async () => {
            const parsingResult: AMFDocumentResult = await client.parseDocument(
                'file://src/test/resources/examples/banking-api-oas30.json'
            );
            expect(parsingResult.results).to.be.empty;
            expect(parsingResult.conforms).to.be.true;
        });

        it('parse document from string', async () => {
            const api = `{
                            "openapi": "3.0.0",
                            "info": {
                              "title": "Basic content",
                              "version": "0.1"
                            },
                            "paths": {}
                          }`;
            const result: AMFParseResult = await client.parseContent(api);
            expect(result.results).to.be.empty;
            expect(result.conforms).to.be.true;
            const document: Document = result.baseUnit as Document;
            const webApi: WebApi = document.encodes as WebApi;
            expect(webApi.name.value()).to.be.equal('Basic content');
        });
    });
    describe('RAML 1.0', () => {
        beforeEach(() => {
            client = RAMLConfiguration.RAML10().baseUnitClient();
        });

        it('parse document from file', async () => {
            const parsingResult: AMFDocumentResult = await client.parseDocument(
                'file://src/test/resources/examples/banking-api.raml'
            );
            expect(parsingResult.results).to.be.empty;
            expect(parsingResult.conforms).to.be.true;
        });

        it('parse document from string', async () => {
            const api = `#%RAML 1.0
                title: ACME Banking HTTP API
                version: 1.0
                `;
            const result: AMFParseResult = await client.parseContent(api);
            expect(result.results).to.be.empty;
            expect(result.conforms).to.be.true;
            const document: Document = result.baseUnit as Document;
            const webApi: WebApi = document.encodes as WebApi;
            expect(webApi.name.value()).to.be.equal('ACME Banking HTTP API');
        });
    });
    describe('RAML 0.8', () => {
        beforeEach(() => {
            client = RAMLConfiguration.RAML08().baseUnitClient();
        });

        it('parse document from file', async () => {
            const parsingResult: AMFDocumentResult = await client.parseDocument(
                'file://src/test/resources/examples/banking-api-08.raml'
            );
            expect(parsingResult.results).to.be.empty;
            expect(parsingResult.conforms).to.be.true;
        });

        it('parse document from string', async () => {
            const api = `#%RAML 0.8
                title: ACME Banking HTTP API
                version: 1.0
                `;
            const result: AMFParseResult = await client.parseContent(api);
            expect(result.results).to.be.empty;
            expect(result.conforms).to.be.true;
            const document: Document = result.baseUnit as Document;
            const webApi: WebApi = document.encodes as WebApi;
            expect(webApi.name.value()).to.be.equal('ACME Banking HTTP API');
        });
    });
    describe('Unknown WebAPI', () => {
        beforeEach(() => {
            client = WebAPIConfiguration.WebAPI().baseUnitClient();
        });

        it('parse document from file', async () => {
            const parsingResult: AMFParseResult = await client.parse(
                'file://src/test/resources/examples/banking-api.json'
            );
            expect(parsingResult.results).to.be.empty;
            expect(parsingResult.conforms).to.be.true;
            expect(parsingResult.sourceSpec.isOas).to.be.true;
            expect(parsingResult.sourceSpec.id).to.be.equal(Spec.OAS20.id);
        });

        it('parse document from string', async () => {
            const api = `#%RAML 0.8
                title: ACME Banking HTTP API
                version: 1.0
                `;
            const result: AMFParseResult = await client.parseContent(api);
            expect(result.results).to.be.empty;
            expect(result.conforms).to.be.true;
            const document: Document = result.baseUnit as Document;
            const webApi: WebApi = document.encodes as WebApi;
            expect(webApi.name.value()).to.be.equal('ACME Banking HTTP API');
            expect(result.sourceSpec.isRaml).to.be.true;
            expect(result.sourceSpec.id).to.be.equal(Spec.RAML08.id);
        });
    });
    describe('Unknown API', () => {
        beforeEach(() => {
            client = APIConfiguration.API().baseUnitClient();
        });

        it('parse document from file', async () => {
            const parsingResult: AMFParseResult = await client.parse(
                'file://src/test/resources/examples/async.yaml'
            );
            expect(parsingResult.results).to.be.empty;
            expect(parsingResult.conforms).to.be.true;
            expect(parsingResult.sourceSpec.isAsync).to.be.true;
            expect(parsingResult.sourceSpec.id).to.be.equal(Spec.ASYNC20.id);
        });

        it('parse document from string', async () => {
            const api = `{
                "asyncapi": "2.0.0",
                "info": {
                    "title": "Something",
                    "version": "1.0"
                },
                "channels": {},
            }`;

            const result: AMFParseResult = await client.parseContent(api);
            expect(result.results).to.be.empty;
            expect(result.conforms).to.be.true;
            const document: Document = result.baseUnit as Document;
            const asyncApi: AsyncApi = document.encodes as AsyncApi;
            expect(asyncApi.name.value()).to.be.equal('Something');
            expect(result.sourceSpec.isAsync).to.be.true;
            expect(result.sourceSpec.id).to.be.equal(Spec.ASYNC20.id);
        });
    });
    describe('AsyncAPI 2.0', () => {
        beforeEach(() => {
            client = AsyncAPIConfiguration.Async20().baseUnitClient()
        });
        it('parse 2.1 API from document', async () => {
            const parsingResult: AMFParseResult = await client.parse("file://src/test/resources/examples/asyncApi-2.1-all.yaml");
            const document: Document = parsingResult.baseUnit as Document;
            const asyncApi: AsyncApi = document.encodes as AsyncApi;
            // This version supports Mercure and IBMMQ bindings

            // Server Binding
            const servers: Array<Server> = asyncApi.servers;
            const theNameServer: Server = servers.pop();
            const serverBinding: ServerBinding = theNameServer.bindings.bindings.pop();

            // Channel Binding
            const channels: Array<EndPoint> = asyncApi.endPoints;
            const someOtherChannel: EndPoint = channels.at(2);
            const channelBinding: ChannelBinding = someOtherChannel.bindings.bindings.at(0);

            // Message Binding
            const publishOperation: Operation = someOtherChannel.operations.at(0);
            const messageRequest: Request = publishOperation.requests.at(0);
            const messageBinding = messageRequest.bindings.bindings.at(0);

            expect(parsingResult.results).to.be.empty;
            expect(parsingResult.conforms).to.be.true;
            expect(asyncApi.name.value()).to.be.equal('Market Data API');
            // Uncomment line when bug fixes are uploaded
            // expect(parsingResult.sourceSpec.isAsync).to.be.true;
            expect(parsingResult.sourceSpec.id).to.be.equal(Spec.ASYNC21.id);
            expect(serverBinding.id).to.contain("/ibmmq-server")
            expect(channelBinding.id).to.contain("/ibmmq-channel")
            expect(messageBinding.id).to.contain("/ibmmq-message")


        });
        it('parse 2.2 API from document', async () => {
            const parsingResult: AMFParseResult = await client.parse("file://src/test/resources/examples/asyncApi-2.2-all.yaml");
            const document: Document = parsingResult.baseUnit as Document;
            const asyncApi: AsyncApi = document.encodes as AsyncApi;
            // This version adds AnypointMQ bindings

            // Channel Binding
            const channels: Array<EndPoint> = asyncApi.endPoints;
            const anotherChannel: EndPoint = channels.at(3);
            const channelBinding = anotherChannel.bindings.bindings.at(0);

            // Message Binding
            const publishOperation: Operation = anotherChannel.operations.at(0);
            const messageRequest: Request = publishOperation.requests.at(0);
            const messageBinding: MessageBinding = messageRequest.bindings.bindings.at(0);


            expect(parsingResult.results).to.be.empty;
            expect(parsingResult.conforms).to.be.true;
            expect(asyncApi.name.value()).to.be.equal("Market Data API");
            // Uncomment line when bug fixes are uploaded
            // expect(parsingResult.sourceSpec.isAsync).to.be.true;
            expect(parsingResult.sourceSpec.id).to.be.equal(Spec.ASYNC22.id);
            expect(channelBinding.id).to.contain("/anypointmq-channel")
            expect(messageBinding.id).to.contain("/anypointmq-message")

        });
        it('parse 2.3 API from document', async () => {
            const parsingResult: AMFParseResult = await client.parse("file://src/test/resources/examples/asyncApi-2.3-all.yaml");
            const document: Document = parsingResult.baseUnit as Document;
            const asyncApi: AsyncApi = document.encodes as AsyncApi;
            // This version adds Solace bindings

            // Operation Binding
            const channels: Array<EndPoint> = asyncApi.endPoints;
            const forthChannel: EndPoint = channels.at(3);
            const publishOperation: Operation = forthChannel.operations.at(0);
            const operationBinding: OperationBinding = publishOperation.bindings.bindings.at(0);


            expect(parsingResult.results).to.be.empty;
            expect(parsingResult.conforms).to.be.true;
            expect(asyncApi.name.value()).to.be.equal("Market Data API");
            // Uncomment line when bug fixes are uploaded
            // expect(parsingResult.sourceSpec.isAsync).to.be.true;
            expect(parsingResult.sourceSpec.id).to.be.equal(Spec.ASYNC23.id);
            expect(operationBinding.id).to.contain("/solace-operation")

        });
        it('parse 2.4 API from document', async () => {
            const parsingResult: AMFParseResult = await client.parse("file://src/test/resources/examples/asyncApi-2.4-all.yaml");
            const document: Document = parsingResult.baseUnit as Document;
            const asyncApi: AsyncApi = document.encodes as AsyncApi;

            // No new bindings for this version

            expect(parsingResult.results).to.be.empty;
            expect(parsingResult.conforms).to.be.true;
            expect(asyncApi.name.value()).to.be.equal("Market Data API");
            // Uncomment line when bug fixes are uploaded
            // expect(parsingResult.sourceSpec.isAsync).to.be.true;
            expect(parsingResult.sourceSpec.id).to.be.equal(Spec.ASYNC24.id);
        });
        it('parse 2.5 API from document', async () => {
            const parsingResult: AMFParseResult = await client.parse("file://src/test/resources/examples/asyncApi-2.5-all.yaml");
            const document: Document = parsingResult.baseUnit as Document;
            const asyncApi: AsyncApi = document.encodes as AsyncApi;
            const declares: Array<DomainElement> = document.declares
            // This version adds GooglePubSub binding.

            // Channel Binding
            const channels: Array<EndPoint> = asyncApi.endPoints;
            const topicProtoSchema: EndPoint = channels.at(4);
            const channelBinding: ChannelBinding = topicProtoSchema.bindings.bindings.at(0);

            //Message Binding
            const messageComponent: Message = declares.at(2) as Message;
            const messageBinding: MessageBinding = messageComponent.bindings.bindings.at(0);

            expect(parsingResult.results).to.be.empty;
            expect(parsingResult.conforms).to.be.true;
            expect(asyncApi.name.value()).to.be.equal("Market Data API");
            // Uncomment line when bug fixes are uploaded
            // expect(parsingResult.sourceSpec.isAsync).to.be.true;
            expect(parsingResult.sourceSpec.id).to.be.equal(Spec.ASYNC25.id);
            // Uncomment line when bug fixes are uploaded
            // expect(channelBinding.id).to.contain("/googlepubsub-channel-010")
            expect(messageBinding.id).to.contain("/googlepubsub-message")
        });
        it('parse 2.6 API from document', async () => {
            const parsingResult: AMFParseResult = await client.parse("file://src/test/resources/examples/asyncApi-2.6-all.yaml");
            const document: Document = parsingResult.baseUnit as Document;
            const asyncApi: AsyncApi = document.encodes as AsyncApi;
            // This version adds Pulsar bindings.

            // Server Binding
            const servers: Array<Server> = asyncApi.servers;
            const theNameServer: Server = servers.at(2);
            const serverBinding: ServerBinding = theNameServer.bindings.bindings.at(0);

            // Channel Binding
            const channels: Array<EndPoint> = asyncApi.endPoints;
            const sixthChannel: EndPoint = channels.at(5);
            const channelBinding: ChannelBinding = sixthChannel.bindings.bindings.at(0);


            expect(parsingResult.results).to.be.empty;
            expect(parsingResult.conforms).to.be.true;
            expect(asyncApi.name.value()).to.be.equal("Market Data API");
            // Uncomment line when bug fixes are uploaded
            // expect(parsingResult.sourceSpec.isAsync).to.be.true;
            expect(parsingResult.sourceSpec.id).to.be.equal(Spec.ASYNC26.id);
            expect(serverBinding.id).to.contain("/pulsar-server")
            // Uncomment line when bug fixes are uploaded
            // expect(channelBinding.id).to.contain("/pulsar-channel")

        });
    })
});
