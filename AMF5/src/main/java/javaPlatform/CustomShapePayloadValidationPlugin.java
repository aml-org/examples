package javaPlatform;

import amf.core.client.common.PluginPriority;
import amf.core.client.common.validation.ValidationMode;
import amf.core.client.platform.model.domain.Shape;
import amf.core.client.platform.validation.payload.AMFShapePayloadValidationPlugin;
import amf.core.client.platform.validation.payload.AMFShapePayloadValidator;
import amf.core.client.platform.validation.payload.ShapeValidationConfiguration;
import amf.core.client.platform.validation.payload.ValidatePayloadRequest;
import amf.shapes.client.platform.model.domain.ScalarShape;

import java.util.Objects;

public class CustomShapePayloadValidationPlugin implements AMFShapePayloadValidationPlugin {

    private final String mediaTypeToValidate;

    public CustomShapePayloadValidationPlugin(String mediaTypeToValidate) {
        this.mediaTypeToValidate = mediaTypeToValidate;
    }

    @Override
    public String id() {
        return "MyCustomPlugin";
    }

    @Override
    public PluginPriority priority() {
        return AMFShapePayloadValidationPlugin.super.priority();
    }

    @Override
    public boolean applies(ValidatePayloadRequest element) {
        return Objects.equals(element.mediaType(), mediaTypeToValidate) && element.shape() instanceof ScalarShape;
    }

    @Override
    public AMFShapePayloadValidator validator(Shape shape, String mediaType, ShapeValidationConfiguration config, ValidationMode validationMode) {
        return new MySpyShapeValidator(shape);
    }
}

