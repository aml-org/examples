import {
  AMFBaseUnitClient,
  AMFDocumentResult,
  AMFResult,
  Document,
  OASConfiguration,
  RAMLConfiguration,
  WebApi,
} from "amf-client-js";
import { expect } from "chai";

describe("Parsing", () => {
  let client: AMFBaseUnitClient;

  describe("OAS 2.0", () => {
    beforeEach(() => {
      client = OASConfiguration.OAS20().baseUnitClient();
    });

    it("parse document from file", async () => {
      const parsingResult: AMFDocumentResult = await client.parseDocument(
        "file://src/test/resources/examples/banking-api.json"
      );
      expect(parsingResult.results).to.be.empty;
      expect(parsingResult.conforms).to.be.true;
    });

    it("parse document from string", async () => {
      const api: string = `{
                "swagger": "2.0",
                "info": {
                    "title": "ACME Banking HTTP API",
                    "version": "1.0"
                },
                "paths": {},
                "host": "acme-banking.com"
            }`;
      const result: AMFResult = await client.parseContent(api);
      expect(result.results).to.be.empty;
      expect(result.conforms).to.be.true;
      const document: Document = result.baseUnit as Document;
      const webApi: WebApi = document.encodes as WebApi;
      expect(webApi.name.value()).to.be.equal("ACME Banking HTTP API");
    });
  });
  describe("OAS 3.0", () => {
    beforeEach(() => {
      client = OASConfiguration.OAS30().baseUnitClient();
    });

    it("parse document from file", async () => {
      const parsingResult: AMFDocumentResult = await client.parseDocument(
        "file://src/test/resources/examples/banking-api-oas30.json"
      );
      expect(parsingResult.results).to.be.empty;
      expect(parsingResult.conforms).to.be.true;
    });
  });
  describe("RAML 1.0", () => {
    beforeEach(() => {
      client = RAMLConfiguration.RAML10().baseUnitClient();
    });

    it("parse document from file", async () => {
      const parsingResult: AMFDocumentResult = await client.parseDocument(
        "file://src/test/resources/examples/banking-api.raml"
      );
      expect(parsingResult.results).to.be.empty;
      expect(parsingResult.conforms).to.be.true;
    });

    it("parse document from string", async () => {
      const api: string = `#%RAML 1.0
                title: ACME Banking HTTP API
                version: 1.0
                `;
      const result: AMFResult = await client.parseContent(api);
      expect(result.results).to.be.empty;
      expect(result.conforms).to.be.true;
      const document: Document = result.baseUnit as Document;
      const webApi: WebApi = document.encodes as WebApi;
      expect(webApi.name.value()).to.be.equal("ACME Banking HTTP API");
    });
  });
  describe("RAML 0.8", () => {
    beforeEach(() => {
      client = RAMLConfiguration.RAML08().baseUnitClient();
    });

    it("parse document from file", async () => {
      const parsingResult: AMFDocumentResult = await client.parseDocument(
        "file://src/test/resources/examples/banking-api-08.raml"
      );
      expect(parsingResult.results).to.be.empty;
      expect(parsingResult.conforms).to.be.true;
    });

    it("parse document from string", async () => {
      const api: string = `#%RAML 0.8
                title: ACME Banking HTTP API
                version: 1.0
                `;
      const result: AMFResult = await client.parseContent(api);
      expect(result.results).to.be.empty;
      expect(result.conforms).to.be.true;
      const document: Document = result.baseUnit as Document;
      const webApi: WebApi = document.encodes as WebApi;
      expect(webApi.name.value()).to.be.equal("ACME Banking HTTP API");
    });
  });
});
