package javaPlatform;

import amf.apicontract.client.platform.AMFBaseUnitClient;
import amf.apicontract.client.platform.RAMLConfiguration;
import amf.apicontract.client.platform.model.domain.EndPoint;
import amf.apicontract.client.platform.model.domain.Operation;
import amf.apicontract.client.platform.model.domain.Parameter;
import amf.apicontract.client.platform.model.domain.Request;
import amf.apicontract.client.platform.model.domain.api.WebApi;
import amf.core.client.common.validation.ProfileNames;
import amf.core.client.platform.model.document.Document;
import amf.core.client.platform.validation.AMFValidationReport;
import org.junit.Test;

import java.util.Collections;
import java.util.concurrent.ExecutionException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertTrue;

public class ModelBuilderTest {

    @Test
    public void buildWebApiTest() throws ExecutionException, InterruptedException {
        final AMFBaseUnitClient client = RAMLConfiguration.RAML10().baseUnitClient();

        final String stringDataType = "http://www.w3.org/2001/XMLSchema#string";

        final WebApi api = new WebApi()
                .withName("Music Service API")
                .withVersion("v1")
                .withContentType(Collections.singletonList("application/json"))
                .withAccepts(Collections.singletonList("application/json"));

        // create EndPoint /me
        final EndPoint me = api.withEndPoint("/me").withName("current-user");
        // add get operation
        final Operation meGet = me.withOperation("get").withDescription("Get Current User's Profile");
        // add a 200 response of type string to the get operation
        meGet.withResponse("200")
                .withPayload()
                .withMediaType("application/json")
                .withScalarSchema("schema")
                .withDataType(stringDataType);


        // create EndPoint /me/playlists
        final EndPoint playlists = api.withEndPoint("/me/playlists").withName("current-user-playlists");
        final Operation playlistsGet = playlists.withOperation("get")
                .withDescription("Get a List of Current User's Playlists");
        playlistsGet.withResponse("200")
                .withPayload()
                .withMediaType("application/json")
                .withScalarSchema("schema")
                .withDataType(stringDataType);


        // create EndPoint /albums/{id}
        final EndPoint album = api.withEndPoint("/albums/{id}").withName("albums");
        // add {id} parameter to albums endpoint
        album.withParameter("id")
                .withDescription("The album ID")
                .withBinding("path")
                .withRequired(true)
                .withScalarSchema("music service Album ID")
                .withDataType(stringDataType);
        // create a request and a query parameter to add to the get operation
        final Request marketRequest = new Request();
        final Parameter marketQueryParam = marketRequest.withQueryParameter("market")
                .withRequired(false)
                .withName("Market")
                .withDescription(" The market (an ISO 3166-1 alpha-2 country code)");
        marketQueryParam
                .withBinding("query")
                .withScalarSchema("Market")
                .withDataType(stringDataType);
        // add get operation to albums/{id} endpoint
        album.withOperation("get")
                .withDescription("Get an Album")
                .withRequest(marketRequest)
                .withResponse("200")
                .withPayload()
                .withMediaType("application/json")
                .withScalarSchema("schema")
                .withDataType(stringDataType);


        // create EndPoint /albums with a query parameter of a list of album IDs and a string response
        final EndPoint albums = api.withEndPoint("/albums").withName("several-albums");
        final Request albumsRequest = new Request();
        albumsRequest.withQueryParameter("ids")
                .withBinding("query")
                .withRequired(true)
                .withName("album-ids")
                .withDescription("A comma-separated list of IDs")
                .withScalarSchema("displayName")
                .withDataType(stringDataType);
        albums.withOperation("get")
                .withDescription("Get Several Albums")
                .withRequest(albumsRequest)
                .withResponse("200")
                .withPayload()
                .withMediaType("application/json")
                .withScalarSchema("schema")
                .withDataType(stringDataType);

        assertThat(api.endPoints()).hasSize(4);

        final Document model = new Document();
        model.withEncodes(api);

        // Run RAML default validations on parsed unit (expects one error -> invalid protocols value)

        final AMFValidationReport report = client.validate(model).get();
        assertTrue(report.conforms());
    }
}
