const amf = require('amf-client-js');
const AMF = amf.AMF;
const ProfileNames = amf.ProfileNames;
const MessageStyles = amf.MessageStyles;

beforeAll(() => {
    amf.AMF.init();

});

test('Validate RAML 1.0', () => {
    const parser = new amf.Raml10Parser();

    return parser.parseFileAsync('file://resources/examples/banking-api-error.raml')
        .then(model => {
            expect(model).not.toBeNull();
            expect(model).toBeDefined();

            // Run RAML default validations on parsed unit (expects one error -> invalid protocols value)
            return AMF.validate(model, ProfileNames.RAML, MessageStyles.RAML)
                .then(report => {
                    console.log(`report.conforms() == ${report.conforms}`)
                    expect(report.conforms).toBe(false)
                })
        });
})

test('Validate RAML 1.0 with custom validation', () => {
    const parser = new amf.Raml10Parser();

    return parser.parseFileAsync('file://resources/examples/banking-api-error.raml')
        .then(model => {
            expect(model).not.toBeNull();
            expect(model).toBeDefined();

            // Run RAML custom validations with a validation profile that accepts the previously invalid protocol value
            return AMF.loadValidationProfile('file://resources/validation_profile.raml')
                .then(customProfile => {

                    return AMF.validate(model, customProfile, MessageStyles.RAML)
                        .then(report => {
                            console.log(`report.conforms() == ${report.conforms}`)
                            expect(report.conforms).toBe(true)
                        })
                })
        });
})
