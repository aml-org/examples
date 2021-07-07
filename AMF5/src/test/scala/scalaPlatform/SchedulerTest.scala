package scalaPlatform

import amf.apicontract.client.scala.RAMLConfiguration
import amf.apicontract.client.scala.model.domain.api.WebApi
import amf.core.client.common.remote.Content
import amf.core.client.common.validation.ValidationMode
import amf.core.client.scala.execution.ExecutionEnvironment
import amf.core.client.scala.model.document.{BaseUnit, Document}
import amf.core.client.scala.model.domain.Shape
import amf.core.client.scala.resource.{LoaderWithExecutionContext, ResourceLoader}
import org.scalatest.flatspec.AsyncFlatSpec
import org.scalatest.matchers.should

import java.util.concurrent.{Executors, ThreadFactory}
import scala.concurrent.{ExecutionContext, Future}
import scala.io.Source

class SchedulerTest extends AsyncFlatSpec with should.Matchers {

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
      .withResourceLoaders(List(CustomResourceLoader(scala.concurrent.ExecutionContext.Implicits.global)))
      .withExecutionEnvironment(executionEnvironment); // execution context of loader is adjusted
    val client = config.createClient();

    /* call async interfaces */
    for {
      parseResult      <- client.parse("file://resources/examples/simple-api.raml")
      validationResult <- client.validate(parseResult.bu)
      payloadReport <- config
        .payloadValidatorFactory()
        .createFor(obtainShapeFromUnit(parseResult.bu), "application/json", ValidationMode.StrictValidationMode)
        .validate("{\"name\": \"firstname and lastname\"}")
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

  /*
     resource loader that uses execution context. Extending LoaderWithExecutionContext allows the configuration
     to adapt the loader to the new execution context if modified.
  */
  private case class CustomResourceLoader(ec: ExecutionContext) extends ResourceLoader with LoaderWithExecutionContext {

    override def withExecutionContext(ec: ExecutionContext): ResourceLoader = CustomResourceLoader(ec)

    /** Fetch specified resource and return associated content. Resource should have been previously accepted. */
    override def fetch(resource: String): Future[Content] = {
      val content = Source.fromFile(resource.stripPrefix("file://")).mkString
      Future(new Content(content, resource))(ec)
    }

    /** Accepts specified resource. */
    override def accepts(resource: String): Boolean = true
  }
}
