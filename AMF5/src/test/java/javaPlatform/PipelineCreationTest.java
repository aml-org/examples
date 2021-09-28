package javaPlatform;

import amf.apicontract.client.platform.AMFBaseUnitClient;
import amf.apicontract.client.platform.RAMLConfiguration;
import amf.apicontract.client.platform.WebAPIConfiguration;
import amf.apicontract.client.platform.model.domain.api.WebApi;
import amf.core.client.platform.AMFGraphConfiguration;
import amf.core.client.platform.AMFResult;
import amf.core.client.platform.errorhandling.ClientErrorHandler;
import amf.core.client.platform.model.document.BaseUnit;
import amf.core.client.platform.model.document.Document;
import amf.core.client.platform.transform.TransformationPipeline;
import amf.core.client.platform.transform.TransformationPipelineBuilder;
import amf.core.client.platform.transform.TransformationStep;
import org.junit.Test;

import java.util.concurrent.ExecutionException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class PipelineCreationTest {

    final String CUSTOM_PIPELINE_NAME = "MyPipeline";
    final String WEB_API_NAME = "MyTransformedWebApi";

    @Test
    public void createPipelineFromEmpty() throws ExecutionException, InterruptedException {
        final TransformationPipeline pipeline = TransformationPipelineBuilder
                .empty(CUSTOM_PIPELINE_NAME)
                .append(new MyTransformationStep(WEB_API_NAME))
                .build();

        final AMFBaseUnitClient client = WebAPIConfiguration.WebAPI().withTransformationPipeline(pipeline).baseUnitClient();
        final AMFResult result = client.parse("file://src/test/resources/examples/banking-api.json").get();
        assert(result.conforms());
        AMFResult transformResult = client.transform(result.baseUnit(), CUSTOM_PIPELINE_NAME);
        assert(transformResult.conforms());
        Document doc = (Document) transformResult.baseUnit();
        WebApi webapi = (WebApi) doc.encodes();
        assert(webapi.name().value().equals(WEB_API_NAME));
    }

    public class MyTransformationStep implements TransformationStep {

        final String webApiName;

        public MyTransformationStep(String webApiName) {
            this.webApiName = webApiName;
        }

        @Override
        public BaseUnit transform(BaseUnit model, ClientErrorHandler errorHandler, AMFGraphConfiguration config) {
            if (model instanceof Document) {
                Document document = (Document) model;
                WebApi api = (WebApi) document.encodes();
                api.withName(webApiName);
                return document;
            }
            else return model;
        }
    }
}
