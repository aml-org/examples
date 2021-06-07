import {
    AMFClient,
    client,
    exported,
    JsServerFileResourceLoader,
    resource,
    ResourceLoaderFactory,
    WebAPIConfiguration
} from "amf-client-js";
import Content = client.remote.Content
import AMFDocumentResult = exported.AMFDocumentResult
import ClientResourceLoader = client.resource.ClientResourceLoader
import {expect} from "chai";
import ResourceLoader = resource.ResourceLoader

describe("Resource Loader test", () => {
    it("Fetches file accessed with Mock Git Protocol", async () => {
        const customResourceLoader: ResourceLoader = ResourceLoaderFactory.create(new MockGitResourceLoader())
        const client: AMFClient = WebAPIConfiguration.WebAPI()
            .withResourceLoader(customResourceLoader)
            .createClient()
        const result: AMFDocumentResult = await client.parseDocument("git://resources/examples/banking-api.raml")
        expect(result.conforms).to.be.true
    })
})

class MockGitResourceLoader implements ClientResourceLoader {

    RESOURCE_TO_FETCH = "git://resources/examples/banking-api.raml"

    accepts(resource: string): boolean {
        return resource.startsWith("git://");
    }

    fetch(resource: string): Promise<Content> {
        const replacedResource = this.RESOURCE_TO_FETCH.replace("git://", "file://")
        return new JsServerFileResourceLoader().fetch(replacedResource)
    }

}
