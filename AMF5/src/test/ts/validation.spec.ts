import {
  AMFClient,
  AMFValidationReport,
  AMFConfiguration,
  ValidationProfile,
  AMFDocumentResult,
  ProfileName,
  ProfileNames,
  RAMLConfiguration,
} from "amf-client-js";
import { expect } from "chai";

describe("RAML 1.0 validation", () => {
  it("Validates model and examples", async () => {
    const client: AMFClient = RAMLConfiguration.RAML10().createClient();
    const result: AMFDocumentResult = await client.parseDocument(
      "file://src/test/resources/examples/banking-api-error.raml"
    );
    expect(result.conforms).to.be.true;
    const validationResult: AMFValidationReport = await client.validate(result.document);
    expect(validationResult.conforms).to.be.false;
    expect(validationResult.results[0].message).to.be.equal(
      "Protocols must have a case insensitive value matching http or https"
    );
  });
});
