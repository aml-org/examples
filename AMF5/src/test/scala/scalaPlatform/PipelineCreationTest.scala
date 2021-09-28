package scalaPlatform

import amf.apicontract.client.scala.WebAPIConfiguration
import amf.apicontract.client.scala.model.domain.api.WebApi
import amf.core.client.scala.AMFGraphConfiguration
import amf.core.client.scala.errorhandling.AMFErrorHandler
import amf.core.client.scala.model.document.{BaseUnit, Document}
import amf.core.client.scala.transform.{TransformationPipelineBuilder, TransformationStep}
import org.scalatest.flatspec.AsyncFlatSpec
import org.scalatest.matchers.should

class PipelineCreationTest extends AsyncFlatSpec with should.Matchers with FileReader {

  private val CUSTOM_PIPELINE_NAME = "MyPipeline"
  private val WEB_API_NAME = "MyTransformedWebApi"

  "AMF" should "let me plug-in a new pipeline and use it" in {
    val pipeline = TransformationPipelineBuilder
      .empty(CUSTOM_PIPELINE_NAME)
      .append(new MyTransformationStep(WEB_API_NAME))
      .build
    val client = WebAPIConfiguration.WebAPI.withTransformationPipeline(pipeline).baseUnitClient
    client.parse("file://src/test/resources/examples/banking-api.json").map { result =>
      result.conforms shouldBe true
      val transformResult = client.transform(result.baseUnit, CUSTOM_PIPELINE_NAME)
      transformResult.conforms shouldBe true
      val doc = transformResult.baseUnit.asInstanceOf[Document]
      val webapi = doc.encodes.asInstanceOf[WebApi]
      webapi.name.value() should equal(WEB_API_NAME)
    }
  }

  class MyTransformationStep(val webApiName: String) extends TransformationStep {
    override def transform(model: BaseUnit, errorHandler: AMFErrorHandler, config: AMFGraphConfiguration): BaseUnit = model match {
      case document: Document =>
        val api = document.encodes.asInstanceOf[WebApi]
        api.withName(webApiName)
        document
      case _ => model
    }
  }
}
