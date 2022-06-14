package scalaPlatform

import scala.io.Source

trait FileReader {

  protected def using(source: Source)(func: Source => String): String = {
    val toReturn = func(source)
    source.close()
    toReturn
  }

  protected def readResource(path: String): String = {
    val source = Source.fromInputStream(this.getClass.getResourceAsStream(path))
    using(source) { _.mkString }
  }
}
