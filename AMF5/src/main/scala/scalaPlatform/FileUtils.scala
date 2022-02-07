package scalaPlatform

trait FileUtils {

  def getStrFromFile(path: String): String = {
    val source = scala.io.Source.fromFile(path)
    val read =
      try source.mkString
      finally source.close()
    read
  }

  def addPrefix(input: String) = s"file://$input"

}
