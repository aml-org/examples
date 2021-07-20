package javaPlatform;

import amf.core.client.common.validation.ProfileName;
import amf.core.client.platform.model.document.PayloadFragment;
import amf.core.client.platform.model.domain.Shape;
import amf.core.client.platform.validation.AMFValidationReport;
import amf.core.client.platform.validation.payload.AMFShapePayloadValidator;

import java.util.Collections;
import java.util.concurrent.CompletableFuture;

class MySpyShapeValidator implements AMFShapePayloadValidator {

    private final Shape shape;
    public boolean isExecuted;

    public MySpyShapeValidator(Shape shape) {
        this.shape = shape;
        this.isExecuted = false;
    }

    @Override
    public CompletableFuture<AMFValidationReport> validate(String payload) {
        return CompletableFuture.completedFuture(syncValidate(payload));
    }

    @Override
    public CompletableFuture<AMFValidationReport> validate(PayloadFragment payloadFragment) {
        return CompletableFuture.completedFuture(syncValidate(""));
    }

    @Override
    public AMFValidationReport syncValidate(String payload) {
        isExecuted = true;
        return new AMFValidationReport(shape.id(), ProfileName.apply("RAML 1.0"), Collections.emptyList());
    }
}
