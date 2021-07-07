package javaPlatform;

import amf.apicontract.client.platform.RAMLConfiguration;
import amf.core.client.platform.AMFGraphClient;
import amf.core.client.platform.AMFGraphConfiguration;
import amf.core.client.platform.AMFResult;
import amf.core.client.platform.execution.ExecutionEnvironment;
import amf.core.client.platform.validation.AMFValidationReport;
import org.junit.Test;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;

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
                .withExecutionEnvironment(executionEnvironment);
        final AMFGraphClient client = config.createClient();

        /* Parsing Raml 10 with specified file returning future. */
        final AMFResult parseResult = client.parse("file://src/test/resources/examples/banking-api.raml").get();

        /* Validating the Raml 10 spec. */
        AMFValidationReport report = client.validate(parseResult.baseUnit()).get();

        /* Shutting down the scheduler which kills the AMF threads created in the thread pool provided by that scheduler. */
        scheduler.shutdownNow();
    }
}
