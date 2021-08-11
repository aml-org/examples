package scalaPlatform

import amf.apicontract.client.scala.OASConfiguration
import amf.core.client.scala.model.document.Document
import amf.rdf.client.scala.RdfUnitConverter
import org.scalatest.flatspec.AsyncFlatSpec
import org.scalatest.matchers.should
import scalaPlatform.TestUtils.{sortingLines, strippedOfWhitespace}

class RdfTest extends AsyncFlatSpec with should.Matchers with FileReader {

  "AMF" should "convert BaseUnit to an RdfModel" in {
    val client = OASConfiguration.OAS20().baseUnitClient()
    client.parse("file://src/test/resources/examples/banking-api.json") map { result =>
      result.baseUnit shouldBe a[Document]
      val rdfModel = RdfUnitConverter.toNativeRdfModel(result.baseUnit)
      val expectedRdf = readResource("/examples/banking-api.nt")
      rdfModel.toN3() should equal(expectedRdf) (after being sortingLines and strippedOfWhitespace)
    }
  }

  "AMF" can "round trip from and to RDF" in {
    val client = OASConfiguration.OAS20().baseUnitClient()
    client.parse("file://src/test/resources/examples/banking-api.json") map { result =>
      result.baseUnit shouldBe a[Document]
      val rdfModel = RdfUnitConverter.toNativeRdfModel(result.baseUnit)
      val roundTripBaseUnit = RdfUnitConverter.fromNativeRdfModel(result.baseUnit.id, rdfModel, OASConfiguration.OAS20())
      val renderedBaseUnit = client.render(roundTripBaseUnit)
      val expectedRender = readResource("/examples/banking-api-rdf-cycle.json")
      renderedBaseUnit should equal(expectedRender)
    }
  }
}
