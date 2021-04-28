import org.junit.Test
import org.junit.Assert._

class ParsingTestScala {
  @Test
  def basicTest(): Unit = {
    val emptyList = Seq.empty
    assertTrue(emptyList.isEmpty)
  }
}
