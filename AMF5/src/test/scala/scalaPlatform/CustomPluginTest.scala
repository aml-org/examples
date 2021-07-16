package scalaPlatform

import amf.apicontract.client.scala.WebAPIConfiguration
import amf.apicontract.client.scala.model.domain.api.WebApi
import amf.core.client.common.{HighPriority, PluginPriority}
import amf.core.client.scala.errorhandling.AMFErrorHandler
import amf.core.client.scala.model.document.{BaseUnit, Document}
import amf.core.client.scala.parse.AMFParsePlugin
import amf.core.client.scala.parse.document.{ParserContext, ReferenceHandler, SimpleReferenceHandler}
import amf.core.internal.parser.Root
import org.scalatest.flatspec.AsyncFlatSpec
import org.scalatest.matchers.should

class CustomPluginTest extends AsyncFlatSpec with should.Matchers {

  private val CUSTOM_MEDIATYPE = "application/custom+syntax"
  private val documentToParseInJson =
    """{
      |    "mySpec": "5.0.0",
      |    "templated": {
      |
      |    }
      |}""".stripMargin

  "AMF" should "let me define a custom parse plugin and use it" in {
    val expectedWebApiName = "CoolWebAPI"
    val client = WebAPIConfiguration.WebAPI()
      .withPlugin(new MyCustomPlugin(expectedWebApiName))
      .baseUnitClient()
    client.parseContent(documentToParseInJson).map { contentResult =>
      contentResult.conforms shouldBe true
      contentResult.baseUnit
        .asInstanceOf[Document]
        .encodes
        .asInstanceOf[WebApi]
        .name.value() shouldEqual expectedWebApiName
    }
  }

  class MyCustomPlugin(webApiName: String) extends AMFParsePlugin {
    override def parse(document: Root, ctx: ParserContext): BaseUnit = {
      val encoded = WebApi().withId("myWebApi").withName(webApiName)

      Document()
        .withId("myDocument")
        .withEncodes(encoded)
        .withRaw(document.raw)

    }

    override def mediaTypes: Seq[String] = Seq(CUSTOM_MEDIATYPE)

    override def validMediaTypesToReference: Seq[String] = Seq.empty

    override def referenceHandler(eh: AMFErrorHandler): ReferenceHandler = SimpleReferenceHandler

    override def allowRecursiveReferences: Boolean = true

    override val id: String = "CUSTOM_PLUGIN"

    override def applies(element: Root): Boolean = true

    override def priority: PluginPriority = HighPriority
  }
}
