package javaPlatform;

import amf.apicontract.client.platform.AMFBaseUnitClient;
import amf.apicontract.client.platform.OASConfiguration;
import amf.core.client.platform.AMFResult;
import amf.core.client.platform.config.RenderOptions;
import amf.core.client.platform.model.document.BaseUnit;
import amf.rdf.client.platform.RdfModel;
import amf.rdf.client.platform.RdfUnitConverter;
import org.junit.Test;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

import static javaPlatform.StringEquals.*;

public class RdfTest implements FileReader {

    @Test
    public void convertBaseUnitToRdfModel() throws IOException, ExecutionException, InterruptedException {
        final AMFBaseUnitClient client = OASConfiguration.OAS20().baseUnitClient();
        final AMFResult parseResult = client.parse("file://src/test/resources/examples/banking-api.json").get();
        final BaseUnit model = parseResult.baseUnit();
        final RdfModel rdfModel = RdfUnitConverter.toNativeRdfModel(model, new RenderOptions());
        final String n3Representation = rdfModel.toN3();
        final String expectedN3 = readResource("/examples/banking-api.nt");
        assertIgnoringWhitespaceAndSortingLines(n3Representation, expectedN3);
    }

    @Test
    public void baseUnitRoundTripFromRdfModel() throws IOException, ExecutionException, InterruptedException {
        final AMFBaseUnitClient client = OASConfiguration.OAS20().baseUnitClient();
        final AMFResult parseResult = client.parse("file://src/test/resources/examples/banking-api.json").get();
        final BaseUnit model = parseResult.baseUnit();
        final RdfModel rdfModel = RdfUnitConverter.toNativeRdfModel(model, new RenderOptions());
        final BaseUnit roundTripBaseUnit = RdfUnitConverter.fromNativeRdfModel(model.id(), rdfModel, OASConfiguration.OAS20());
        final String renderedRoundTripUnit = client.render(roundTripBaseUnit);
        final String expectedApi = readResource("/examples/banking-api-rdf-cycle.json");
        assertIgnoringWhitespace(renderedRoundTripUnit, expectedApi);
    }
}
