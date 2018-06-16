var amf = require("amf-client-js");
var AMF = amf.AMF;
var Aml10Parser = amf.Aml10Parser;

//#parsing-example
AMF.init()
    .then(function() {
        return AMF.registerDialect("file://aml/music/dialect/playlist.yaml")
    })
    .then(function() {
        new Aml10Parser()
            .parseFileAsync("file://aml/music/playlist1.yaml")
            .then(displayEncodedElement)
    });

function displayEncodedElement(model) {
    console.log("Encoded element:");
    console.log("  " + model.encodes.id);
    console.log("Class terms for element:");
    model.encodes.getTypeUris().forEach(function(type) {
        console.log(" - " + type);
    });
}