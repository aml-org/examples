package javaPlatform;

import static org.junit.Assert.assertEquals;

public class StringEquals {

    public static void assertIgnoringWhitespace(String toTest, String expected){
        assertEquals(removeWhiteSpaces(toTest), removeWhiteSpaces(expected));
    }

    private static String removeWhiteSpaces(String input) {
        return input.replaceAll("\\s+", "");
    }
}
