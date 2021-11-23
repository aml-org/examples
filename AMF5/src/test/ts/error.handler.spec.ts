import {
  AMFBaseUnitClient,
  AMFDocumentResult,
  AMFValidationResult,
  ErrorHandler,
  ErrorHandlerProvider,
  JsErrorHandler,
  OASConfiguration
} from 'amf-client-js';
import * as chaiAsPromised from 'chai-as-promised';
import { expect, use as useChai } from 'chai';

useChai(chaiAsPromised);

class MyErrorHandler implements JsErrorHandler {
  private readonly err: Error;

  constructor(err: Error) {
    this.err = err;
  }

  report(result: AMFValidationResult): void {
    throw this.err;
  }

  getResults(): Array<AMFValidationResult> {
    return [];
  }
}

describe('Use custom error handler', () => {
  const error = new Error('A WILD ERROR APPEARED!');
  const unhandledProvider: ErrorHandlerProvider = ErrorHandler.provider(new MyErrorHandler(error));

  it('throws exception on violation', async () => {
    const client: AMFBaseUnitClient = OASConfiguration.OAS30()
      .withErrorHandlerProvider(unhandledProvider)
      .baseUnitClient();
    const failedPromise: Promise<AMFDocumentResult> = client.parseDocument(
      'file://src/test/resources/examples/banking-api-oas30-error.json'
    );
    await expect(failedPromise).to.not.eventually.be.rejectedWith(new Error('NOT MY ERROR'));
    await expect(failedPromise).to.eventually.be.rejectedWith(error);
  });
});
