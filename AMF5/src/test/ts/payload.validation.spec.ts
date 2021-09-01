import {
  AMFBaseUnitClient,
  AMFConfiguration,
  AMFDocumentResult,
  AMFResult,
  Document, RAMLConfiguration,
  ValidationMode,
  WebApi,
} from "amf-client-js";
import { expect } from "chai";

describe("Shape payload validation", () => {
  const APPLICATION_JSON = "application/json";
  const VALID_PAYLOAD = {
    firstname: "aFirstName",
    lastname: "aLastName",
    age: 173,
  };
  const INVALID_PAYLOAD = {
    firstname: true,
    lastname: 56,
    age: "187",
  };

  it("validates shape correctly", async () => {
    const config: AMFConfiguration = RAMLConfiguration.RAML10();
    const client: AMFBaseUnitClient = config.baseUnitClient();
    const result: AMFDocumentResult = await client.parseDocument(
      "file://src/test/resources/examples/simple-api.raml"
    );
    const transformResult: AMFResult = client.transform(result.document);
    const unit: Document = transformResult.baseUnit as Document;
    const api: WebApi = unit.encodes as WebApi;
    const userSchema = api.endPoints[0].operations[0].request.payloads[0].schema;
    const validator = config.elementClient().payloadValidatorFor(
      userSchema,
      APPLICATION_JSON,
      ValidationMode.StrictValidationMode
    );
    const validResult = await validator.validate(JSON.stringify(VALID_PAYLOAD));
    const invalidResult = await validator.validate(JSON.stringify(INVALID_PAYLOAD));
    expect(validResult.results).to.be.empty;
    expect(validResult.conforms).to.be.ok;
    expect(invalidResult.results).to.not.be.empty;
    expect(invalidResult.conforms).to.not.be.ok;
  });
});
