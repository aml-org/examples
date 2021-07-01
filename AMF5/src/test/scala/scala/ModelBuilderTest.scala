package scala

import amf.apicontract.client.scala.RAMLConfiguration
import amf.apicontract.client.scala.model.domain.Request
import amf.apicontract.client.scala.model.domain.api.WebApi
import amf.core.client.scala.model.document.Document
import org.junit.Assert.assertTrue
import org.scalatest.flatspec.AsyncFlatSpec
import org.scalatest.matchers.should

class ModelBuilderTest extends AsyncFlatSpec with should.Matchers {

  "AMF" should "create a valid model in memory" in {
    val stringDataType = "http://www.w3.org/2001/XMLSchema#string"
    val client = RAMLConfiguration.RAML().createClient()
    val api = WebApi()
      .withName("Music Service API")
      .withVersion("v1")
      .withContentType(List("application/json"))
      .withAccepts(List("application/json"))

    // create EndPoint /me
    val me = api.withEndPoint("/me").withName("current-user")
    // add get operation
    val meGet =
      me.withOperation("get").withDescription("Get Current User's Profile")
    // add a 200 response of type string to the get operation
    meGet
      .withResponse("200")
      .withPayload()
      .withMediaType("application/json")
      .withScalarSchema("schema")
      .withDataType(stringDataType)

    // create EndPoint /me/playlists
    val playlists =
      api.withEndPoint("/me/playlists").withName("current-user-playlists")
    val playlistsGet = playlists
      .withOperation("get")
      .withDescription("Get a List of Current User's Playlists")
    playlistsGet
      .withResponse("200")
      .withPayload()
      .withMediaType("application/json")
      .withScalarSchema("schema")
      .withDataType(stringDataType)

    // create EndPoint /albums/{id}
    val album = api.withEndPoint("/albums/{id}").withName("albums")
    // add {id} parameter to albums endpoint
    album
      .withParameter("id")
      .withDescription("The album ID")
      .withBinding("path")
      .withRequired(true)
      .withScalarSchema("music service Album ID")
      .withDataType(stringDataType)
    // create a request and a query parameter to add to the get operation
    val marketRequest = Request()
    val marketQueryParam = marketRequest
      .withQueryParameter("market")
      .withRequired(false)
      .withName("Market")
      .withDescription(" The market (an ISO 3166-1 alpha-2 country code)")
    marketQueryParam
      .withBinding("query")
      .withScalarSchema("Market")
      .withDataType(stringDataType)
    // add get operation to albums/{id} endpoint
    album
      .withOperation("get")
      .withDescription("Get an Album")
      .withRequest(marketRequest)
      .withResponse("200")
      .withPayload()
      .withMediaType("application/json")
      .withScalarSchema("schema")
      .withDataType(stringDataType)

    // create EndPoint /albums with a query parameter of a list of album IDs and a string response
    val albums = api.withEndPoint("/albums").withName("several-albums")
    val albumsRequest = Request()
    albumsRequest
      .withQueryParameter("ids")
      .withBinding("query")
      .withRequired(true)
      .withName("album-ids")
      .withDescription("A comma-separated list of IDs")
      .withScalarSchema("displayName")
      .withDataType(stringDataType)
    albums
      .withOperation("get")
      .withDescription("Get Several Albums")
      .withRequest(albumsRequest)
      .withResponse("200")
      .withPayload()
      .withMediaType("application/json")
      .withScalarSchema("schema")
      .withDataType(stringDataType)

    assertTrue(api.endPoints.length == 4)

    val model = Document()
    model.withEncodes(api)

    // Run RAML default validations on parsed unit (expects one error -> invalid protocols value)
    client.validate(model).map { validationReport =>
      validationReport.conforms shouldBe true
      validationReport.results.length shouldBe 0
    }
  }
}
