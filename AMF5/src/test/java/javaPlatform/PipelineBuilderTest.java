package javaPlatform;

import amf.apicontract.client.platform.AMFClient;
import amf.apicontract.client.platform.OASConfiguration;
import amf.core.client.platform.AMFGraphClient;
import amf.core.client.platform.AMFGraphConfiguration;
import amf.core.client.platform.AMFResult;
import amf.core.client.platform.errorhandling.ClientErrorHandler;
import amf.core.client.platform.model.document.BaseUnit;
import amf.core.client.platform.model.document.Document;
import amf.core.client.platform.model.domain.DomainElement;
import amf.core.client.platform.transform.TransformationPipeline;
import amf.core.client.platform.transform.TransformationPipelineBuilder;
import amf.core.client.platform.transform.TransformationStep;
import org.junit.Test;

import java.util.concurrent.ExecutionException;

import static org.junit.Assert.*;
import static org.junit.Assert.assertNotNull;

public class PipelineBuilderTest {

    @Test
    public void createPipelineFromEmpty() throws ExecutionException, InterruptedException {
        final String pipelineName = "defaultName";
        TransformationPipeline pipeline = TransformationPipelineBuilder.empty(pipelineName).append(new AddIdCustomStep()).build();
        final AMFGraphClient client = AMFGraphConfiguration.predefined().withTransformationPipeline(pipeline).createClient();
        BaseUnit doc = new Document().withId("oldId");
        client.transform(doc, pipelineName);
        assert(doc.id().equals("newId"));
    }

    private class AddIdCustomStep implements TransformationStep {
        @Override
        public BaseUnit transform(BaseUnit model, ClientErrorHandler errorHandler) {
            return model.withId("newId");
        }
    }
}
