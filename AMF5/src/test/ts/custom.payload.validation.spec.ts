import {
  AMFPayloadValidationPluginConverter,
  AMFValidationReport,
  DataTypes,
  JsAMFPayloadValidationPlugin,
  JsPayloadValidator,
  OASConfiguration,
  PayloadFragment,
  ProfileName,
  ScalarShape,
  Shape,
  ShapeValidationConfiguration,
  ValidatePayloadRequest,
  ValidationMode
} from 'amf-client-js';
import { expect } from 'chai';

describe('Custom Payload Validation', () => {
  const CUSTOM_MEDIATYPE = 'application/my-cool-mediatype';

  it('uses my custom validator', async () => {
    const myPayloadPlugin = AMFPayloadValidationPluginConverter.toAMF(
      new MyPayloadValidationPlugin(CUSTOM_MEDIATYPE)
    );
    const config = OASConfiguration.OAS30().withShapePayloadPlugin(myPayloadPlugin);
    const shape = new ScalarShape().withDataType(DataTypes.Boolean);
    const validator = config
      .elementClient()
      .payloadValidatorFor(shape, CUSTOM_MEDIATYPE, ValidationMode.StrictValidationMode);
    const report = await validator.validate('somePayload');

    expect(report.conforms).to.be.true;
  });
});

class MyPayloadValidationPlugin implements JsAMFPayloadValidationPlugin {
  id = 'MyPlugin';
  ID: string = this.id;
  readonly mediaTypeToValidate: string;

  constructor(mediaTypeToValidate: string) {
    this.mediaTypeToValidate = mediaTypeToValidate;
  }

  applies(element: ValidatePayloadRequest): boolean {
    return element.mediaType == this.mediaTypeToValidate;
  }

  validator(
    shape: Shape,
    mediaType: string,
    config: ShapeValidationConfiguration,
    validationMode: ValidationMode
  ): JsPayloadValidator {
    return new MySpyCustomValidator(shape);
  }
}

class MySpyCustomValidator implements JsPayloadValidator {
  shape: Shape;
  isExecuted = false;

  constructor(shape: Shape) {
    this.shape = shape;
  }

  syncValidate(payload: string): AMFValidationReport {
    this.isExecuted = true;
    return new AMFValidationReport(this.shape.id, ProfileName.apply('RAML 1.0'), []);
  }

  validate(payload: string): Promise<AMFValidationReport>;
  validate(payloadFragment: PayloadFragment): Promise<AMFValidationReport>;
  validate(payload: string | PayloadFragment): Promise<AMFValidationReport> {
    return Promise.resolve(this.syncValidate(''));
  }
}
