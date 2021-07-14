package javaPlatform;

import amf.apicontract.client.platform.RAMLConfiguration;
import amf.apicontract.client.platform.model.domain.EndPoint;
import amf.apicontract.client.platform.model.domain.Operation;
import amf.apicontract.client.platform.model.domain.Payload;
import amf.apicontract.client.platform.model.domain.Request;
import amf.apicontract.client.platform.model.domain.api.WebApi;
import amf.core.client.common.remote.Content;
import amf.core.client.common.validation.ValidationMode;
import amf.core.client.platform.AMFGraphClient;
import amf.core.client.platform.AMFGraphConfiguration;
import amf.core.client.platform.AMFResult;
import amf.core.client.platform.execution.ExecutionEnvironment;
import amf.core.client.platform.model.document.BaseUnit;
import amf.core.client.platform.model.document.Document;
import amf.core.client.platform.resource.FileResourceLoader;
import amf.core.client.platform.resource.LoaderWithExecutionContext;
import amf.core.client.platform.resource.ResourceLoader;
import amf.core.client.platform.validation.AMFValidationReport;
import amf.shapes.client.platform.model.domain.AnyShape;
import org.junit.Test;
import scala.concurrent.ExecutionContext;
import java.util.Collections;
import java.util.concurrent.*;

public class SchedulerTest {
    @Test
    public void schedulerTest() throws InterruptedException, ExecutionException {
        /* Instantiating a ScheduledExecutorService with some example arguments. */
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(30, new ThreadFactory() {
            int i = 0;

            @Override
            public Thread newThread(Runnable r) {
                i = i + 1;
                final Thread thread = new Thread(r);
                thread.setName("AMF-" + i);
                return thread;
            }
        });

        /* Instantiating a ExecutionEnvironment passing as argument the scheduler previously created. */
        ExecutionEnvironment executionEnvironment = new ExecutionEnvironment(scheduler);

        final AMFGraphConfiguration config = RAMLConfiguration.RAML10()
                .withResourceLoaders(Collections.singletonList(new CustomResourceLoader())) // adds custom loader that uses execution context
                .withExecutionEnvironment(executionEnvironment); // execution context is defined, resource loader is adjusted.
        final AMFGraphClient client = config.createClient();

        /* call async interfaces */

        final AMFResult parseResult = client.parse("file://src/test/resources/examples/simple-api.raml").get();
        AMFValidationReport report = client.validate(parseResult.baseUnit()).get();
        AMFValidationReport payloadReport = config.payloadValidatorFactory()
                .createFor(obtainShapeFromUnit(parseResult.baseUnit()), "application/json", ValidationMode.StrictValidationMode())
                .validate("{\"name\": \"firstname and lastname\"}").get();
//        config.forInstance

        /* Shutting down the scheduler which kills the AMF threads created in the thread pool provided by that scheduler. */
        scheduler.shutdownNow();
    }

    private AnyShape obtainShapeFromUnit(BaseUnit b) {
        final WebApi webApi = (WebApi) ((Document) b).encodes();
        final EndPoint usersEndpoint = webApi.endPoints().get(0);
        final Operation postMethod = usersEndpoint.operations().get(0);
        final Request request = postMethod.requests().get(0);
        final Payload userPayload = request.payloads().get(0);
        return (AnyShape) userPayload.schema();
    }

    /*
        resource loader that uses execution context. Extending LoaderWithExecutionContext allows the configuration
        to adapt the loader to the new execution context if modified.
     */
    private static class CustomResourceLoader implements ResourceLoader, LoaderWithExecutionContext {
        private final FileResourceLoader resourceLoader;

        @Override
        public ResourceLoader withExecutionContext(ExecutionContext ec) {
            return new CustomResourceLoader(ec);
        }

        public CustomResourceLoader() {
            this.resourceLoader = new FileResourceLoader();
        }

        public CustomResourceLoader(ExecutionContext ec) {
            this.resourceLoader = new FileResourceLoader(ec);
        }

        @Override
        public CompletableFuture<Content> fetch(String path) {
            return resourceLoader.fetch(path);
        }

        @Override
        public boolean accepts(String resource) {
            return resourceLoader.accepts(resource);
        }
    }
}
