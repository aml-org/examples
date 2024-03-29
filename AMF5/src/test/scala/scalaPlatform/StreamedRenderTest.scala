package scalaPlatform

import amf.apicontract.client.scala.OASConfiguration
import amf.core.client.scala.model.document.Document
import org.junit.runner.RunWith
import org.scalatest.flatspec.AsyncFlatSpec
import org.scalatest.matchers.must.Matchers.convertToAnyMustWrapper
import org.scalatest.matchers.should
import org.scalatestplus.junit.JUnitRunner
import org.yaml.builder.JsonOutputBuilder

import java.io.{File, FileOutputStream, FileWriter, OutputStreamWriter}
import java.util.Date
import scala.io.Source

@RunWith(classOf[JUnitRunner])
class StreamedRenderTest extends AsyncFlatSpec with should.Matchers with FileReader {

  "AMF" should "stream the rendering of Json-LD" in {
    val client = OASConfiguration.OAS20().baseUnitClient()
    val tmpFile = File.createTempFile("banking-api.json", new Date().getTime.toString)
    client.parse("file://src/test/resources/examples/banking-api.json") map { result =>
      result.baseUnit mustBe a[Document]
      val writer = new OutputStreamWriter(new FileOutputStream(tmpFile))
      val builder = JsonOutputBuilder(writer, prettyPrint = true)
      client.renderGraphToBuilder(result.baseUnit, builder)
      writer.flush()
      writer.close()
      val goldenContent = readResource("/examples/banking-api.flattened.jsonld")
      val writtenContent = using(Source.fromFile(tmpFile)) { source => source.mkString }
      goldenContent shouldEqual writtenContent
    }
  }
}
