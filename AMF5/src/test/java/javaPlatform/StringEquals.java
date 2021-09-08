package javaPlatform;

import java.util.Arrays;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;

public class StringEquals {

    public static void assertIgnoringWhitespace(String toTest, String expected){
        assertEquals(removeWhiteSpaces(expected), removeWhiteSpaces(toTest));
    }

    public static void assertIgnoringWhitespaceAndSortingLines(String toTest, String expected){
        assertEquals(removeWhiteSpaces(sortLines(expected)), removeWhiteSpaces(sortLines(toTest)));
    }

    private static String removeWhiteSpaces(String input) {
        return input.replaceAll("\\s+", "");
    }
    private static String sortLines(String input) {
        return Arrays.stream(input.split("\n")).sorted().collect(Collectors.joining("\n"));
    }
}
