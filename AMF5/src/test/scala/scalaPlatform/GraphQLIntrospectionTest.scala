package scalaPlatform

import amf.apicontract.client.scala.AMFBaseUnitClient
import amf.core.client.common.transform.PipelineId
import amf.graphql.client.scala.GraphQLConfiguration
import amf.graphqlfederation.client.scala.GraphQLFederationConfiguration
import amf.graphqlfederation.internal.spec.transformation.GraphQLFederationIntrospectionPipeline
import org.junit.runner.RunWith
import org.scalatest.flatspec.AsyncFlatSpec
import org.scalatest.matchers.should.Matchers
import org.scalatestplus.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class GraphQLIntrospectionTest extends AsyncFlatSpec with Matchers {

  private val original = "file://src/test/resources/examples/federation-origin.graphql"
  private val introspected = "src/test/resources/expected/federation-introspected.graphql"

  val fedClient: AMFBaseUnitClient = GraphQLFederationConfiguration.GraphQLFederation().baseUnitClient
  val graphQLClient: AMFBaseUnitClient = GraphQLConfiguration.GraphQL().baseUnitClient

  "AMF" should "generate introspection schema from Federation API" in {
    fedClient.parse(original) map { parseResult =>
      assert(parseResult.conforms)

      val transformResult = fedClient.transform(
        parseResult.baseUnit,
        PipelineId.Introspection
      )

      assert(transformResult.conforms)

      val renderedGraphQL = graphQLClient.render(transformResult.baseUnit, "application/graphql")
      val goldenGraphQL = getStrFromFile(introspected)

      normalized(goldenGraphQL) shouldEqual (normalized(renderedGraphQL))
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
