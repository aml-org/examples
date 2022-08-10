package scalaPlatform

import amf.apicontract.client.scala.AMFBaseUnitClient
import amf.graphql.client.scala.GraphQLConfiguration
import amf.graphqlfederation.client.scala.GraphQLFederationConfiguration
import amf.graphqlfederation.internal.spec.transformation.GraphQLFederationIntrospectionPipeline
import org.junit.runner.RunWith
import org.scalatest.flatspec.AsyncFlatSpec
import org.scalatest.matchers.should.Matchers
import org.scalatestplus.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class GraphQLConversionTest extends AsyncFlatSpec with Matchers {

  private val origin_federation = "file://src/test/resources/examples/federation-origin.graphql"
  private val converted_no_federation = "src/test/resources/expected/federation-converted.jsonld"

  val graphQLFedClient: AMFBaseUnitClient =
    GraphQLFederationConfiguration.GraphQLFederation.baseUnitClient
  val graphQLClient: AMFBaseUnitClient = GraphQLConfiguration.GraphQL().baseUnitClient

  "AMF" should "convert GraphQLFederation to GraphQL" in {
    graphQLFedClient.parse(origin_federation) map { parseResult =>
      assert(parseResult.conforms)
      val transformResult = graphQLFedClient.transform(
        parseResult.baseUnit,
        GraphQLFederationIntrospectionPipeline.name
      )
      assert(transformResult.conforms)
      val renderedGraphQL = graphQLClient.render(transformResult.baseUnit, "application/ld+json")
      val goldenGraphQL = getStrFromFile(converted_no_federation)

      normalized(goldenGraphQL) shouldEqual(normalized(renderedGraphQL))
    }
  }

  private def getStrFromFile(path: String): String = {
    val source = scala.io.Source.fromFile(path)
    val read =
      try source.mkString
      finally source.close()
    read
  }

  def normalized(s: String): String = s.replaceAll("(?s)\\s+", " ").trim
}
