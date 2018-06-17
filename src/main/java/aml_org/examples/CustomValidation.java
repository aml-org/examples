package aml_org.examples;

import amf.client.AMF;
import amf.client.model.document.DialectInstance;
import amf.client.parse.Aml10Parser;
import amf.client.validate.ValidationReport;
import amf.client.validate.ValidationResult;

import java.util.concurrent.ExecutionException;

import static java.lang.System.out;

public class CustomValidation {

    //#custom-validation-example
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        AMF.init().get();

        AMF.registerDialect("file://aml/music/dialect/playlist.yaml").get();

        DialectInstance model = (DialectInstance) new Aml10Parser().parseFileAsync("file://aml/music/playlist1.yaml").get();

        AMF.loadValidationProfile("file://aml/music/custom_validations/boring.yaml").get();

        ValidationReport report = AMF.validate(model, "Boring Playlists", "AMF").get();

        out.println("Validates? " + report.conforms());
        if (!report.conforms()) {
            out.println("Errors:");
            for (ValidationResult result : report.results()) {
                out.println(" - " + result.message() + " => " + result.targetNode());
            }
        }
    }
}
