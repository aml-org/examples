const amf = require('amf-client-js');
const pipelines = amf.ResolutionPipeline;
const fs = require('fs')
const path = require('path');

beforeAll(() => {
    amf.AMF.init();

});

test('RAML 1.0 to OAS 2.0', () => {
    const parser = new amf.Raml10Parser();
    const resolver = new amf.Oas20Resolver();
    const renderer = new amf.Oas20Renderer();

    return parser.parseFileAsync("file://src/test/resources/examples/banking-api.raml").then(ramlApi => {
        const convertedOasApi = resolver.resolve(ramlApi, pipelines.COMPATIBILITY_PIPELINE)
        return renderer.generateString(convertedOasApi).then(result => {
            const expectedResult = fs.readFileSync(path.resolve(__dirname, '../../../resources/expected/converted-banking-api.json'), 'utf8')
            expect(result).toEqual(expectedResult);
        });
    })
})


test('OAS 2.0 to RAML 1.0', () => {
    const parser = new amf.Oas20Parser();
    const resolver = new amf.Raml10Resolver();
    const renderer = new amf.Raml10Renderer();

    return parser.parseFileAsync("file://src/test/resources/examples/banking-api.json").then(OasApi => {
        const convertedRamlApi = resolver.resolve(OasApi, pipelines.COMPATIBILITY_PIPELINE)
        return renderer.generateString(convertedRamlApi).then(result => {
            const expectedResult = fs.readFileSync(path.resolve(__dirname, '../../../resources/expected/converted-banking-api.raml'), 'utf8')
            expect(result).toEqual(expectedResult);
        });
    })
})
