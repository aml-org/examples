import amf.MessageStyles;
import amf.ProfileNames;
import amf.client.AMF;
import amf.client.environment.DefaultEnvironment;
import amf.client.environment.Environment;
import amf.client.model.document.BaseUnit;
import amf.client.parse.Raml10Parser;
import amf.client.remote.Content;
import amf.client.resource.FileResourceLoader;
import amf.client.resource.ResourceLoader;
import amf.client.validate.ValidationReport;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.File;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.Assert.*;

public class ResourceLoaderTest {

    @BeforeClass
    public static void setup() throws ExecutionException, InterruptedException {
        AMF.init().get();
    }

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
        // add custom resource loader to the environment
        final Environment env = DefaultEnvironment.apply().add(new CustomResourceLoader());

        // pass the environment with the ResourceLoader to the parser
        final Raml10Parser parser = new Raml10Parser(env);

        // using a custom protocol that our CustomResourceLoader can parse
        final BaseUnit model = parser.parseFileAsync("CustomProtocol/resources/examples/banking-api.raml").get();
        assertNotNull(model);

        // Run RAML default validations on parsed unit (expects no errors).
        final ValidationReport report = AMF.validate(model, ProfileNames.RAML(), MessageStyles.RAML()).get();
        System.out.println("report.conforms() == " + report.conforms());
        assertTrue(report.conforms());
    }
}
