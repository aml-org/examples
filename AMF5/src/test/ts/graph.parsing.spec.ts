import {AMFClient, exported, model, OASConfiguration} from "amf-client-js"
import {expect} from "chai";
import AMFDocumentResult = exported.AMFDocumentResult
import WebApi = model.domain.WebApi

describe("AMF Graph", () => {

    let client: AMFClient

    beforeEach(() => {
        client = OASConfiguration.OAS20().createClient()
    })

    it("parse document from file", async () => {
        const parsingResult: AMFDocumentResult = await client.parseDocument("file://resources/examples/banking-api.jsonld")
        expect(parsingResult.results).to.be.empty
        expect(parsingResult.conforms).to.be.true
        const webApi: WebApi = parsingResult.document.encodes as WebApi
        expect(webApi.servers[0].url).not.to.be.undefined
    })
})
