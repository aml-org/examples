import {
    JsonLDSchemaConfiguration,
    JsonLDSchemaConfigurationClient,
    JsonLDSchemaResult,
    JsonLDInstanceResult,
    JsonLDObject, JsonLDInstanceDocument
} from 'amf-client-js';
import {expect} from "chai";

describe ('JSONLD Schema Parsed',() => {
    it('parse jsonLD Schema', async ()=>{

        const client: JsonLDSchemaConfigurationClient = JsonLDSchemaConfiguration.JsonLDSchema().baseUnitClient()
        const jsonLDSchemaResult: JsonLDSchemaResult = await client.parseJsonLDSchema("file://src/test/resources/jsonld-schemas/schema.json");
        const jsonLDInstanceResult: JsonLDInstanceResult = await client.parseJsonLDInstance("file://src/test/resources/jsonld-schemas/instance.json", jsonLDSchemaResult.jsonDocument);
        const jsonLDObject: JsonLDObject = jsonLDInstanceResult.instance.encodes[0] as JsonLDObject;
        expect(jsonLDObject.graph().containsProperty("anypoint://vocabulary/security.yaml#sensitive")).to.be.true;
    });
});

//volver a adoptar la version del nuevo snapchat, deberia ser 65
