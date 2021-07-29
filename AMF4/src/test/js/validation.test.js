const amf = require('amf-client-js');
const path = require('path');

const AMF = amf.AMF;
const ProfileNames = amf.ProfileNames;
const MessageStyles = amf.MessageStyles;

beforeAll(() => {
    amf.plugins.features.AMFValidation.register();
    amf.AMF.init();
});

test('Validate RAML 1.0', () => {
    const parser = new amf.Raml10Parser();

    return parser.parseFileAsync(`file://${path.resolve(__dirname, '../../../resources/examples/banking-api-error.raml')}`)
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

    return parser.parseFileAsync(`file://${path.resolve(__dirname, '../../../resources/examples/banking-api-error.raml')}`)
        .then(model => {
            expect(model).not.toBeNull();
            expect(model).toBeDefined();

            const customValidationProfilePath = `file://${path.resolve(__dirname, '../../../resources/validation_profile.raml')}`;

            // Run RAML custom validations with a validation profile that accepts the previously invalid protocol value
            // TODO: Fix
            // Failed: bc {
            //   "Hg": null,
            //   "SJa": true,
            //   "Xs": "Cannot load a custom validation profile for this",
            //   "ZX": null,
            //   "stackdata": [Circular],
            // }
            return AMF.loadValidationProfile(customValidationProfilePath)
                .then(customProfile => {
                    return AMF.validate(model, customProfile, MessageStyles.RAML)
                        .then(report => {
                            console.log(`report.conforms() == ${report.conforms}`)
                            expect(report.conforms).toBe(true)
                        })
                })
        });
})
