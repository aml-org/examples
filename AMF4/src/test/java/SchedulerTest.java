import amf.MessageStyles;
import amf.ProfileNames;
import amf.client.AMF;
import amf.client.environment.DefaultEnvironment;
import amf.client.environment.Environment;
import amf.client.execution.ExecutionEnvironment;
import amf.client.model.document.BaseUnit;
import amf.client.parse.Raml10Parser;
import amf.client.validate.ValidationReport;
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
        /* Most of AMF client interfaces related could receive an ExecutionEnvironment as argument */
        ExecutionEnvironment executionEnvironment = new ExecutionEnvironment(scheduler);

        /* Instantiating a Environment using the executionEnvironment previously created. */
        Environment environment = DefaultEnvironment.apply(executionEnvironment);

        /* Initializing AMF passing as argument the executionEnvironment previously created. */
        AMF.init(executionEnvironment).get();

        /* Instantiating a Raml10Parser passing as argument the Environment. */
        Raml10Parser parser = new Raml10Parser(environment);

        /* Parsing Raml 10 with specified file returning future. */
        BaseUnit baseUnit = parser.parseFileAsync("file://src/test/resources/examples/banking-api.raml").get();

        /* Validating the Raml 10 spec. */
        ValidationReport report = parser.reportValidation(ProfileNames.RAML10(), MessageStyles.RAML()).get();

        /* Shutting down the scheduler which kills the AMF threads created in the thread pool provided by that scheduler. */
        scheduler.shutdownNow();
    }
}
