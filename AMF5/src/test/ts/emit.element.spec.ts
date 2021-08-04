import {DataTypes, OASConfiguration, org, Payload, Response, ScalarShape} from "amf-client-js";
import { expect } from "chai";
import * as fs from "fs"
import * as path from "path"

describe("DomainElement emission", () => {

    const RENDERED_RESPONSE = path.join(__dirname, "../resources/expected/emitted-response.json")

    it("emits a Response", () => {
        const payload = new Payload()
            .withMediaType("application/json")
            .withSchema(new ScalarShape().withDataType(DataTypes.Boolean).withId("aScalar"))
            .withId("somethingElse");
        const response = new Response()
            .withStatusCode("401")
            .withDisplayName("My Example Response")
            .withDescription("An example response")
            .withPayloads([payload])
            .withId("someId");
        const client = OASConfiguration.OAS30().elementClient();
        const builder = new org.yaml.builder.JsOutputBuilder();
        client.renderToBuilder(response, builder)
        const responseNode = builder.result as any
        const responseAsString = JSON.stringify(responseNode, null, 2).trim()
        const expected = fs.readFileSync(RENDERED_RESPONSE, { encoding: 'utf8'}).trim();
        expect(responseAsString).to.be.equal(expected)
    })
})
