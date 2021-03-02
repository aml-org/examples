import amf.MessageStyles;
import amf.ProfileName;
import amf.ProfileNames;
import amf.client.AMF;
import amf.client.environment.DefaultEnvironment;
import amf.client.environment.Environment;
import amf.client.model.document.BaseUnit;
import amf.client.parse.Raml10Parser;
import amf.client.validate.ValidationReport;
import aml_org.CustomResourceLoader;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.concurrent.ExecutionException;

import static org.junit.Assert.*;

public class ResourceLoaderTest {

    @BeforeClass
    public static void setup() throws ExecutionException, InterruptedException {
        AMF.init().get();
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
