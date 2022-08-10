package scalaPlatform

import amf.core.client.scala.model.document.Document
import amf.core.client.scala.model.domain.Shape
import amf.graphql.client.scala.GraphQLConfiguration
import amf.graphqlfederation.client.scala.GraphQLFederationConfiguration
import amf.shapes.client.scala.config.JsonSchemaConfiguration
import amf.shapes.client.scala.model.document.JsonSchemaDocument
import org.junit.Assert.{assertEquals, assertNotNull, assertSame, assertTrue}
import org.junit.runner.RunWith
import org.scalatest.flatspec.AsyncFlatSpec
import org.scalatest.matchers.must.Matchers.convertToAnyMustWrapper
import org.scalatest.matchers.should
import org.scalatestplus.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class GraphQLParsingTest extends AsyncFlatSpec with should.Matchers {

  it should "parse GraphQL file" in {
    val config = GraphQLConfiguration.GraphQL()
    val client = config.baseUnitClient()

    // A BaseUnit is the return type of any parsing
    // The actual object can be many things, depending on the content of the source file
    // https://github.com/aml-org/amf/blob/develop/documentation/model.md#baseunit
    client.parse("file://src/test/resources/examples/simple.graphql") map { result =>

      assertNotNull(result.baseUnit)
      val model = result.baseUnit

      // In this case the BaseUnit is a Document
      assertSame(model.getClass, classOf[Document])

      val document = model.asInstanceOf[Document]

      // DomainElement is the base class for any element describing a domain model
      val graphQLApi = document.encodes
      assertNotNull(graphQLApi)

      result.conforms shouldBe true
    }
  }

  it should "parse GraphQL Federation file" in {
    val config = GraphQLFederationConfiguration.GraphQLFederation()
    val client = config.baseUnitClient()

    // A BaseUnit is the return type of any parsing
    // The actual object can be many things, depending on the content of the source file
    // https://github.com/aml-org/amf/blob/develop/documentation/model.md#baseunit
    client.parse("file://src/test/resources/examples/simple-federation.graphql") map { result =>

      assertNotNull(result.baseUnit)
      val model = result.baseUnit

      // In this case the BaseUnit is a Document
      assertSame(model.getClass, classOf[Document])

      val document = model.asInstanceOf[Document]

      // DomainElement is the base class for any element describing a domain model
      val graphQLFederationApi = document.encodes
      assertNotNull(graphQLFederationApi)

      result.conforms shouldBe true
    }
  }
}
