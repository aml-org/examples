package javaPlatform;

import amf.apicontract.client.platform.AMFConfiguration;
import amf.apicontract.client.platform.APIConfiguration;
import amf.core.client.common.validation.ProfileNames;
import amf.core.client.platform.AMFParseResult;
import amf.core.client.platform.validation.AMFValidationReport;
import org.junit.Test;

import java.util.concurrent.ExecutionException;

public class UnknownConfigCreation {

    @Test
    public void createConfigFromParsedApiResult() throws ExecutionException, InterruptedException {
        AMFConfiguration guessingConfig = APIConfiguration.API();
        AMFParseResult result = guessingConfig.baseUnitClient().parse("file://src/test/resources/examples/banking-api-error.raml").get();
        AMFConfiguration configForApi = APIConfiguration.fromSpec(result.sourceSpec());
        AMFValidationReport report = configForApi.baseUnitClient().validate(result.baseUnit()).get();
        assert(!report.conforms());
        assert(report.profile().equals(ProfileNames.RAML10()));
        assert(report.results().get(0).message().equals("Protocols must have a case insensitive value matching http or https"));
    }
}
