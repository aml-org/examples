import { DataTypes, OASConfiguration, ScalarShape } from 'amf-client-js';
import { expect } from 'chai';
import * as fs from 'fs';
import * as path from 'path';

describe('Shape serialization', () => {
  const EXPECTED_SCHEMA = path.join(__dirname, '../resources/expected/draft4-scalar-schema.json');

  it('serializes a shape to Json Schema', () => {
    const client = OASConfiguration.OAS20().elementClient();
    const shape = new ScalarShape()
      .withId('someId')
      .withDataType(DataTypes.Double)
      .withMinimum(2)
      .withMaximum(7);
    const jsonSchemaString = client.toJsonSchema(shape);
    const expectedContent = fs.readFileSync(EXPECTED_SCHEMA, { encoding: 'utf8' });
    expect(jsonSchemaString).to.be.equal(expectedContent);
  });
});
