package scalaPlatform

import amf.core.client.common.remote.Content
import amf.core.client.scala.resource.ResourceLoader
import resource._

import java.util.regex.Pattern
import scala.concurrent.Future
import scala.io.Source

class CustomResourceLoader(private val pathPattern: Pattern) extends ResourceLoader {

  /** Fetch specified resource and return associated content. Resource should have been previously accepted. */
  override def fetch(resource: String): Future[Content] = {
    val normalizedPath = resource.substring(pathPattern.pattern.length - 1)
    var content = ""
    for { source <- managed(Source.fromFile(normalizedPath)) } {
      content = source.mkString
    }
    Future.successful(new Content(content, resource))
  }

  /** Accepts specified resource. */
  override def accepts(resource: String): Boolean = {
    if (resource == null || resource.isEmpty) return false
    val matcher = pathPattern.matcher(resource)
    matcher.find
  }
}
