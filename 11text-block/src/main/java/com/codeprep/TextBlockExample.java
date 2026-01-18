package com.codeprep;

/*
 * TEXT BLOCKS IN JAVA (Java 15+)
 *
 * Text Blocks is a feature introduced in Java 13 (preview) and made standard in Java 15.
 * It provides a cleaner and more readable way to write multi-line string literals.
 *
 * SYNTAX:
 * - Text blocks are delimited by triple double-quote marks (""")
 * - Opening delimiter must be followed by a line terminator
 * - Closing delimiter can be on the same line as content or on a new line
 *
 * KEY FEATURES:
 * 1. Multi-line strings without explicit escape sequences
 * 2. Automatic indentation management (incidental whitespace is removed)
 * 3. No need for concatenation operators (+) or newline characters (\n)
 * 4. Preserves formatting and makes code more readable
 *
 * USE CASES:
 *
 * 1. SQL QUERIES:
 *    - Write SQL statements across multiple lines without concatenation
 *    - Improves readability and maintainability of complex queries
 *
 * 2. JSON/XML STRINGS:
 *    - Embed JSON or XML content directly in code
 *    - No need to escape quotes or worry about formatting
 *
 * 3. HTML TEMPLATES:
 *    - Write HTML snippets with proper indentation
 *    - Useful for generating email templates or simple web content
 *
 * 4. MULTI-LINE LOG MESSAGES:
 *    - Create well-formatted log messages spanning multiple lines
 *    - Maintain readability in error messages and documentation
 *
 * 5. TEST DATA:
 *    - Define test input data that spans multiple lines
 *    - Makes unit tests more readable
 *
 * 6. REGULAR EXPRESSIONS:
 *    - Write complex regex patterns across multiple lines
 *    - Add comments and improve pattern readability
 *
 * ADVANTAGES:
 * - Eliminates string concatenation clutter
 * - Reduces escape sequence usage
 * - Preserves natural formatting
 * - Makes code more maintainable
 * - Compiler handles indentation automatically
 */
public class TextBlockExample {
    public static void main(String[] args) {
        // Example 1: SQL Query
        sqlQueryExample();

        // Example 2: JSON String
        jsonExample();

        // Example 3: HTML Template
        htmlExample();

        // Example 4: Multi-line Message
        multilineMessageExample();

        // Example 5: Old way vs New way
        comparisonExample();
    }

    // Example 1: SQL Query with Text Blocks
    static void sqlQueryExample() {
        System.out.println("=== SQL Query Example ===");

        String query = """
                SELECT e.employee_id, e.first_name, e.last_name, d.department_name
                FROM employees e
                INNER JOIN departments d ON e.department_id = d.department_id
                WHERE e.salary > 50000
                  AND d.location = 'New York'
                ORDER BY e.last_name, e.first_name;
                """;

        System.out.println(query);
        System.out.println();
    }

    // Example 2: JSON String
    static void jsonExample() {
        System.out.println("=== JSON Example ===");

        String json = """
                {
                  "name": "John Doe",
                  "age": 30,
                  "email": "john.doe@example.com",
                  "address": {
                    "street": "123 Main St",
                    "city": "New York",
                    "zipCode": "10001"
                  },
                  "skills": ["Java", "Python", "JavaScript"]
                }
                """;

        System.out.println(json);
        System.out.println();
    }

    // Example 3: HTML Template
    static void htmlExample() {
        System.out.println("=== HTML Template Example ===");

        String name = "Alice";
        String html = """
                <!DOCTYPE html>
                <html>
                <head>
                    <title>Welcome Page</title>
                </head>
                <body>
                    <h1>Welcome, %s!</h1>
                    <p>Thank you for joining our platform.</p>
                    <a href="/dashboard">Go to Dashboard</a>
                </body>
                </html>
                """.formatted(name);

        System.out.println(html);
        System.out.println();
    }

    // Example 4: Multi-line Log/Error Message
    static void multilineMessageExample() {
        System.out.println("=== Multi-line Message Example ===");

        String errorMessage = """
                ╔═══════════════════════════════════════╗
                ║       CRITICAL ERROR DETECTED         ║
                ╠═══════════════════════════════════════╣
                ║ Error Code: ERR_DB_CONNECTION_FAILED  ║
                ║ Timestamp: 2026-01-18 10:30:45        ║
                ║ Details: Unable to connect to DB      ║
                ║          Connection timeout after 30s ║
                ║ Action: Please check network settings ║
                ╚═══════════════════════════════════════╝
                """;

        System.out.println(errorMessage);
    }

    // Example 5: Old Way vs Text Blocks
    static void comparisonExample() {
        System.out.println("=== Comparison: Old Way vs Text Blocks ===");

        // OLD WAY - Hard to read and maintain
        String oldWay = "{\n" +
                "  \"user\": \"admin\",\n" +
                "  \"password\": \"secret\",\n" +
                "  \"roles\": [\"admin\", \"user\"]\n" +
                "}";

        // NEW WAY - Clean and readable
        String newWay = """
                {
                  "user": "admin",
                  "password": "secret",
                  "roles": ["admin", "user"]
                }
                """;

        System.out.println("Old Way Output:");
        System.out.println(oldWay);
        System.out.println("\nNew Way Output:");
        System.out.println(newWay);
        System.out.println("Both produce the same result, but text blocks are much cleaner!");
    }
}
