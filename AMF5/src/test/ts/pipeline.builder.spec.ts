import {
    AMFGraphConfiguration,
    TransformationPipelineBuilder,
    JsTransformationStep,
    TransformationStepFactory, amf, ClientErrorHandler, Document, TransformationPipeline
} from "amf-client-js";
import {expect} from "chai";

describe("Pipeline builder", () => {

    it("from empty pipeline", () => {
        var pipelineName = "simple-pipeline";
        let customStage = TransformationStepFactory.from(new AddIdCustomStep())
        let pipeline: TransformationPipeline = TransformationPipelineBuilder.empty(pipelineName).append(customStage).build()
        let config = AMFGraphConfiguration.predefined().withTransformationPipeline(pipeline)
        let unit = new Document().withId("oldId")
        var amfResult = config.createClient().transform(unit, pipelineName);
        expect(amfResult.baseUnit.id).to.equal("newId")
    });
});

class AddIdCustomStep implements JsTransformationStep {
    transform(model: amf.core.client.platform.model.document.BaseUnit, errorHandler: ClientErrorHandler): amf.core.client.platform.model.document.BaseUnit {
        return model.withId("newId")
    }
}
