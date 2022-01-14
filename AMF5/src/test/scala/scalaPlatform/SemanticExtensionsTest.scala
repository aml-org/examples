package scalaPlatform

import amf.apicontract.client.scala.OASConfiguration
import amf.apicontract.client.scala.model.domain.api.{Api, WebApi}
import amf.core.client.common.transform.PipelineId
import amf.core.client.scala.model.document.Document
import org.scalatest.flatspec.AsyncFlatSpec
import org.scalatest.matchers.should

import scala.concurrent.Future

class SemanticExtensionsTest extends AsyncFlatSpec with should.Matchers {

  val EXTENSION_DIALECT = "file://src/test/resources/examples/semantic/extensions.yaml"
  val VALID_EXTENDED_API_SPEC = "file://src/test/resources/examples/semantic/api.oas30.yaml"
  val INVALID_EXTENDED_API_SPEC = "file://src/test/resources/examples/semantic/invalid-api.oas30.yaml"

  "Applied semantic extension" should "accessible in Endpoint and Response objects" in {
    for {
      // Register my extensions into my configuration
      client <- OASConfiguration.OAS30().withDialect(EXTENSION_DIALECT).map(_.baseUnitClient())
      // Parse and transform the API
      parsed <- client.parseDocument(VALID_EXTENDED_API_SPEC)
      // Default, Editing or Cache pipeline can be used
      transformed <- Future.successful(client.transform(parsed.document, PipelineId.Editing))
    } yield {
      parsed.conforms shouldBe true
      transformed.conforms shouldBe true
      val unit = transformed.baseUnit
      val api = unit.asInstanceOf[Document].encodes.asInstanceOf[Api]

      // Check on the "deprecated" extension
      // Extension access is done via the "graph" access. The "graph" method exposes the underlying graph.

      val endpointIsDeprecated = api.endPoints.head.graph.containsProperty("http://a.ml/vocabularies/apiContract#deprecated")
      endpointIsDeprecated shouldBe true

      /*
        * Extension property access is done the same as in AML
        * As the 'replaceFor' property doens't have a property term, the base of its uri is ""http://a.ml/vocabularies/data#""
        */
      val deprecatedExtension = api.endPoints.head.graph.getObjectByProperty("http://a.ml/vocabularies/apiContract#deprecated").head
      val replaceForValue = deprecatedExtension.graph.scalarByProperty("http://a.ml/vocabularies/data#replaceFor").head
      replaceForValue shouldEqual "v2/paginated"

      // Check for the page-size extension in the second endpoint
      val pageSize = api.endPoints(1)
        .operations.find(_.method.value() == "get").get
        .responses.head
        .graph.scalarByProperty("http://a.ml/vocabularies/apiContract#pageSize").head
      pageSize shouldEqual 35
    }
  }

  "Invalid semantic extension" should "be validated and show up in report" in {
    for {
      client <- OASConfiguration.OAS30().withDialect(EXTENSION_DIALECT).map(_.baseUnitClient())
      // Parse and validate the API (this API is valid)
      validParsed <- client.parseDocument(VALID_EXTENDED_API_SPEC)
      validReport <- client.validate(validParsed.baseUnit)
      // Parse and validate the API (this API is NOT valid)
      invalid <- client.parseDocument(INVALID_EXTENDED_API_SPEC)
      report <- client.validate(invalid.baseUnit)
    } yield {
      validParsed.conforms shouldBe true
      validReport.conforms shouldBe true

      // Parsing conforms as the API doesn't have syntax errors
      invalid.conforms shouldBe true

      // One validation error for each invalid aspect of each extension. There is one for each.
      report.conforms should not be(true)
      report.results should have size(2)
    }
  }
}
