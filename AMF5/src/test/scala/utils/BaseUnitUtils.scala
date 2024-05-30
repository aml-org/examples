package utils
import amf.apicontract.client.scala.model.domain.{EndPoint, Message, Operation, Request, Server}
import amf.apicontract.client.scala.model.domain.api.{Api, AsyncApi, WebApi}
import amf.core.client.scala.model.document.{BaseUnit, Document}
import amf.core.client.scala.model.domain.DomainElement

object BaseUnitUtils {
  def getApi(baseUnit: BaseUnit, isAsyncApi: Boolean = true): Api =
    if (isAsyncApi) baseUnit.asInstanceOf[Document].encodes.asInstanceOf[AsyncApi]
    else baseUnit.asInstanceOf[Document].encodes.asInstanceOf[WebApi]

  def getDeclarations(baseUnit: BaseUnit): scala.Seq[DomainElement] =
    baseUnit.asInstanceOf[Document].declares

  def getFirstServer(baseUnit: BaseUnit, isAsyncApi: Boolean = true): Server =
    getApi(baseUnit, isAsyncApi).servers.head

  def getLastServer(baseUnit: BaseUnit, isAsyncApi: Boolean = true): Server =
    getApi(baseUnit, isAsyncApi).servers.last

  def getLastEndpoint(baseUnit: BaseUnit, isAsyncApi: Boolean = true): EndPoint =
    getApi(baseUnit, isAsyncApi).endPoints.last

  def getFirstOperationFromLastEndpoint(baseUnit: BaseUnit, isAsyncApi: Boolean = true): Operation =
    getLastEndpoint(baseUnit, isAsyncApi).operations.head

  def getFirstRequest(baseUnit: BaseUnit, isAsyncApi: Boolean = true): Request =
    getFirstOperationFromLastEndpoint(baseUnit, isAsyncApi).requests.head

  def getMessageComponent(baseUnit: BaseUnit): Message =
    getDeclarations(baseUnit)(2).asInstanceOf[Message]

}
