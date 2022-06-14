package scalaPlatform

import amf.apicontract.client.scala.RAMLConfiguration
import amf.core.client.platform.config.AMFEventNames
import amf.core.client.scala.config.{AMFEvent, AMFEventListener, StartingParsingEvent}
import org.junit.Assert.{assertNotNull, assertTrue}
import org.junit.runner.RunWith
import org.scalatest.flatspec.AsyncFlatSpec
import org.scalatest.matchers.should
import org.scalatestplus.junit.JUnitRunner

import scala.collection.mutable

@RunWith(classOf[JUnitRunner])
class EventListenerTest extends AsyncFlatSpec with should.Matchers {

  "AMF" should "notify about certain events" in {
    val spy = new SpyEventListener()
    val client = RAMLConfiguration.RAML10()
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
