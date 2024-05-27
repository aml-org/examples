package scalaPlatform

import amf.apicontract.client.scala.model.domain.Message
import amf.apicontract.client.scala.model.domain.api.{AsyncApi, WebApi}
import amf.apicontract.client.scala.model.domain.bindings.ChannelBindings
import amf.apicontract.client.scala.{
  APIConfiguration,
  AsyncAPIConfiguration,
  OASConfiguration,
  RAMLConfiguration,
  WebAPIConfiguration
}
import amf.core.client.scala.model.document.Document
import amf.core.internal.remote.Spec
import org.junit.runner.RunWith
import org.scalatest.flatspec.AsyncFlatSpec
import org.scalatest.matchers.must.Matchers.convertToAnyMustWrapper
import org.scalatest.matchers.should
import org.scalatestplus.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class ParsingTest extends AsyncFlatSpec with should.Matchers {

  "AMF client" should "parse an OAS 2.0 API" in {
    val client = OASConfiguration.OAS20().baseUnitClient()
    client.parse("file://src/test/resources/examples/banking-api.json") map { result =>
      result.baseUnit mustBe a[Document]
      result.conforms shouldBe true
    }
  }

  it should "parse an OAS 2.0 API from a string" in {
    val client = OASConfiguration.OAS20().baseUnitClient()
    val api =
      """{
        |    "swagger": "2.0",
        |    "info": {
        |        "title": "ACME Banking HTTP API",
        |        "version": "1.0"
        |    },
        |    "host": "acme-banking.com",
        |    "paths": {}
        |}""".stripMargin
    client.parseContent(api) map { result =>
      result.baseUnit mustBe a[Document]
      result.conforms shouldBe true
    }
  }

  it should "parse an OAS 3.0 API" in {
    val client = OASConfiguration.OAS30().baseUnitClient()
    client.parse(
      "file://src/test/resources/examples/banking-api-oas30.json"
    ) map { result =>
      result.baseUnit mustBe a[Document]
      result.conforms shouldBe true
    }
  }

  it should "parse an OAS 3.0 API from a string" in {
    val client = OASConfiguration.OAS30().baseUnitClient()
    val api =
      """{
        |  "openapi": "3.0.0",
        |  "info": {
        |    "title": "Basic content",
        |    "version": "0.1"
        |  },
        |  "paths": {}
        |}""".stripMargin
    client.parseContent(api) map { result =>
      result.baseUnit mustBe a[Document]
      result.conforms shouldBe true
    }
  }

  it should "parse an RAML 1.0 API" in {
    val client = RAMLConfiguration.RAML10().baseUnitClient()
    client.parse("file://src/test/resources/examples/banking-api.raml") map { result =>
      result.baseUnit mustBe a[Document]
      result.conforms shouldBe true
    }
  }

  it should "parse an RAML 1.0 API from a string" in {
    val client = RAMLConfiguration.RAML10().baseUnitClient()
    val api =
      """#%RAML 1.0
        |
        |title: ACME Banking HTTP API
        |version: 1.0""".stripMargin
    client.parseContent(api) map { result =>
      result.baseUnit mustBe a[Document]
      result.conforms shouldBe true
    }
  }

  it should "parse an RAML 0.8 API" in {
    val client = RAMLConfiguration.RAML08().baseUnitClient()
    client.parse(
      "file://src/test/resources/examples/banking-api-08.raml"
    ) map { result =>
      result.baseUnit mustBe a[Document]
      result.conforms shouldBe true
    }
  }

  it should "parse an RAML 0.8 API from a string" in {
    val client = RAMLConfiguration.RAML08().baseUnitClient()
    val api =
      """#%RAML 0.8
        |
        |title: ACME Banking HTTP API
        |version: 1.0""".stripMargin
    client.parseContent(api) map { result =>
      result.baseUnit mustBe a[Document]
      result.conforms shouldBe true
    }
  }

  it should "parse an unknown WebAPI" in {
    val client = WebAPIConfiguration.WebAPI().baseUnitClient()
    client.parse("file://src/test/resources/examples/banking-api.raml") map { result =>
      result.baseUnit mustBe a[Document]
      result.conforms shouldBe true
      result.sourceSpec.isRaml shouldBe true
      result.sourceSpec.id mustBe Spec.RAML10.id
    }
  }

  it should "parse an unknown WebAPI from a string" in {
    val client = WebAPIConfiguration.WebAPI().baseUnitClient()
    val api =
      """{
        |  "openapi": "3.0.0",
        |  "info": {
        |    "title": "Basic content",
        |    "version": "0.1"
        |  },
        |  "paths": {}
        |}""".stripMargin
    client.parseContent(api) map { result =>
      result.baseUnit mustBe a[Document]
      result.conforms shouldBe true
      result.sourceSpec.isOas shouldBe true
      result.sourceSpec.id mustBe Spec.OAS30.id
    }
  }

  it should "parse an unknown API" in {
    val client = APIConfiguration.API().baseUnitClient()
    client.parse("file://src/test/resources/examples/async.yaml") map { result =>
      result.baseUnit mustBe a[Document]
      result.conforms shouldBe true
      result.sourceSpec.isAsync shouldBe true
      result.sourceSpec.id mustBe Spec.ASYNC20.id
    }
  }

  it should "parse an unknown API from a string" in {
    val client = APIConfiguration.API().baseUnitClient()
    val api =
      """asyncapi: "2.0.0"
        |info:
        |  title: "Something"
        |  version: "1.0"
        |channels: {}
        |""".stripMargin
    client.parseContent(api) map { result =>
      result.baseUnit mustBe a[Document]
      result.conforms shouldBe true
      result.sourceSpec.isAsync shouldBe true
      result.sourceSpec.id mustBe Spec.ASYNC20.id
    }
  }

  it should "parse an AsyncAPI 2.1 API" in {
    val client = AsyncAPIConfiguration.Async20().baseUnitClient()
    client.parse("file://src/test/resources/examples/asyncApi-2.1-all.yaml") map { result =>
      val bu = result.baseUnit.asInstanceOf[Document]
      val encodes = bu.encodes.asInstanceOf[AsyncApi]
      // This version supports Mercure and IBMMQ bindings

      // Server Binding
      val server = encodes.servers.head
      val ibmmqServerBinding = server.bindings.bindings.head

      // Channel Binding
      val someOtherChannel = encodes.endPoints.last
      val ibmmqChannelBinding = someOtherChannel.bindings.bindings.head

      // Message Binding
      val publishOperation = someOtherChannel.operations.head
      val messageRequest = publishOperation.requests.head
      val ibmmqMessageBinding = messageRequest.bindings.bindings.head

      result.baseUnit mustBe a[Document]
      result.conforms shouldBe true
      result.sourceSpec.isAsync shouldBe true
      result.sourceSpec.id mustBe Spec.ASYNC21.id
      ibmmqChannelBinding.componentId shouldBe "/ibmmq-channel"
      ibmmqServerBinding.componentId shouldBe "/ibmmq-server"
      ibmmqMessageBinding.componentId shouldBe "/ibmmq-message"
    }
  }

  it should "parse an AsyncAPI 2.2 API" in {
    val client = AsyncAPIConfiguration.Async20().baseUnitClient()
    client.parse("file://src/test/resources/examples/asyncApi-2.2-all.yaml") map { result =>
      val baseUnit = result.baseUnit.asInstanceOf[Document]
      val encodes = baseUnit.encodes.asInstanceOf[AsyncApi]
      // This version adds AnypointMQ bindings

      // Channel Binding
      val anotherChannel = encodes.endPoints.last
      val anypointMQChannelBinding = anotherChannel.bindings.bindings.head

      // Message Binding
      val publishOperation = anotherChannel.operations.head
      val messageRequest = publishOperation.requests.head
      val anypointMQMessageBinding = messageRequest.bindings.bindings.head

      result.baseUnit mustBe a[Document]
      result.conforms shouldBe true
      result.sourceSpec.isAsync shouldBe true
      result.sourceSpec.id mustBe Spec.ASYNC22.id
      anypointMQChannelBinding.componentId shouldBe "/anypointmq-channel"
      anypointMQMessageBinding.componentId shouldBe "/anypointmq-message"
    }
  }

  it should "parse an AsyncAPI 2.3 API" in {
    val client = AsyncAPIConfiguration.Async20().baseUnitClient()
    client.parse("file://src/test/resources/examples/asyncApi-2.3-all.yaml") map { result =>
      val baseUnit = result.baseUnit.asInstanceOf[Document]
      val encodes = baseUnit.encodes.asInstanceOf[AsyncApi]
      // This version adds Solace bindings

      // Operation Binding
      val forthChannel = encodes.endPoints.last
      val publishOperation = forthChannel.operations.head
      val solaceOperationBinding = publishOperation.bindings.bindings.head

      result.baseUnit mustBe a[Document]
      result.conforms shouldBe true
      result.sourceSpec.isAsync shouldBe true
      result.sourceSpec.id mustBe Spec.ASYNC23.id
      solaceOperationBinding.componentId shouldBe "/solace-operation"
    }
  }

  it should "parse an AsyncAPI 2.4 API" in {
    val client = AsyncAPIConfiguration.Async20().baseUnitClient()
    client.parse("file://src/test/resources/examples/asyncApi-2.4-all.yaml") map { result =>
      //no new bindings in this version
      result.baseUnit mustBe a[Document]
      result.conforms shouldBe true
      result.sourceSpec.isAsync shouldBe true
      result.sourceSpec.id mustBe Spec.ASYNC24.id
    }
  }

  it should "parse an AsyncAPI 2.5 API" in {
    val client = AsyncAPIConfiguration.Async20().baseUnitClient()
    client.parse("file://src/test/resources/examples/asyncApi-2.5-all.yaml") map { result =>
      val baseUnit = result.baseUnit.asInstanceOf[Document]
      val encodes = baseUnit.encodes.asInstanceOf[AsyncApi]
      // This version adds GooglePubSub binding.

      // Channel Binding
      val topicProtoSchema = encodes.endPoints.last
      val googlePubSubChannelBinding = topicProtoSchema.bindings.bindings.head

      // Message Binding
      val messageComponent = baseUnit.declares(2).asInstanceOf[Message]
      val googlePubSubMessageBinding = messageComponent.bindings.bindings.head

      result.baseUnit mustBe a[Document]
      result.conforms shouldBe true
      result.sourceSpec.isAsync shouldBe true
      result.sourceSpec.id mustBe Spec.ASYNC25.id
      // By default we assume that the version is 0.1.0
      googlePubSubChannelBinding.componentId shouldBe "/googlepubsub-channel-010"
      googlePubSubMessageBinding.componentId shouldBe "/googlepubsub-message-010"
    }
  }

  it should "parse an AsyncAPI 2.6 API" in {
    val client = AsyncAPIConfiguration.Async20().baseUnitClient()
    client.parse("file://src/test/resources/examples/asyncApi-2.6-all.yaml") map { result =>
      val baseUnit = result.baseUnit.asInstanceOf[Document]
      val encodes = baseUnit.encodes.asInstanceOf[AsyncApi]
      // This version adds Pulsar bindings.

      // Server Binding
      val theNameServer = encodes.servers.last
      val pulsarServerBinding = theNameServer.bindings.bindings.head

      // Channel Binding
      val sixthChannel = encodes.endPoints.last
      val pulsarChannelBinding = sixthChannel.bindings.bindings.head

      result.baseUnit mustBe a[Document]
      result.conforms shouldBe true
      result.sourceSpec.isAsync shouldBe true
      result.sourceSpec.id mustBe Spec.ASYNC26.id
      pulsarServerBinding.componentId shouldBe "/pulsar-server"
    // Uncomment this line when bug is fixed
//      pulsarChannelBinding.componentId shouldBe "/pulsar-channel"
    }
  }
}
