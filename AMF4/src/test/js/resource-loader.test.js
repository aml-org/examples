const amf = require('amf-client-js');

beforeAll(() => {
    amf.AMF.init();

});

class CustomResourceLoader {
    static CUSTOM_PATTERN = "CustomProtocol";

    constructor() {
        this.resourceLoader = new amf.JsServerFileResourceLoader();
    }

    // has the logic for fetching in a custom way
    fetch(resource) {
        const customLogicResult = resource.toString().replace(CustomResourceLoader.CUSTOM_PATTERN, 'file:/');
        return this.resourceLoader.fetch(customLogicResult);
    }

    // returns true if it can fetch the resource
    accepts(resource) {
        if (resource === null || resource === undefined || resource === "") return false;
        return resource.startsWith(CustomResourceLoader.CUSTOM_PATTERN);
    }
}

test('Validate RAML 1.0 with custom resource loader', () => {
    const env = amf.client.DefaultEnvironment.apply().addClientLoader(new CustomResourceLoader());
    const parser = new amf.Raml10Parser(env);

    return parser.parseFileAsync('CustomProtocol/src/test/resources/examples/banking-api.raml')
        .then(model => {
            expect(model).not.toBeNull();
            expect(model).toBeDefined();
        });
})
