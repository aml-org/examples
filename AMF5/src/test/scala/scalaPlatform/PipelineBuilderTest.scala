package scalaPlatform

import amf.core.client.scala.AMFGraphConfiguration
import amf.core.client.scala.errorhandling.{AMFErrorHandler, UnhandledErrorHandler}
import amf.core.client.scala.model.document.{BaseUnit, Document}
import amf.core.client.scala.transform.TransformationPipelineBuilder
import amf.core.client.scala.transform.pipelines.TransformationPipelineRunner
import amf.core.client.scala.transform.stages.TransformationStep
import org.scalatest.flatspec.AsyncFlatSpec
import org.scalatest.matchers.must.Matchers.convertToAnyMustWrapper
import org.scalatest.matchers.should

class PipelineBuilderTest extends AsyncFlatSpec with should.Matchers {

  "Pipeline builder" should "allow creating from empty pipeline" in {
    val pipelineName = "defaultName"
    val pipeline = TransformationPipelineBuilder.empty(pipelineName).append(ModifyIdCustomStep("modified")).build()

    val unit = Document().withId("")
    val client = AMFGraphConfiguration.predefined().withTransformationPipeline(pipeline).createClient()
    client.transform(unit, pipelineName)
    unit.id should be("modified")
  }

  private case class ModifyIdCustomStep(content: String) extends TransformationStep {
    override def transform(baseUnit: BaseUnit, errorHandler: AMFErrorHandler): BaseUnit = {
      baseUnit.withId(baseUnit.id + content)
    }
  }
}
