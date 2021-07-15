import {
    AMFClient,
    AMFDocumentResult,
    AMFResult,
    OASConfiguration,
    ParsingOptions,
    ProvidedMediaType, RenderOptions
} from "amf-client-js";
import {expect} from "chai";

describe("Configuration", () => {
    it("define parsing and render options", async () => {
        let client: AMFClient = OASConfiguration.OAS20()
            .withParsingOptions(new ParsingOptions().setMaxYamlReferences(20))
            .withRenderOptions(new RenderOptions().withSourceMaps).createClient();
        const parsingResult: AMFResult = await client.parse("file://src/test/resources/examples/banking-api.json");
        const jsonld: string = client.render(parsingResult.baseUnit);
        expect(jsonld.includes("[(87,12)-(87,39)]"));
    });
})
