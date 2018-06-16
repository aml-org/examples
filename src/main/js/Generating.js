var amf = require("amf-client-js");
var AMF = amf.AMF;
var Aml10Renderer = amf.Aml10Renderer;
var AmfGraphParser = amf.AmfGraphParser;

//#generation-example
AMF.init()
    .then(function() {
        return AMF.registerDialect("file://aml/music/dialect/playlist.yaml")
    })
    .then(function() {
        return new AmfGraphParser()
            .parseFileAsync("file://aml/music/playlist1.json");
    })
    .then(function(unit) {
        return new Aml10Renderer().generateString(unit);

    })
    .then(function(amlDocument) {
        console.log(amlDocument);
    });