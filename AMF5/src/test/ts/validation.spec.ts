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
      "file://resources/examples/banking-api-error.raml"
    );
    expect(result.conforms).to.be.true;
    const validationResult: AMFValidationReport = await client.validate(result.document);
    expect(validationResult.conforms).to.be.false;
    expect(validationResult.results[0].message).to.be.equal(
      "Protocols must have a case insensitive value matching http or https"
    );
  });

  it("Validates model with custom profile", async () => {
    const conf: AMFConfiguration = await RAMLConfiguration.RAML10().withCustomValidationsEnabled();
    const client: AMFClient = await conf
      .withCustomProfile("file://resources/validation_profile.raml")
      .then((conf) => conf.createClient());
    const result: AMFDocumentResult = await client.parseDocument(
      "file://resources/examples/banking-api-error.raml"
    );
    expect(result.conforms).to.be.true;
    const validationResult: AMFValidationReport = await client.validate(
      result.document,
      new ProfileName("Banking")
    );
    expect(validationResult.conforms).to.be.true;
    expect(validationResult.results).to.have.length(1);
    expect(validationResult.results[0].level).to.be.equal("Warning");
    expect(validationResult.results[0].validationId).to.be.equal(
      "http://a.ml/vocabularies/amf/parser#raml-root-schemes-values"
    );
  });

  it("Validates model with parsed custom profile", async () => {
    const conf: AMFConfiguration = await RAMLConfiguration.RAML10().withCustomValidationsEnabled();
    const validationProfile: ValidationProfile = await conf
      .createClient()
      .parseValidationProfile("file://resources/validation_profile.raml");
    expect(validationProfile.profileName().profile).to.be.equal("Banking");
    expect(validationProfile.baseProfile().profile).to.be.equal(ProfileNames.RAML10.profile);
    const client: AMFClient = conf.withCustomProfile(validationProfile).createClient();
    const result: AMFDocumentResult = await client.parseDocument(
      "file://resources/examples/banking-api-error.raml"
    );
    expect(result.conforms).to.be.true;
    const validationResult: AMFValidationReport = await client.validate(
      result.document,
      validationProfile.profileName()
    );
    expect(validationResult.conforms).to.be.true;
    expect(validationResult.results).to.have.length(1);
    expect(validationResult.results[0].level).to.be.equal("Warning");
    expect(validationResult.results[0].validationId).to.be.equal(
      "http://a.ml/vocabularies/amf/parser#raml-root-schemes-values"
    );
  });
});
