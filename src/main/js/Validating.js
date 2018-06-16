var amf = require("amf-client-js");
var AMF = amf.AMF;
var Aml10Parser = amf.Aml10Parser;

//#validation-example
AMF.init()
    .then(function() {
        return AMF.registerDialect("file://aml/music/dialect/playlist.yaml")
    })
    .then(function() {
        return new Aml10Parser()
            .parseFileAsync("file://aml/music/playlist1.yaml")
    })
    .then(function(unit) {
        return AMF.validate(unit, "Playlist 1.0", "AMF")
    })
    .then(function(report) {
        console.log("Validates?");
        console.log(report.conforms);
    });