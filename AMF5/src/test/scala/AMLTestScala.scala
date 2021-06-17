import amf.aml.client.scala.AMLConfiguration
import amf.aml.client.scala.model.document.Dialect
import amf.aml.client.scala.model.domain.DialectDomainElement
import org.scalatest.flatspec.AsyncFlatSpec
import org.scalatest.matchers.should

class AMLTestScala extends AsyncFlatSpec with should.Matchers {
  val simpleDialect = "file://resources/examples/dialect.yaml"
  val simpleDialectInstance = "file://resources/examples/dialect-instance.yaml"
  val simpleNodeTypeUri = "file://resources/examples/dialect.yaml#/declarations/Simple"

  "AML" should "parse a dialect" in {
    val client = AMLConfiguration.predefined().createClient()
    client.parseDialect(simpleDialect) map { parseResult =>
      parseResult.conforms shouldBe true
      val dialect = parseResult.bu.asInstanceOf[Dialect]
      val dialectElementId = dialect.documents().root().encoded().value()
      dialectElementId shouldEqual "file://resources/examples/dialect.yaml#/declarations/Simple"
    }
  }

  it should "parse a dialect instance" in {
    AMLConfiguration.predefined().withDialect(simpleDialect) flatMap { amlConfig =>
      val client = amlConfig.createClient()
      client.parseDialectInstance(simpleDialectInstance) map { parseResult =>
        parseResult.conforms shouldBe true
        val instanceElement = parseResult.dialectInstance.encodes.asInstanceOf[DialectDomainElement]
        instanceElement.meta.`type`.map(_.iri()).contains(simpleNodeTypeUri) shouldBe true
      }
    }
  }
}
