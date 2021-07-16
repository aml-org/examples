package javaPlatform;

import amf.apicontract.client.platform.AMFBaseUnitClient;
import amf.apicontract.client.platform.RAMLConfiguration;
import amf.core.client.platform.model.document.BaseUnit;
import amf.core.client.platform.validation.AMFValidationReport;
import org.junit.Test;

import java.util.concurrent.ExecutionException;

import static org.junit.Assert.*;

public class ValidationTest {

    @Test
    public void validateRaml() throws ExecutionException, InterruptedException {
        final AMFBaseUnitClient client = RAMLConfiguration.RAML10().baseUnitClient();

        final BaseUnit model = client.parse("file://src/test/resources/examples/banking-api-error.raml").get().baseUnit();
        assertNotNull(model);

        // Run RAML default validations on parsed unit (expects one error -> invalid protocols value)
        final AMFValidationReport report = client.validate(model).get();
        System.out.println("report.conforms() == " + report.conforms());
        assertFalse(report.conforms());
    }
}
