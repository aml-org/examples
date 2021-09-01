package scalaPlatform

import org.scalactic.{AbstractStringUniformity, Uniformity}

object TestUtils {
  val strippedOfWhitespace: Uniformity[String] =
    new AbstractStringUniformity {
      /**Returns the string with all consecutive white spaces reduced to a single space.*/
      def normalized(s: String): String = s.replaceAll("\\s+", "")
      override def toString: String = "withoutWhitespace"
    }

  val sortingLines: Uniformity[String] =
    new AbstractStringUniformity {
      override def normalized(a: String): String = a.split("\n").sorted.mkString
    }
}
