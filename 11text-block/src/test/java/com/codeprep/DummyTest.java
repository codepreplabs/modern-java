package com.codeprep;

/**
 * Dummy test class for demonstration purposes
 * This is a placeholder test file for the Text Block module
 */
public class DummyTest {

    public void testTextBlockBasic() {
        String expected = """
                Hello, Text Blocks!
                This is a multi-line string.
                """;
        String actual = """
                Hello, Text Blocks!
                This is a multi-line string.
                """;
        System.out.println("Testing basic text block");
        assert expected.equals(actual) : "Text blocks should match";
    }

    public void testTextBlockFormatting() {
        String name = "Java";
        String formatted = """
                Welcome to %s!
                """.formatted(name);
        System.out.println("Testing formatted text block: " + formatted);
        assert formatted.contains("Java") : "Formatted text should contain Java";
    }

    public void testOldVsNewWay() {
        // Old way with concatenation
        String oldWay = "Line 1\n" + "Line 2\n" + "Line 3\n";

        // New way with text blocks
        String newWay = """
                Line 1
                Line 2
                Line 3
                """;

        System.out.println("Testing old vs new way comparison");
        assert oldWay.equals(newWay) : "Both approaches should produce the same result";
    }

    public static void main(String[] args) {
        DummyTest test = new DummyTest();

        System.out.println("Running Text Block Tests...\n");

        test.testTextBlockBasic();
        System.out.println("✓ Basic text block test passed\n");

        test.testTextBlockFormatting();
        System.out.println("✓ Formatted text block test passed\n");

        test.testOldVsNewWay();
        System.out.println("✓ Old vs new way comparison test passed\n");

        System.out.println("All tests passed successfully!");
    }
}
