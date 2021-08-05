import {
    AMFDocumentResult,
    AMFResult,AMFBaseUnitClient, RAMLConfiguration, OASConfiguration, PipelineId,
} from "amf-client-js";
import { expect } from "chai";
import * as fileSystem from "fs";

describe("Conversion", () => {
  let raml10Client: AMFBaseUnitClient;
  let oas20Client: AMFBaseUnitClient;
  const RAML_10_CONVERTED_GOLDEN = "src/test/resources/expected/converted-banking-api.raml";
  const OAS_20_CONVERTED_GOLDEN = "src/test/resources/expected/converted-banking-api.json";

  beforeEach(() => {
    raml10Client = RAMLConfiguration.RAML10().baseUnitClient()
    oas20Client = OASConfiguration.OAS20().baseUnitClient()
  });

  it("Convert RAML 1.0 to OAS 2.0", async () => {
    const golden: string = fileSystem.readFileSync(OAS_20_CONVERTED_GOLDEN, {
      encoding: "utf8",
    });
    const parseResult: AMFDocumentResult = await raml10Client.parseDocument(
      "file://src/test/resources/examples/banking-api.raml"
    );
    expect(parseResult.results).to.be.empty;
    const transformResult: AMFResult = await oas20Client.transform(
      parseResult.baseUnit,
      PipelineId.Compatibility
    );
    expect(transformResult.results).to.be.empty;
    const rendered: string = oas20Client.render(transformResult.baseUnit, "application/json");
    expect(rendered).to.be.equal(golden, `Rendered: ${rendered} \nGolden: ${golden}`);
  });

  it("Convert OAS 2.0 to RAML 1.0", async () => {
    const golden: string = fileSystem.readFileSync(RAML_10_CONVERTED_GOLDEN, {
      encoding: "utf8",
    });
    const parseResult: AMFDocumentResult = await oas20Client.parseDocument(
      "file://src/test/resources/examples/banking-api.json"
    );
    expect(parseResult.results).to.be.empty;
    const transformResult: AMFResult = await raml10Client.transform(
      parseResult.baseUnit,
      PipelineId.Compatibility
    );
    expect(transformResult.results).to.be.empty;
    const rendered: string = raml10Client.render(transformResult.baseUnit, "application/yaml");
    expect(rendered).to.be.equal(golden, `Rendered: ${rendered} \nGolden: ${golden}`);
  });
});
