import amf.MessageStyles;
import amf.ProfileNames;
import amf.client.exported.AMFClient;
import amf.client.exported.AMFConfiguration;
import amf.client.exported.RAMLConfiguration;
import amf.client.model.document.BaseUnit;
import amf.client.remote.Content;
import amf.client.resource.FileResourceLoader;
import amf.client.resource.ResourceLoader;
import amf.client.validate.AMFValidationReport;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.File;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class ResourceLoaderTest {

    private static class CustomResourceLoader implements ResourceLoader {
        private final FileResourceLoader resourceLoader = new FileResourceLoader();
        private final Pattern CUSTOM_PATH_PATTERN = Pattern.compile("^CustomProtocol/");

        @Override
        public CompletableFuture<Content> fetch(String path) {
            final String normalizedPath = path.substring(CUSTOM_PATH_PATTERN.pattern().length() - 1);
            return resourceLoader.fetch(new File(normalizedPath).getAbsolutePath());
        }

        @Override
        public boolean accepts(String resource) {
            if (resource == null || resource.isEmpty()) return false;
            final Matcher matcher = CUSTOM_PATH_PATTERN.matcher(resource);
            return matcher.find();
        }
    }

    @Test
    public void validateRamlWithResourceLoader() throws ExecutionException, InterruptedException {
        // add custom resource loader to the configuration
        final AMFConfiguration config = RAMLConfiguration.RAML().withResourceLoader(new CustomResourceLoader());
        final AMFClient client = config.createClient();

        // using a custom protocol that our CustomResourceLoader can parse
        final BaseUnit model = client.parse("CustomProtocol/resources/examples/banking-api.raml").get().baseUnit();
        assertNotNull(model);

        // Run RAML default validations on parsed unit (expects no errors).
        final AMFValidationReport report = client.validate(model, ProfileNames.RAML10()).get();
        System.out.println("report.conforms() == " + report.conforms());
        assertTrue(report.conforms());
    }
}
