const amf = require('amf-client-js');
const pipelines = amf.ResolutionPipeline;

beforeAll(() => {
    // initializes all AMF plugins, although we only need WebApi Plugin and AMF Core functionality
    amf.AMF.init();

});

test('Resolve RAML 1.0', () => {
    const parser = new amf.Raml10Parser();
    const resolver = new amf.Raml10Resolver();
    const renderer = new amf.Raml10Renderer(); // to console.log the resolved model

    return parser.parseFileAsync('file://src/test/resources/examples/banking-api.raml').then(unresolvedModel => {
        expect(unresolvedModel).not.toBeNull();
        expect(unresolvedModel).toBeDefined();

        const resolvedModel = resolver.resolve(unresolvedModel, pipelines.COMPATIBILITY_PIPELINE)
        expect(resolvedModel).not.toBeNull();
        expect(resolvedModel).toBeDefined();

        // has amf-specific fields for cross-spec conversion support
        renderer.generateString(resolvedModel).then(m => console.log(m));
    });
})

test('Resolve OAS 3.0', () => {
    const parser = new amf.Oas30Parser();
    const resolver = new amf.Oas20Resolver()
    const renderer = new amf.Oas30Renderer(); // to console.log the resolved model

    return parser.parseFileAsync('file://src/test/resources/examples/banking-api-oas30.json').then(unresolvedModel => {
        expect(unresolvedModel).not.toBeNull();
        expect(unresolvedModel).toBeDefined();

        // using default pipeline
        const resolvedModel = resolver.resolve(unresolvedModel)
        expect(resolvedModel).not.toBeNull();
        expect(resolvedModel).toBeDefined();

        // it's identical to the source file because all schemas and parameters where already inlined
        // (and the default pipeline was used)
        renderer.generateString(resolvedModel).then(m => console.log(m));
    });
})
