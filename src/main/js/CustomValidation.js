var amf = require("amf-client-js");
var AMF = amf.AMF;
var Aml10Parser = amf.Aml10Parser;

//#custom-validation-example
AMF.init()
    .then(function() {
        return AMF.registerDialect("file://aml/music/dialect/playlist.yaml");
    })
    .then(function() {
        return AMF.loadValidationProfile("file://aml/music/custom_validations/boring.yaml");
    })
    .then(function() {
        return new Aml10Parser()
            .parseFileAsync("file://aml/music/playlist1.yaml");
    })
    .then(function(unit) {
        return AMF.validate(unit, "Boring Playlists", "AMF");
    })
    .then(function(report) {
        console.log("Validates? " + report.conforms);
        if (!report.conforms) {
            out.println("Errors:");
            report.results.forEach(function (result) {
                console.log(" - " + result.message + " => " + result.targetNode);
            });
        }
    });