import {
  AMFBaseUnitClient,
  AMFDocumentResult,
  AMFResult,
  Document,
  EndPoint,
  Operation,
  PipelineId,
  PipelineName,
  Server,
  Vendor,
  WebApi,
  WebAPIConfiguration,
} from "amf-client-js";
import { expect } from "chai";

describe("Transform RAML APIs", () => {
  let client: AMFBaseUnitClient;

  beforeEach(() => {
    client = WebAPIConfiguration.WebAPI().baseUnitClient();
  });

  describe("RAML 1.0", () => {
    it("applies resource types and traits, applies inheritance, etc", async () => {
      const parseResult: AMFDocumentResult = await client.parseDocument(
        "file://src/test/resources/examples/raml-resource-type.raml"
      );
      const transformed: AMFResult = client.transform(parseResult.baseUnit);
      const doc: Document = transformed.baseUnit as Document;
      const api: WebApi = doc.encodes as WebApi;
      expect(api.endPoints[0].operations).to.not.empty;
    });

    it("applies overlays to document", async () => {
      const parseResult: AMFDocumentResult = await client.parseDocument(
        "file://src/test/resources/examples/raml-overlay/test-overlay.raml"
      );
      const transformed: AMFResult = client.transform(parseResult.baseUnit);
      const doc: Document = transformed.baseUnit as Document;
      expect(doc.references()).to.be.empty;
      const api: WebApi = doc.encodes as WebApi;
      const existsEndpointSlashOthers: EndPoint | undefined = api.endPoints.find(
        (e) => e.path.value() == "/others"
      );
      expect(existsEndpointSlashOthers).to.not.be.undefined;
    });
  });

  describe("OAS 3.0", () => {
    it("transforms the document", async () => {
      const parseResult: AMFDocumentResult = await client.parseDocument(
        "file://src/test/resources/examples/banking-api-oas30.json"
      );
      const transformed: AMFResult = client.transform(parseResult.baseUnit);
      const doc: Document = transformed.baseUnit as Document;
      const api: WebApi = doc.encodes as WebApi;
      const apiOperations: Operation[] = api.endPoints.flatMap((e) => e.operations);
      const firstServerOfEachOperation: Server[] = apiOperations.map((op) => op.servers[0]);
      expect(firstServerOfEachOperation).to.not.contain(
        undefined,
        "There is an operation without server"
      );
      expect(firstServerOfEachOperation).to.have.length(
        apiOperations.length,
        "Each operation should have a server"
      );
    });
  });
});
