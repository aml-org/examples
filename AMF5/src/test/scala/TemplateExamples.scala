import amf.client.environment.{AsyncAPIConfiguration, WebAPIConfiguration}
import amf.client.exported.AMLConfiguration
import org.junit.Assert.assertTrue

/**
  * Basic, not-working examples saved to help create more complex examples
  */
class TemplateExamples {
  def parseWithDialect() = {
    // import amf-aml
    val client = AMLConfiguration
      .predefined()
      .withDialect("file://dialect.yaml")
      .get()
      .createClient()
    val result = client.parse("file://myinstance.yaml").get()
    assertTrue(result.conforms)
  }

  def parseSyncANDAsync() = {
    WebAPIConfiguration
      .WebAPI()
      .merge(AsyncAPIConfiguration.Async20())
      .createClient()
      .parse("")
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
