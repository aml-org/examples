package scalaPlatform

import amf.apicontract.client.scala.WebAPIConfiguration
import amf.core.client.platform.config.AMFEventNames
import amf.core.client.scala.config.{AMFEvent, AMFEventListener, StartingParsingEvent}
import org.junit.Assert.{assertNotNull, assertTrue}
import org.scalatest.flatspec.AsyncFlatSpec
import org.scalatest.matchers.should

import scala.collection.mutable

class EventListenerTest extends AsyncFlatSpec with should.Matchers {

  "AMF" should "notify about certain events" in {
    val spy = new SpyEventListener()
    val client = WebAPIConfiguration.WebAPI()
      .withEventListener(spy)
      .baseUnitClient()

    client.parse("file://src/test/resources/examples/banking-api.raml").map { parseResult =>
      val model = parseResult.baseUnit
      assertNotNull(model)
      assertTrue(parseResult.conforms)
      assert(spy.listenedTo.nonEmpty)
    }
  }

  class SpyEventListener extends AMFEventListener {
    val listenedTo: mutable.ListBuffer[StartingParsingEvent] = new mutable.ListBuffer[StartingParsingEvent]

    override def notifyEvent(event: AMFEvent): Unit = {
      if (event.name == AMFEventNames.StartedParse) listenedTo.append(event.asInstanceOf[StartingParsingEvent])
    }
  }
}
