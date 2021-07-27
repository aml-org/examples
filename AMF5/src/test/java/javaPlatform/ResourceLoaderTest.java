package javaPlatform;

import amf.apicontract.client.platform.AMFBaseUnitClient;
import amf.apicontract.client.platform.AMFConfiguration;
import amf.apicontract.client.platform.RAMLConfiguration;
import amf.core.client.common.remote.Content;
import amf.core.client.common.validation.ProfileNames;
import amf.core.client.platform.model.document.BaseUnit;
import amf.core.client.platform.resource.FileResourceLoader;
import amf.core.client.platform.resource.ResourceLoader;
import amf.core.client.platform.validation.AMFValidationReport;
import org.junit.Test;

import java.io.File;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class ResourceLoaderTest {

    @Test
    public void validateRamlWithResourceLoader() throws ExecutionException, InterruptedException {
        // add custom resource loader to the configuration
        final AMFConfiguration config = RAMLConfiguration.RAML10().withResourceLoader(new CustomResourceLoader());
        final AMFBaseUnitClient client = config.baseUnitClient();

        // using a custom protocol that our CustomResourceLoader can parse
        final BaseUnit model = client.parse("CustomProtocol/src/test/resources/examples/banking-api.raml").get().baseUnit();
        assertNotNull(model);

        // Run RAML default validations on parsed unit (expects no errors).
        final AMFValidationReport report = client.validate(model).get();
        System.out.println("report.conforms() == " + report.conforms());
        assertTrue(report.conforms());
    }
}
