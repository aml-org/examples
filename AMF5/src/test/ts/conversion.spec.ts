import {
  WebAPIConfiguration,
  AMFDocumentResult,
  AMFResult,
  ProvidedMediaType, AMFBaseUnitClient,
} from "amf-client-js";
import { expect } from "chai";
import * as fileSystem from "fs";

describe("Conversion", () => {
  let client: AMFBaseUnitClient;
  const RAML_10_CONVERTED_GOLDEN = "src/test/resources/expected/converted-banking-api.raml";
  const OAS_20_CONVERTED_GOLDEN = "src/test/resources/expected/converted-banking-api.json";

  beforeEach(() => {
    client = WebAPIConfiguration.WebAPI().baseUnitClient();
  });

  it("Convert RAML 1.0 to OAS 2.0", async () => {
    const golden: string = fileSystem.readFileSync(OAS_20_CONVERTED_GOLDEN, {
      encoding: "utf8",
    });
    const parseResult: AMFDocumentResult = await client.parseDocument(
      "file://src/test/resources/examples/banking-api.raml"
    );
    expect(parseResult.results).to.be.empty;
    const transformResult: AMFResult = await client.transformCompatibility(
      parseResult.baseUnit,
      ProvidedMediaType.Oas20
    );
    expect(transformResult.results).to.be.empty;
    const rendered: string = client.render(transformResult.baseUnit, ProvidedMediaType.Oas20);
    expect(rendered).to.be.equal(golden, `Rendered: ${rendered} \nGolden: ${golden}`);
  });

  it("Convert OAS 2.0 to RAML 1.0", async () => {
    const golden: string = fileSystem.readFileSync(RAML_10_CONVERTED_GOLDEN, {
      encoding: "utf8",
    });
    const parseResult: AMFDocumentResult = await client.parseDocument(
      "file://src/test/resources/examples/banking-api.json"
    );
    expect(parseResult.results).to.be.empty;
    const transformResult: AMFResult = await client.transformCompatibility(
      parseResult.baseUnit,
      ProvidedMediaType.Raml10
    );
    expect(transformResult.results).to.be.empty;
    const rendered: string = client.render(transformResult.baseUnit, ProvidedMediaType.Raml10);
    expect(rendered).to.be.equal(golden, `Rendered: ${rendered} \nGolden: ${golden}`);
  });
});
