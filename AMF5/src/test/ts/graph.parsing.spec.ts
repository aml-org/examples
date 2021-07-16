import {AMFBaseUnitClient, AMFDocumentResult, OASConfiguration, WebApi} from "amf-client-js";
import { expect } from "chai";

const BANKING_API = "file://src/test/resources/examples/banking-api.flattened.jsonld";

describe("AMF Graph", () => {
  let client: AMFBaseUnitClient;

  beforeEach(() => {
    client = OASConfiguration.OAS20().baseUnitClient();
  });

  it("parse document from file", async () => {
    const parsingResult: AMFDocumentResult = await client.parseDocument(BANKING_API);
    expect(parsingResult.results).to.be.empty;
    expect(parsingResult.conforms).to.be.true;
    const webApi: WebApi = parsingResult.document.encodes as WebApi;
    expect(webApi.servers[0].url).not.to.be.undefined;
  });
});
