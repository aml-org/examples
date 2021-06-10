import {core, AMFClient, PipelineName, TransformationPipeline, WebAPIConfiguration, exported} from "amf-client-js";
import {expect} from "chai";
import * as fileSystem from "fs";

import Vendor = core.Vendor
import AMFDocumentResult = exported.AMFDocumentResult
import AMFResult = exported.AMFResult

describe("Conversion", () => {

    let client: AMFClient
    const OAS_20_CONVERSION = PipelineName.from(Vendor.OAS20.name, TransformationPipeline.COMPATIBILITY_PIPELINE)
    const RAML_10_CONVERSION = PipelineName.from(Vendor.RAML10.name, TransformationPipeline.COMPATIBILITY_PIPELINE)
    const RAML_10_CONVERTED_GOLDEN = "resources/expected/converted-banking-api.raml"
    const OAS_20_CONVERTED_GOLDEN = "resources/expected/converted-banking-api.json"

    beforeEach(() => {
        client = WebAPIConfiguration.WebAPI().createClient()
    })

    it("Convert RAML 1.0 to OAS 2.0", async () => {
        const golden: string = fileSystem.readFileSync(OAS_20_CONVERTED_GOLDEN, {encoding: "utf8"})
        const parseResult: AMFDocumentResult = await client.parseDocument("file://resources/examples/banking-api.raml")
        expect(parseResult.results).to.be.empty
        const transformResult: AMFResult = await client.transform(parseResult.baseUnit, OAS_20_CONVERSION)
        expect(transformResult.results).to.be.empty
        const rendered: string = client.render(transformResult.baseUnit, Vendor.OAS20.mediaType)
        expect(rendered).to.be.equal(golden, `Rendered: ${rendered} \nGolden: ${golden}`)
    });

    it("Convert OAS 2.0 to RAML 1.0", async() => {
        const golden: string = fileSystem.readFileSync(RAML_10_CONVERTED_GOLDEN, {encoding: "utf8"})
        const parseResult: AMFDocumentResult = await client.parseDocument("file://resources/examples/banking-api.json")
        expect(parseResult.results).to.be.empty
        const transformResult: AMFResult = await client.transform(parseResult.baseUnit, RAML_10_CONVERSION)
        expect(transformResult.results).to.be.empty
        const rendered: string = client.render(transformResult.baseUnit, Vendor.RAML10.mediaType)
        expect(rendered).to.be.equal(golden, `Rendered: ${rendered} \nGolden: ${golden}`)
    });
})
