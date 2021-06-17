import amf.aml.client.platform.AMLClient;
import amf.apicontract.client.platform.AMFClient;
import amf.apicontract.client.platform.RAMLConfiguration;
import amf.core.client.common.validation.ProfileName;
import amf.core.client.platform.model.document.BaseUnit;
import amf.core.client.platform.validation.AMFValidationReport;
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
        final AMFClient client = RAMLConfiguration.RAML10()
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
