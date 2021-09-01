package scalaPlatform

import amf.aml.client.scala.AMLConfiguration
import amf.aml.client.scala.model.document.Dialect
import amf.aml.client.scala.model.domain.DialectDomainElement
import junit.framework.TestCase.assertTrue
import org.scalatest.flatspec.AsyncFlatSpec
import org.scalatest.matchers.should

import scala.concurrent.Future.successful

class AMLTest extends AsyncFlatSpec with should.Matchers {
  val simpleDialectWithVocabulary = "file://src/test/resources/examples/dialect-vocab.yaml"
  val simpleVocabulary = "file://src/test/resources/examples/vocabulary.yaml"
  val simpleDialect = "file://src/test/resources/examples/dialect.yaml"
  val simpleDialectInstance = "file://src/test/resources/examples/dialect-instance.yaml"
  val simpleNodeTypeUri = "file://src/test/resources/examples/dialect.yaml#/declarations/Simple"

  "AML" should "parse a dialect" in {
    val client = AMLConfiguration.predefined().baseUnitClient()
    client.parseDialect(simpleDialect) map { parseResult =>
      parseResult.conforms shouldBe true
      val dialect = parseResult.baseUnit.asInstanceOf[Dialect]
      val dialectElementId = dialect.documents().root().encoded().value()
      dialectElementId shouldEqual "file://src/test/resources/examples/dialect.yaml#/declarations/Simple"
    }
  }

  it should "parse a dialect instance" in {
    AMLConfiguration.predefined().withDialect(simpleDialect) flatMap { amlConfig =>
      val client = amlConfig.baseUnitClient()
      client.parseDialectInstance(simpleDialectInstance)
    } map { parseResult =>
      parseResult.conforms shouldBe true
      val instanceElement = parseResult.dialectInstance.encodes.asInstanceOf[DialectDomainElement]
      instanceElement.meta.`type`.map(_.iri()).contains(simpleNodeTypeUri) shouldBe true
    }
  }

  it should "parse a vocabulary" in {
    val client = AMLConfiguration.predefined.baseUnitClient
    for {
      parseResult <- client.parseVocabulary(simpleVocabulary)
    } yield {
      parseResult.conforms shouldBe true
      val vocabularyBase: String = parseResult.vocabulary.base.value
      vocabularyBase shouldEqual "http://simple.org/vocabulary#"
    }
  }

  it should "parse a instance with loaded dialect and vocabulary" in {
    for {
      config <- AMLConfiguration.predefined.withDialect(simpleDialectWithVocabulary)
      client <- successful(config.baseUnitClient())
      parseResult <- client.parseDialectInstance(simpleDialectInstance)
    } yield {
      assertTrue(parseResult.conforms)
      val simplePropA = parseResult.dialectInstance.encodes.asInstanceOf[DialectDomainElement].getScalarByProperty("http://simple.org/vocabulary#simpleA").head
      simplePropA shouldBe "simpleInstance"
    }
  }
}
