import {
    AMFBaseUnitClient,
    AMFDocumentResult,
    AMFParseResult,
    APIConfiguration,
    AsyncApi, AsyncAPIConfiguration,
    Document,
    OASConfiguration,
    RAMLConfiguration,
    Spec,
    WebApi,
    WebAPIConfiguration
} from 'amf-client-js';
import {expect} from 'chai';
import {describe} from "mocha";
import doc = Mocha.reporters.doc;

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
            expect(parsingResult.results).to.be.empty;
            expect(parsingResult.conforms).to.be.true;
            const document: Document = parsingResult.baseUnit as Document;
            const asyncApi: AsyncApi = document.encodes as AsyncApi;
            expect(asyncApi.name.value()).to.be.equal('Market Data API');
            // Uncomment line when bug fixes are uploaded
            // expect(parsingResult.sourceSpec.isAsync).to.be.true;
            expect(parsingResult.sourceSpec.id).to.be.equal(Spec.ASYNC21.id);

        });
        it('parse 2.2 API from document', async () => {
            const parsingResult: AMFParseResult = await client.parse("file://src/test/resources/examples/asyncApi-2.2-all.yaml");
            expect(parsingResult.results).to.be.empty;
            expect(parsingResult.conforms).to.be.true;
            const document: Document = parsingResult.baseUnit as Document;
            const asyncApi: AsyncApi = document.encodes as AsyncApi;
            expect(asyncApi.name.value()).to.be.equal("Market Data API");
            // Uncomment line when bug fixes are uploaded
            // expect(parsingResult.sourceSpec.isAsync).to.be.true;
            expect(parsingResult.sourceSpec.id).to.be.equal(Spec.ASYNC22.id);

        });
        it('parse 2.3 API from document', async () => {
            const parsingResult: AMFParseResult = await client.parse("file://src/test/resources/examples/asyncApi-2.3-all.yaml");
            expect(parsingResult.results).to.be.empty;
            expect(parsingResult.conforms).to.be.true;
            const document: Document = parsingResult.baseUnit as Document;
            const asyncApi: AsyncApi = document.encodes as AsyncApi;
            expect(asyncApi.name.value()).to.be.equal("Market Data API");
            // Uncomment line when bug fixes are uploaded
            // expect(parsingResult.sourceSpec.isAsync).to.be.true;
            expect(parsingResult.sourceSpec.id).to.be.equal(Spec.ASYNC23.id);

        });
        it('parse 2.4 API from document', async () => {
            const parsingResult: AMFParseResult = await client.parse("file://src/test/resources/examples/asyncApi-2.4-all.yaml");
            expect(parsingResult.results).to.be.empty;
            expect(parsingResult.conforms).to.be.true;
            const document: Document = parsingResult.baseUnit as Document;
            const asyncApi: AsyncApi = document.encodes as AsyncApi;
            expect(asyncApi.name.value()).to.be.equal("Market Data API");
            // Uncomment line when bug fixes are uploaded
            // expect(parsingResult.sourceSpec.isAsync).to.be.true;
            expect(parsingResult.sourceSpec.id).to.be.equal(Spec.ASYNC24.id);
        });
        it('parse 2.5 API from document', async () => {
            const parsingResult: AMFParseResult = await client.parse("file://src/test/resources/examples/asyncApi-2.5-all.yaml");
            expect(parsingResult.results).to.be.empty;
            expect(parsingResult.conforms).to.be.true;
            const document: Document = parsingResult.baseUnit as Document;
            const asyncApi: AsyncApi = document.encodes as AsyncApi;
            expect(asyncApi.name.value()).to.be.equal("Market Data API");
            // Uncomment line when bug fixes are uploaded
            // expect(parsingResult.sourceSpec.isAsync).to.be.true;
            expect(parsingResult.sourceSpec.id).to.be.equal(Spec.ASYNC25.id);
        });
        it('parse 2.6 API from document', async () => {
            const parsingResult: AMFParseResult = await client.parse("file://src/test/resources/examples/asyncApi-2.6-all.yaml");
            expect(parsingResult.results).to.be.empty;
            expect(parsingResult.conforms).to.be.true;
            const document: Document = parsingResult.baseUnit as Document;
            const asyncApi: AsyncApi = document.encodes as AsyncApi;
            expect(asyncApi.name.value()).to.be.equal("Market Data API");
            // Uncomment line when bug fixes are uploaded
            // expect(parsingResult.sourceSpec.isAsync).to.be.true;
            expect(parsingResult.sourceSpec.id).to.be.equal(Spec.ASYNC26.id);
        });
    })
});
