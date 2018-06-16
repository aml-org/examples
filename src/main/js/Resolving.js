var amf = require("amf-client-js");
var AMF = amf.AMF;
var Resolver = amf.Resolver;
var Aml10Parser = amf.Aml10Parser;
var AmfGraphRenderer = amf.AmfGraphRenderer;

//#resolving-example
AMF.init()
    .then(function() {
        return AMF.registerDialect("file://aml/music/dialect/playlist.yaml");
    })
    .then(function() {
        return new Aml10Parser()
            .parseFileAsync("file://aml/music/playlist1.yaml");
    })
    .then(function(unit) {
        var resolved = new Resolver("AML 1.0").resolve(unit);
        return new AmfGraphRenderer().generateString(resolved);
    })
    .then(function(jsonld) {
        console.log(jsonld)
    });