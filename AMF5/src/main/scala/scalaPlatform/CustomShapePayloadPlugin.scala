package scalaPlatform

import amf.core.client.common.validation.{ProfileName, ValidationMode}
import amf.core.client.scala.model.document.PayloadFragment
import amf.core.client.scala.model.domain.Shape
import amf.core.client.scala.validation.AMFValidationReport
import amf.core.client.scala.validation.payload.{AMFShapePayloadValidationPlugin, AMFShapePayloadValidator, ShapeValidationConfiguration, ValidatePayloadRequest}
import amf.shapes.client.scala.model.domain.ScalarShape

import scala.concurrent.Future

class CustomShapePayloadPlugin(mediaTypeToValidate: String) extends AMFShapePayloadValidationPlugin {

  override def applies(element: ValidatePayloadRequest): Boolean = {
    element.mediaType == mediaTypeToValidate && element.shape.isInstanceOf[ScalarShape]
  }

  override def validator(
      shape: Shape,
      mediaType: String,
      config: ShapeValidationConfiguration,
      validationMode: ValidationMode
  ): AMFShapePayloadValidator = new MySpyShapeValidator(shape)

  override val id: String = "MyPlugin"
}

class MySpyShapeValidator(val shape: Shape, var isExecuted: Boolean = false) extends AMFShapePayloadValidator {
  override def validate(payload: String): Future[AMFValidationReport] = Future.successful { syncValidate(payload) }

  override def validate(payloadFragment: PayloadFragment): Future[AMFValidationReport] = Future.successful(syncValidate(""))

  override def syncValidate(payload: String): AMFValidationReport = {
    isExecuted = true
    AMFValidationReport.empty(shape.id, ProfileName("RAML 1.0"))
  }
}
