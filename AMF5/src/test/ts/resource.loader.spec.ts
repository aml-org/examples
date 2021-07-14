import {
  AMFClient,
  AMFDocumentResult,
  ClientResourceLoader,
  Content,
  JsServerFileResourceLoader,
  ResourceLoader,
  ResourceLoaderFactory,
  WebAPIConfiguration,
} from "amf-client-js";
import { expect } from "chai";

describe("Resource Loader test", () => {
  it("Fetches file accessed with Mock Git Protocol", async () => {
    const customResourceLoader: ResourceLoader = ResourceLoaderFactory.create(
      new MockGitResourceLoader()
    );
    const client: AMFClient = WebAPIConfiguration.WebAPI()
      .withResourceLoader(customResourceLoader)
      .createClient();
    const result: AMFDocumentResult = await client.parseDocument(
      "git://src/test/resources/examples/banking-api.raml"
    );
    expect(result.conforms).to.be.true;
  });
});

class MockGitResourceLoader implements ClientResourceLoader {
  RESOURCE_TO_FETCH = "git://src/test/resources/examples/banking-api.raml";

  accepts(resource: string): boolean {
    return resource.startsWith("git://");
  }

  fetch(resource: string): Promise<Content> {
    const replacedResource = this.RESOURCE_TO_FETCH.replace("git://", "file://");
    return new JsServerFileResourceLoader().fetch(replacedResource);
  }
}
