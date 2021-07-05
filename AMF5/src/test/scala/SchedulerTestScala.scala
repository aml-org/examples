import amf.apicontract.client.scala.RAMLConfiguration
import amf.apicontract.client.scala.model.domain.api.WebApi
import amf.core.client.common.validation.ValidationMode
import amf.core.client.scala.AMFGraphConfiguration
import amf.core.client.scala.errorhandling.UnhandledErrorHandler
import amf.core.client.scala.execution.ExecutionEnvironment
import amf.core.client.scala.model.document.{BaseUnit, Document}
import amf.core.client.scala.model.domain.Shape
import org.scalatest.flatspec.AsyncFlatSpec
import org.scalatest.matchers.should

import java.util.concurrent.{Executors, ThreadFactory}
import scala.concurrent.ExecutionContext

class SchedulerTestScala extends AsyncFlatSpec with should.Matchers {

  "AMF configuration" should "allow defining custom execution context" in {
    /* Instantiating a ScheduledExecutorService with some example arguments. */
    val scheduler = Executors.newScheduledThreadPool(30, new ThreadFactory {
      var i: Int = 0
      override def newThread(r: Runnable): Thread = {
        i = i + 1
        val thread = new Thread(r);
        thread.setName("AMF-" + i);
        thread
      }
    });

    /* Instantiating a ExecutionEnvironment passing as argument the scheduler previously created. */
    val executionEnvironment = new ExecutionEnvironment(ExecutionContext.fromExecutorService(scheduler));

    val config = RAMLConfiguration.RAML10()
      .withExecutionEnvironment(executionEnvironment);
    val client = config.createClient();

    /* call async interfaces */
    for {
      parseResult      <- client.parse("file://resources/examples/simple-api.raml")
      validationResult <- client.validate(parseResult.bu)
      payloadReport <- config
        .payloadValidatorFactory()
        .createFor(obtainShapeFromUnit(parseResult.bu), "application/json", ValidationMode.StrictValidationMode)
        .validate("{\"name\": \"firstname and lastname\"}")
//      newConfig <- config.forInstance("")
    } yield {
      /* Shutting down the scheduler which kills the AMF threads created in the thread pool provided by that scheduler. */
      scheduler.shutdownNow();
      succeed
    }
  }

  def obtainShapeFromUnit(b: BaseUnit): Shape = {
    val webApi = b
      .asInstanceOf[Document]
      .encodes
      .asInstanceOf[WebApi]
    val usersEndpoint = webApi.endPoints.head
    val postMethod    = usersEndpoint.operations.head
    val request       = postMethod.requests.head
    val userPayload   = request.payloads.head
    userPayload.schema
  }
}
