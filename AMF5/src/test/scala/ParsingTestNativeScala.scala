import amf.client.environment.{OASConfiguration, RAMLConfiguration}
import org.junit.Assert._
import org.junit.Test

import scala.concurrent.Await
import scala.concurrent.duration.Duration

class ParsingTestNativeScala {

  @Test def parseOas20(): Unit = {
    val client = OASConfiguration.OAS20().createClient()
    val parsingResult = Await.result(
      client.parse("file://resources/examples/banking-api.json"),
      Duration.Inf
    )
    assertTrue(parsingResult.conforms)
  }

  @Test def parseOas20String(): Unit = {
    val client = OASConfiguration.OAS20().createClient()
    val api =
      """{
        |    "swagger": 2.0,
        |    "info": {
        |        "title": "ACME Banking HTTP API",
        |        "version": "1.0"
        |    },
        |    "host": "acme-banking.com"
        |}""".stripMargin
    val parsingResult = Await.result(client.parseContent(api), Duration.Inf)
    assertTrue(parsingResult.conforms)
  }

  @Test def parseOas30(): Unit = {
    val client = OASConfiguration.OAS30().createClient()
    val parsingResult = Await.result(
      client.parse("file://resources/examples/banking-api-oas30.json"),
      Duration.Inf
    )
    assertTrue(parsingResult.conforms)
  }

  @Test def parseRaml10(): Unit = {
    val client = RAMLConfiguration.RAML10().createClient()
    val parsingResult = Await.result(
      client.parse("file://resources/examples/banking-api.raml"),
      Duration.Inf
    )
    assertTrue(parsingResult.conforms)
  }

  @Test def parseRaml10String(): Unit = {
    val client = RAMLConfiguration.RAML10().createClient()
    val api =
      """#%RAML 1.0
        |
        |title: ACME Banking HTTP API
        |version: 1.0""".stripMargin
    val parsingResult = Await.result(client.parseContent(api), Duration.Inf)
    assertTrue(parsingResult.conforms)
  }

  @Test def parseRaml08(): Unit = {
    val client = RAMLConfiguration.RAML08().createClient()
    val parsingResult = Await.result(client.parse("file://resources/examples/banking-api-08.raml"), Duration.Inf)
    assertTrue(parsingResult.conforms)
  }

  @Test def parseRaml08String(): Unit = {
    val client = RAMLConfiguration.RAML08().createClient()
    val api =
      """#%RAML 0.8
        |
        |title: ACME Banking HTTP API
        |version: 1.0""".stripMargin
    val parsingResult = Await.result(client.parseContent(api), Duration.Inf)
    assertTrue(parsingResult.conforms)
  }
  // TODO: add graph parse
}
