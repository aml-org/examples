import {
    AMFDocumentResult, AMFEvent, AMFEventListenerFactory,
    JsAMFEventListener, OASConfiguration,


} from "amf-client-js";
import {expect} from "chai";

describe("Event listener", () => {

    it("custom listener", async () => {
        let eventSpy = new EventSpy();
        let client = OASConfiguration.OAS20()
            .withEventListener(AMFEventListenerFactory.from(eventSpy)).baseUnitClient();
        const parsingResult: AMFDocumentResult = await client.parseDocument(
            "file://src/test/resources/examples/banking-api.json"
        );
        expect(parsingResult.results).to.be.empty;
        expect(parsingResult.conforms).to.be.true;
        expect(eventSpy.events).to.be.not.empty;
    });

    class EventSpy implements JsAMFEventListener {
        events: string[];
        constructor() {
            this.events = []
        }
        notifyEvent(event: AMFEvent): void {
            this.events.push(event.name)
        }
    }
});
