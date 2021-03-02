import amf.MessageStyles;
import amf.ProfileName;
import amf.ProfileNames;
import amf.client.AMF;
import amf.client.model.document.BaseUnit;
import amf.client.parse.Raml10Parser;
import amf.client.validate.ValidationReport;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.concurrent.ExecutionException;

import static org.junit.Assert.*;

public class ValidationTest {

    @BeforeClass
    public static void setup() throws ExecutionException, InterruptedException {
        AMF.init().get();
    }


    @Test
    public void validateRaml() throws ExecutionException, InterruptedException {
        final Raml10Parser parser = new Raml10Parser();

        final BaseUnit model = parser.parseFileAsync("file://resources/examples/banking-api-error.raml").get();
        assertNotNull(model);

        // Run RAML default validations on parsed unit (expects one error -> invalid protocols value)
        final ValidationReport report = AMF.validate(model, ProfileNames.RAML(), MessageStyles.RAML()).get();
        System.out.println("report.conforms() == " + report.conforms());
        assertFalse(report.conforms());
    }

    @Test
    public void validateRamlWithCustomValidation() throws ExecutionException, InterruptedException {
        final Raml10Parser parser = new Raml10Parser();

        final BaseUnit model = parser.parseFileAsync("file://resources/examples/banking-api-error.raml").get();
        assertNotNull(model);

        // Run RAML custom validations with a validation profile that accepts the previously invalid protocol value
        final ProfileName customProfile = AMF.loadValidationProfile("file://resources/validation_profile.raml").get();

        final ValidationReport report = AMF.validate(model, customProfile, MessageStyles.RAML()).get();
        System.out.println("report.conforms() == " + report.conforms());
        assertTrue(report.conforms());
    }
}
