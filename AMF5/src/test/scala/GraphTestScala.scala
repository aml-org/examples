import amf.apicontract.client.scala.WebAPIConfiguration
import amf.core.client.scala.model.document.Document
import org.scalatest.flatspec.AsyncFlatSpec
import org.scalatest.matchers.must.Matchers.convertToAnyMustWrapper
import org.scalatest.matchers.should

class GraphTestScala extends AsyncFlatSpec with should.Matchers {

  "AMF Graph operations" should "parse" in {
    val client = WebAPIConfiguration.WebAPI().createClient()
    client.parse(
      "file://resources/examples/banking-api.jsonld"
    ) map { result =>
      result.bu mustBe a[Document]
      result.conforms shouldBe true
    }
  }
}
