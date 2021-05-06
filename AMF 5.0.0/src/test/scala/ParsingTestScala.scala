import amf.client.environment.{OASConfiguration, RAMLConfiguration}
import org.junit.Assert._
import org.junit.Test

import scala.concurrent.Await
import scala.concurrent.duration._

// ALL tests fail because parse is not implemented yet
// TODO: test when parse is implemented
class ParsingTestScala {

  @Test def parseOas20(): Unit = {
    val resultFuture = OASConfiguration
      .OAS20()
      .createClient()
      .parse("file://resources/examples/banking-api.json")

    val parsingResult = Await.result(resultFuture, 1 second)
    assertTrue(parsingResult.conforms)
  }

  @Test def parseOas20String(): Unit = {
    val environment = OASConfiguration.OAS()
    val api =
      """{
        |    "swagger": 2.0,
        |    "info": {
        |        "title": "ACME Banking HTTP API",
        |        "version": "1.0"
        |    },
        |    "host": "acme-banking.com"
        |}""".stripMargin
    AMFParser.parseContent(api, environment)
    // ...
  }

  @Test def parseOas30(): Unit = {
    val resultFuture = OASConfiguration
      .OAS30()
      .createClient()
      .parse("file://resources/examples/banking-api-oas30.json")

    val parsingResult = Await.result(resultFuture, 1 second)
    assertTrue(parsingResult.conforms)
  }

  @Test def parseRaml10(): Unit = {
    val resultFuture = RAMLConfiguration
      .RAML08()
      .createClient()
      .parse("file://resources/examples/banking-api.raml")

    val parsingResult = Await.result(resultFuture, 1 second)
    assertTrue(parsingResult.conforms)
  }

  @Test def parseRaml10String(): Unit = {
    val environment = RAMLConfiguration.RAML()
    val api =
      """#%RAML 1.0
        |
        |title: ACME Banking HTTP API
        |version: 1.0""".stripMargin
    AMFParser.parseContent(api, environment)
    // ...
  }

  @Test def parseRaml08(): Unit = {
    val resultFuture = RAMLConfiguration
      .RAML08()
      .createClient()
      .parse("file://resources/examples/banking-api-08.raml")

    val parsingResult = Await.result(resultFuture, 1 second)
    assertTrue(parsingResult.conforms)
  }

  @Test def parseRaml08String(): Unit = {
    val environment = RAMLConfiguration.RAML()
    val api =
      """#%RAML 0.8
        |
        |title: ACME Banking HTTP API
        |version: 1.0""".stripMargin
    AMFParser.parseContent(api, environment)
    // ...
  }

  // TODO: add graph parse and make tests work
}
