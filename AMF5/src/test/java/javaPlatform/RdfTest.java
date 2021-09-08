package javaPlatform;

import amf.aml.client.platform.AMLBaseUnitClient;
import amf.aml.client.platform.AMLConfiguration;
import amf.aml.client.platform.model.document.DialectInstance;
import amf.apicontract.client.platform.AMFBaseUnitClient;
import amf.apicontract.client.platform.OASConfiguration;
import amf.core.client.platform.AMFResult;
import amf.core.client.platform.config.RenderOptions;
import amf.core.client.platform.model.document.BaseUnit;
import amf.rdf.client.platform.RdfModel;
import amf.rdf.client.platform.RdfUnitConverter;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

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

    @Test
    public void editRdfModelAndRoundTrip() throws IOException, ExecutionException, InterruptedException {
        // Setup config & client
        final AMLConfiguration configuration = AMLConfiguration.predefined().withDialect("file://src/test/resources/examples/dialect2.yaml").get();
        final AMLBaseUnitClient client = configuration.baseUnitClient();

        // Parse instance 2a
        final DialectInstance instance2a = client.parseDialectInstance("file://src/test/resources/examples/dialect-instance2a.yaml").get().dialectInstance();

        // Generate RDF model
        final RdfModel instance2aRdfModel = RdfUnitConverter.toNativeRdfModel(instance2a, new RenderOptions());

        // Edit RDF model (add property)
        final String subject = "file://src/test/resources/examples/dialect-instance2a.yaml#/encodes";
        final String predicate = "http://a.ml/vocabularies/data#b";
        final String object = "5";
        instance2aRdfModel.addLiteralTriple(subject, predicate, object);

        // Return to BaseUnit
        final DialectInstance instance2aRoundTrip = (DialectInstance) RdfUnitConverter.fromNativeRdfModel(instance2a.id(), instance2aRdfModel, configuration);

        // Assert equality between instance 2a and 2b
        final String instance2aRoundTripRender = client.render(instance2aRoundTrip);
        final String instance2b = readResource("/examples/dialect-instance2b.yaml");
        assertIgnoringWhitespace(instance2aRoundTripRender, instance2b);
    }
}
