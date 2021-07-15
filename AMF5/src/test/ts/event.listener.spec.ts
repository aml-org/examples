import {
    AMFDocumentResult,
    AMFGraphConfiguration, Document, OASConfiguration,
    TransformationPipeline,
    TransformationPipelineBuilder,
    TransformationStepFactory,
    amf
} from "amf-client-js";
import {expect} from "chai";

describe("Event listener", () => {

    // it("custom listener", async () => {
    //     var eventSpy = new EventSpy();
    //     let client = OASConfiguration.OAS20()
    //         .withEventListener(AMFEventListenerFactory.from(eventSpy)).createClient();
    //     const parsingResult: AMFDocumentResult = await client.parseDocument(
    //         "file://src/test/resources/examples/banking-api.json"
    //     );
    //     expect(parsingResult.results).to.be.empty;
    //     expect(parsingResult.conforms).to.be.true;
    //     expect(eventSpy.events).to.be.not.empty;
    // });
    //
    // class EventSpy implements JsAMFEventListener {
    //     events: string[];
    //     constructor() {
    //         this.events = []
    //     }
    //     notifyEvent(event: amf.core.client.platform.config.AMFEvent): void {
    //         this.events.push(event.name)
    //     }
    // }
});
