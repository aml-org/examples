import {
  BaseUnit,
  Document,
  EndPoint,
  Operation,
  Parameter, ProvidedMediaType,
  RAMLConfiguration,
  Request,
  WebApi,
} from "amf-client-js";
import { expect } from "chai";
import * as fileSystem from "fs";

describe("Model Builder", () => {
  const STRING_DATATYPE = "http://www.w3.org/2001/XMLSchema#string";
  const APPLICATION_JSON = "application/json";
  const HTTP_STATUS_OK = "200";
  const RENDER_GOLDEN_FILE = "src/test/resources/examples/model-render.raml";
  const UTF8 = "utf8";

  it("builds the model and mutates the instance", async () => {
    const model: BaseUnit = buildModel();
    const client = RAMLConfiguration.RAML10().baseUnitClient();
    const report = await client.validate(model);
    expect(report.conforms).to.be.true;
    expect(report.results).to.be.empty;
  });

  it("renders built model", async () => {
    const golden = fileSystem.readFileSync(RENDER_GOLDEN_FILE, {
      encoding: UTF8,
    });
    const model: BaseUnit = buildModel();
    const client = RAMLConfiguration.RAML10().baseUnitClient();
    const rendered = await client.render(model, ProvidedMediaType.Raml10);
    expect(rendered).to.be.equal(golden, `Rendered: ${rendered} \nGolden: ${golden}`);
  });

  function buildModel(): BaseUnit {
    const api: WebApi = new WebApi()
      .withName("Music Service API")
      .withVersion("v1")
      .withContentType([APPLICATION_JSON])
      .withAccepts([APPLICATION_JSON]);

    const meEndpoint: EndPoint = api.withEndPoint("/me").withName("current-user");
    const meGet: Operation = meEndpoint
      .withOperation("get")
      .withDescription("Get Current User's Profile");
    meGet
      .withResponse(HTTP_STATUS_OK)
      .withPayload()
      .withMediaType(APPLICATION_JSON)
      .withScalarSchema("schema")
      .withDataType(STRING_DATATYPE);

    const playlistsEndpoint: EndPoint = api
      .withEndPoint("/me/playlists")
      .withName("current-user-playlists");
    const playlistsGet: Operation = playlistsEndpoint
      .withOperation("get")
      .withDescription("Get a List of Current User's Playlists");
    playlistsGet
      .withResponse(HTTP_STATUS_OK)
      .withPayload()
      .withMediaType(APPLICATION_JSON)
      .withScalarSchema("schema")
      .withDataType(STRING_DATATYPE);

    const albumByIdEndpoint: EndPoint = api.withEndPoint("/albums/{id}").withName("albums");
    albumByIdEndpoint
      .withParameter("id")
      .withDescription("The album ID")
      .withBinding("path")
      .withRequired(true)
      .withScalarSchema("music service Album ID")
      .withDataType(STRING_DATATYPE);

    const albumByMarketRequest: Request = new Request();
    const marketQueryParam: Parameter = albumByMarketRequest
      .withQueryParameter("market")
      .withRequired(false)
      .withName("Market")
      .withDescription(" The market (an ISO 3166-1 alpha-2 country code)");
    marketQueryParam.withBinding("query").withScalarSchema("Market").withDataType(STRING_DATATYPE);

    albumByIdEndpoint
      .withOperation("get")
      .withDescription("Get an Album")
      .withRequest(albumByMarketRequest)
      .withResponse(HTTP_STATUS_OK)
      .withPayload()
      .withMediaType(APPLICATION_JSON)
      .withScalarSchema("schema")
      .withDataType(STRING_DATATYPE);

    const albumEndpoint: EndPoint = api.withEndPoint("/albums").withName("several-albums");
    const albumRequest: Request = new Request();
    albumRequest
      .withQueryParameter("ids")
      .withBinding("query")
      .withRequired(true)
      .withName("album-ids")
      .withDescription("A comma-separated list of IDs")
      .withScalarSchema("displayName")
      .withDataType(STRING_DATATYPE);
    albumEndpoint
      .withOperation("get")
      .withDescription("Get Several Albums")
      .withRequest(albumRequest)
      .withResponse("200")
      .withPayload()
      .withMediaType("application/json")
      .withScalarSchema("schema")
      .withDataType(STRING_DATATYPE);

    expect(api.endPoints.length == 4);

    const document: Document = new Document();
    return document.withEncodes(api);
  }
});
