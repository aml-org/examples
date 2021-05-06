import amf.client.environment.{AsyncAPIConfiguration, OASConfiguration, RAMLConfiguration, WebAPIConfiguration}
import amf.client.remote.Content
import amf.client.resource.ResourceLoader

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import amf.client.convert.CoreClientConverters._

/**
 * Basic, not-working examples saved to help create more complex examples
 */
class TemplateExamples {
  def parseWithDialect() = {
    // import amf-aml
    AMLConfiguration.AML().withDialect("file://dialect.yaml").flatMap(_.createClient().parse("file://myinstance.yaml"))
  }
  // depends on api-contract
  def parseOnlyRaml() = {
    RAMLConfiguration.RAML10().createClient().parse("file://raml.raml").map(_.conforms)
  }

  def withResourceLoader = {
    val rl = new ResourceLoader {
      /** Fetch specified resource and return associated content. Resource should have benn previously accepted. */
      override def fetch(resource: String): ClientFuture[Content] = Future.failed(new Exception)
      /** Accepts specified resource. */
      override def accepts(resource: String): Boolean = false
    }
    WebAPIConfiguration.WebAPI().withResourceLoader(rl).createClient().parse("")
  }

  def parseString = {
    val environment = OASConfiguration.OAS()
    AMFParser.parseContent("", environment)
  }

  def parseSyncANDAsync() = {
    WebAPIConfiguration.WebAPI().merge(AsyncAPIConfiguration.Async20()).createClient().parse("")
  }
  // basico: parse, render, validar, transformar
  // parse -> instance -> la guardo
  // como valido esa instancia?
  // cast cuando iteramos modelo? result tipado? ver como queda y analizar
  // rendering, a json ld, a algo nativo
  // default del graph rendering sea pretty print (el default del flatten no es pretty)
  // advanced: transformacion a spec, render con configs.
  // transformation, nativo, como construir tu pipeline
  // withCustomProfile
  // semantic extensions
}
