const amf = require('amf-client-js');
const path = require('path');

let payloadValidator;

beforeAll(() => {
    amf.AMF.init();

    const parser = new amf.Raml10Parser();
    const resolver = new amf.Raml10Resolver();

    return parser.parseFileAsync(`file://${path.resolve(__dirname, '../../../resources/examples/simple-api.raml')}`)
        .then(unresolvedModel => {
            const resolvedModel = resolver.resolve(unresolvedModel)

            const webApi = resolvedModel.encodes;
            const usersEndpoint = webApi.endPoints[0];
            const postMethod = usersEndpoint.operations[0];
            const request = postMethod.requests[0];
            const userPayload = request.payloads[0];
            const userSchema = userPayload.schema;

            payloadValidator = userSchema.payloadValidator("application/json")
        });
});

test('isValid test', () => {
    const invalidUserPayload = "{\"name\": \"firstname and lastname\"}";
    return payloadValidator.isValid("application/json", invalidUserPayload).then(isValid => {
        expect(isValid).toBeFalsy();
    })
})


test('validate test', () => {
    const invalidUserPayload = "{\"name\": \"firstname and lastname\"}";
    return payloadValidator.validate("application/json", invalidUserPayload).then(report => {
        expect(report.conforms).toBeFalsy();
    })
})

test('syncValidate test', () => {
    const invalidUserPayload = "{\"name\": \"firstname and lastname\"}";
    const result = payloadValidator.syncValidate("application/json", invalidUserPayload);
    expect(result.conforms).toBeFalsy();
})
