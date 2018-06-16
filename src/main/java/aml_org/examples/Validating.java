package aml_org.examples;

import amf.client.AMF;
import amf.client.model.document.DialectInstance;
import amf.client.parse.Aml10Parser;
import amf.client.validate.ValidationReport;

import java.util.concurrent.ExecutionException;

import static java.lang.System.out;

public class Validating {

    //#validation-example
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        AMF.init().get();

        AMF.registerDialect("file://aml/music/dialect/playlist.yaml").get();

        DialectInstance model = (DialectInstance) new Aml10Parser().parseFileAsync("file://aml/music/playlist1.yaml").get();

        ValidationReport report = AMF.validate(model, "Playlist 1.0", "AMF").get();

        out.println("Validates?");
        out.println(report.conforms());
    }
}
