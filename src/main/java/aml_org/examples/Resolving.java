package aml_org.examples;

import amf.client.AMF;
import amf.client.model.document.DialectInstance;
import amf.client.parse.Aml10Parser;
import amf.client.render.AmfGraphRenderer;
import amf.client.resolve.Resolver;

import java.util.concurrent.ExecutionException;

import static java.lang.System.out;

public class Resolving {

    //#resolving-example
    public static void main(String[] args) throws ExecutionException, InterruptedException {

        AMF.init().get();

        AMF.registerDialect("file://aml/music/dialect/playlist.yaml").get();

        DialectInstance model = (DialectInstance) new Aml10Parser().parseFileAsync("file://aml/music/playlist1.yaml").get();

        DialectInstance resolvedModel = (DialectInstance) new Resolver("AML 1.0").resolve(model);

        String parsedJsonLd = new AmfGraphRenderer().generateString(resolvedModel).get();

        out.println(parsedJsonLd);

    }
}
