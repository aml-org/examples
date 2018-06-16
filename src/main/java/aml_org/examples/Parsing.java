package aml_org.examples;

import amf.client.AMF;
import amf.client.model.document.DialectInstance;
import amf.client.parse.Aml10Parser;

import java.util.concurrent.ExecutionException;

import static java.lang.System.out;

public class Parsing {

    //#parsing-example
    public static void main(String[] args) throws ExecutionException, InterruptedException {

        AMF.init().get();

        AMF.registerDialect("file://aml/music/dialect/playlist.yaml").get();

        DialectInstance model = (DialectInstance) new Aml10Parser().parseFileAsync("file://aml/music/playlist1.yaml").get();

        displayEncodedElement(model);
    }

    static void displayEncodedElement(DialectInstance model) {
        out.println("Encoded element:");
        out.println("  " + model.encodes().id());
        out.println("Class terms for element:");
        for (String type : model.encodes().getTypeUris()) {
            out.println(" - " + type);
        }
    }

}
