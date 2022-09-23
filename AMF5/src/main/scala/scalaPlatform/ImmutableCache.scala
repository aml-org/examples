package scalaPlatform

import amf.core.client.platform.resource.ResourceNotFound
import amf.core.client.scala.config.{CachedReference, UnitCache}
import amf.core.client.scala.model.document.BaseUnit

import scala.concurrent.Future

class ImmutableCache(private val cache: Map[String, CachedReference] = Map.empty) extends UnitCache {


  def add(alias: String, url: String, unit: BaseUnit): ImmutableCache = {
    new ImmutableCache(cache + (alias -> CachedReference(url, unit)))
  }

  override def fetch(url: String): Future[CachedReference] = cache.get(url).map(Future.successful).getOrElse(Future.failed(new ResourceNotFound(url)))
}
