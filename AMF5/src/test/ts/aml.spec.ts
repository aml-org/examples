import {AMLClient, AMLConfiguration, exported, model} from "amf-client-js";
import {expect} from "chai";
import Dialect = model.document.Dialect;
import DialectInstance = model.document.DialectInstance;
import DialectDomainElement = model.domain.DialectDomainElement;
import AMLDialectResult = exported.AMLDialectResult;
import AMLDialectInstanceResult = exported.AMLDialectInstanceResult;


describe("AML Operations", () => {

    const simpleDialect: string = "file://resources/examples/dialect.yaml"
    const simpleDialectInstance: string = "file://resources/examples/dialect-instance.yaml"
    const simpleNodeTypeUri: string = "file://resources/examples/dialect.yaml#/declarations/Simple"

    it("parses a dialect", async () => {
        const amlConfig: AMLConfiguration = AMLConfiguration.predefined()
        const client: AMLClient = amlConfig.createClient()
        const parseResult: AMLDialectResult = await client.parseDialect(simpleDialect)
        expect(parseResult.conforms).to.be.true
        const dialect: Dialect = parseResult.baseUnit as Dialect
        const dialectElementId: string = dialect.documents().root().encoded().value()
        expect(dialectElementId).to.be.equal("file://resources/examples/dialect.yaml#/declarations/Simple")
    })

    it("parse a dialect instance", async () => {
        const amlConfig: AMLConfiguration = await AMLConfiguration.predefined().withDialect(simpleDialect)
        const client: AMLClient = amlConfig.createClient()
        const parseResult: AMLDialectInstanceResult = await client.parseDialectInstance(simpleDialectInstance)
        expect(parseResult.conforms).to.be.true
        const instance: DialectInstance = parseResult.dialectInstance as DialectInstance
        const instanceElement: DialectDomainElement = instance.encodes as DialectDomainElement
        expect(instanceElement.getTypeUris()).to.contain(simpleNodeTypeUri)
    })
})
