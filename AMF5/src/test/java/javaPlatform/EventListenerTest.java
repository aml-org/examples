package javaPlatform;

import amf.apicontract.client.platform.AMFBaseUnitClient;
import amf.apicontract.client.platform.WebAPIConfiguration;
import amf.core.client.platform.AMFResult;
import amf.core.client.platform.config.AMFEvent;
import amf.core.client.platform.config.AMFEventListener;
import amf.core.client.platform.config.AMFEventNames;
import amf.core.client.platform.config.StartingParsingEvent;
import amf.core.client.platform.model.document.BaseUnit;
import amf.core.client.platform.model.document.Document;
import amf.core.client.platform.model.domain.DomainElement;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class EventListenerTest {

    @Test
    public void parseAMFGraph() throws ExecutionException, InterruptedException {
        final SpyEventListener spy = new SpyEventListener();
        final AMFBaseUnitClient client = WebAPIConfiguration.WebAPI()
                .withEventListener(spy)
                .baseUnitClient();

        final AMFResult parseResult = client.parse("file://src/test/resources/examples/banking-api.raml").get();

        final BaseUnit model = parseResult.baseUnit();
        assertNotNull(model);
        assertTrue(parseResult.conforms());
        assert(spy.listenedTo.size() != 0);
    }

    private class SpyEventListener implements AMFEventListener {

        public List<StartingParsingEvent> listenedTo = new ArrayList<>();

        @Override
        public void notifyEvent(AMFEvent event) {
            if (event.name().equals(AMFEventNames.StartedParse())) {
                listenedTo.add((StartingParsingEvent) event);
            }
        }
    }
}
