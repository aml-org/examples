const amf = require('amf-client-js');
const path = require('path');
const AMF = amf.AMF;
const pathToResources = path.join(
    __dirname,
    '..',
    '..',
    'main',
    'resources',
    'examples',
    'banking-api.json'
    );

beforeAll(() => {
    AMF.init();
});

test('parse OAS 2.0', () => {
    const parser = new amf.Oas20Parser();

    const pathToResources = path.join(
        __dirname,
        '..',
        '..',
        'main',
        'resources',
        'examples',
        'banking-api.json'
    );

    parser.parseFileAsync(`file://${path.resolve(pathToResources)}`).then(model => {
        expect(model).not.toBeNull();
        expect(model).toBeDefined();

    }).catch(err => console.log(err));
})

test('parse RAML 1.0', () => {
    const parser = new amf.Raml10Parser();

    parser.parseFileAsync("file://src/main/resources/examples/banking-api.raml").then(model => {
        expect(model).not.toBeNull();
        expect(model).toBeDefined();

    }).catch(err => console.log(err));
})

test('parse OAS 2.0 from string', () => {
    const parser = new amf.Oas20Parser();

    const api =
        '{\n' +
        '  \'swagger\': \'2.0\',\n' +
        '  \'info\': {\n' +
        '    \'title\': \'ACME Banking HTTP API\',\n' +
        '    \'version\': \'1.0\'\n' +
        '  },\n' +
        '  \'host\': \'acme-banking.com\'' +
        '}';

    parser.parseStringAsync(api).then(model => {
        expect(model).not.toBeNull();
        expect(model).toBeDefined();
        expect(model.raw).toEqual(api);

    }).catch(err => console.log(err));
})
