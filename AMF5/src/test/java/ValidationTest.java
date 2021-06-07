import amf.MessageStyles;
import amf.ProfileName;
import amf.ProfileNames;
import amf.client.exported.AMFClient;
import amf.client.exported.AMLClient;
import amf.client.exported.RAMLConfiguration;
import amf.client.model.document.BaseUnit;
import amf.client.validate.AMFValidationReport;
import org.junit.Test;

import java.util.concurrent.ExecutionException;

import static org.junit.Assert.*;

public class ValidationTest {

    @Test
    public void validateRaml() throws ExecutionException, InterruptedException {
        final AMFClient client = RAMLConfiguration.RAML10().createClient();

        final BaseUnit model = client.parse("file://resources/examples/banking-api-error.raml").get().baseUnit();
        assertNotNull(model);

        // Run RAML default validations on parsed unit (expects one error -> invalid protocols value)
        final AMFValidationReport report = client.validate(model).get();
        System.out.println("report.conforms() == " + report.conforms());
        assertFalse(report.conforms());
    }

    @Test
    public void validateRamlWithCustomValidation() throws ExecutionException, InterruptedException {

        // Run RAML custom validations with a validation profile that accepts the previously invalid protocol value
        final AMLClient client = RAMLConfiguration.RAML10()
                .withCustomValidationsEnabled().get()
                .withCustomProfile("file://resources/validation_profile.raml").get()
                .createClient();

        final BaseUnit model = client.parse("file://resources/examples/banking-api-error.raml").get().baseUnit();
        assertNotNull(model);

        // TODO interface should be adjusted to obtain profile name after parsing custom profile.
        final AMFValidationReport report = client.validate(model, new ProfileName("Banking")).get();
        System.out.println("report.conforms() == " + report.conforms());
        assertTrue(report.conforms());
    }
}
