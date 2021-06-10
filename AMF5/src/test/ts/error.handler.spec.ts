import {client, exported, handler, OASConfiguration, resolve, ErrorHandler, AMFClient} from "amf-client-js";
import * as chaiAsPromised from "chai-as-promised";
import {expect, use as useChai} from "chai";
import ErrorHandlerProvider = exported.ErrorHandlerProvider
import AMFDocumentResult = exported.AMFDocumentResult

useChai(chaiAsPromised);

class MyErrorHandler implements handler.JsErrorHandler {

    private readonly err: Error;

    constructor(err: Error) {
        this.err = err
    }

    report(result: client.validate.ValidationResult): void {
        throw this.err
    }

    getResults(): Array<client.validate.ValidationResult> {
        return [];
    }
}

describe("Use custom error handler", () => {

    // TODO: check, doesn't work.
    const error = new Error("A WILD ERROR APPEARED!")
    const unhandledProvider: ErrorHandlerProvider = ErrorHandler.provider(new MyErrorHandler(error))

    it("throws exception on violation", async () => {
        const client: AMFClient = OASConfiguration
            .OAS30()
            .withErrorHandlerProvider(unhandledProvider)
            .createClient()
        const failedPromise: Promise<AMFDocumentResult> = client.parseDocument("file://resources/examples/banking-api-oas30-error.json")
        await expect(failedPromise).to.not.eventually.be.rejectedWith(new Error("NOT MY ERROR"))
        await expect(failedPromise).to.eventually.be.rejectedWith(error)
    })
})
