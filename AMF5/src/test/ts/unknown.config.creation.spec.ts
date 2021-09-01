import {APIConfiguration, ProfileNames} from "amf-client-js";
import { expect } from "chai";


describe('Unknown config creation', () => {
    it("can create a configuration from a parsed API result", async () => {
        const apiPath = "file://src/test/resources/examples/banking-api-error.raml"
        const parseResult = await APIConfiguration.API().baseUnitClient().parse(apiPath)

        // 'specificConfig' should have all the RAML10 settings configured in the AMFConfiguration object
        const specificConfig = APIConfiguration.fromSpec(parseResult.sourceSpec)
        const report = await specificConfig.baseUnitClient().validate(parseResult.baseUnit)
        expect(report.conforms).to.be.false
        expect(report.profile).to.eq(ProfileNames.RAML10)
        expect(report.results[0].message).to.eq("Protocols must have a case insensitive value matching http or https")
    })
});
