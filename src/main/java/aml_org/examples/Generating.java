package aml_org.examples;

import amf.client.AMF;
import amf.client.model.document.BaseUnit;
import amf.client.parse.AmfGraphParser;
import amf.client.render.Aml10Renderer;

import java.util.concurrent.ExecutionException;

import static java.lang.System.out;

public class Generating {

    //#generation-example
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        AMF.init().get();

        AMF.registerDialect("file://aml/music/dialect/playlist.yaml").get();

        BaseUnit playlist = new AmfGraphParser().parseFileAsync("file://aml/music/playlist1.json").get();

        String amlDocument = new Aml10Renderer().generateString(playlist).get();

        out.println(amlDocument);
    }
}
