import {
    AMFClient,
    OASConfiguration, org

} from "amf-client-js";
import { expect } from "chai";

describe("Parsing", () => {
    let client: AMFClient;

    describe("OAS 2.0", () => {
        beforeEach(() => {
            client = OASConfiguration.OAS20().createClient();
        });

        it("should output json-ld as json object", async () => {
            const result = await client.parse("file://src/test/resources/examples/banking-api.json")
            expect(result.conforms).to.be.true
            const unit = result.baseUnit
            const builder = new org.yaml.builder.JsOutputBuilder()
            const obj: any = client.renderGraphToBuilder(unit, builder)
            expect(obj["@graph"][0]["@id"]).to.equal("file://src/test/resources/examples/banking-api.json#/web-api")
        })
    });
});
