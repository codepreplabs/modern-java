package com.codeprep; /**
 * Simple Web Server (JEP 408) - Introduced in Java 18
 *
 * The Simple Web Server provides a minimal HTTP server for prototyping, testing, and debugging.
 * It's a command-line tool and API for serving static files with minimal configuration.
 *
 * Key Features:
 * - Zero-configuration static file server
 * - Serves files from a specified directory via HTTP
 * - Supports only HTTP/1.1 (not HTTPS)
 * - Implements HEAD and GET methods only
 * - Returns appropriate status codes (200, 404, 500, etc.)
 * - Directory listing support
 * - MIME type detection based on file extensions
 *
 * Common Use Cases:
 *
 * 1. Prototyping and Development:
 *    - Quick sharing of HTML/CSS/JS files during development
 *    - Testing single-page applications (SPAs) locally
 *    - Serving static content without setting up Apache/Nginx
 *
 * 2. Testing and Debugging:
 *    - Creating minimal reproducible test cases
 *    - Testing HTTP client implementations
 *    - Serving mock data files for integration tests
 *    - Educational purposes and learning HTTP concepts
 *
 * 3. Documentation and Demos:
 *    - Serving generated documentation (JavaDoc, Markdown HTML)
 *    - Quick demos of static websites
 *    - Sharing files on local network during presentations
 *
 * 4. Build Tool Integration:
 *    - Serving build artifacts during CI/CD pipelines
 *    - Testing static site generators output
 *    - Preview builds before deployment
 *
 * Command Line Usage:
 *   jwebserver                              # Serves current directory on port 8000
 *   jwebserver -p 9000                      # Custom port
 *   jwebserver -d /path/to/directory        # Custom directory
 *   jwebserver -b 0.0.0.0                   # Bind to all interfaces
 *
 * Programmatic API Usage:
 *   HttpServer server = SimpleFileServer.createFileServer(
 *       new InetSocketAddress(8080),
 *       Path.of("/path/to/files"),
 *       SimpleFileServer.OutputLevel.VERBOSE
 *   );
 *   server.start();
 *
 * Important Limitations:
 * - NOT suitable for production use (no security features)
 * - No HTTPS support
 * - No authentication or authorization
 * - No POST, PUT, DELETE methods
 * - No dynamic content generation (CGI, servlets, etc.)
 * - Single-threaded or limited concurrency
 *
 * When NOT to use:
 * - Production environments
 * - Applications requiring security
 * - Dynamic web applications
 * - High-traffic scenarios
 * - When you need HTTPS/SSL/TLS
 */

import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.SimpleFileServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.file.Path;

public class SimpleWebServerExample {

    public static void main(String[] args) throws IOException {
        // Example 1: Basic file server
        basicFileServer();

        // Example 2: Custom configuration
        // customFileServer();

        // Example 3: Server with proper shutdown
        // serverWithShutdown();
    }

    /**
     * Creates a basic file server on port 8080 serving current directory
     */
    public static void basicFileServer() throws IOException {
        // Get current directory
        Path currentDir = Path.of(".").toAbsolutePath().normalize();

        // Create server on port 8080
        HttpServer server = SimpleFileServer.createFileServer(
            new InetSocketAddress(8080),
            currentDir,
            SimpleFileServer.OutputLevel.VERBOSE
        );

        // Start the server
        server.start();

        System.out.println("Server started!");
        System.out.println("Serving files from: " + currentDir);
        System.out.println("Access at: http://localhost:8080");
        System.out.println("Press Ctrl+C to stop the server");
    }

    /**
     * Creates a file server with custom configuration
     */
    public static void customFileServer() throws IOException {
        // Specify custom directory
        Path customDir = Path.of("D:/learning/java/modern-java/17web-server");

        // Create server on custom port
        int port = 9000;
        HttpServer server = SimpleFileServer.createFileServer(
            new InetSocketAddress("localhost", port),
            customDir,
            SimpleFileServer.OutputLevel.INFO
        );

        // Start the server
        server.start();

        System.out.println("Custom server started!");
        System.out.println("Serving files from: " + customDir);
        System.out.println("Access at: http://localhost:" + port);
        System.out.println("Press Ctrl+C to stop the server");
    }

    /**
     * Creates a file server with proper shutdown handling
     *
     * How to Stop the Server:
     *
     * 1. Graceful Shutdown (Recommended):
     *    - Press Ctrl+C in the terminal
     *    - The shutdown hook will execute and stop the server cleanly
     *    - Gives the server 3 seconds to finish processing requests
     *
     * 2. Immediate Stop:
     *    - Call server.stop(0) - stops immediately
     *    - May interrupt ongoing requests
     *
     * 3. Delayed Stop:
     *    - Call server.stop(5) - waits up to 5 seconds for requests to finish
     *    - More graceful than immediate stop
     *
     * 4. From Command Line (jwebserver):
     *    - Press Ctrl+C to stop
     *    - Or close the terminal window
     *
     * 5. Kill Process (Last Resort):
     *    Windows: taskkill /F /PID <process_id>
     *    Linux/Mac: kill -9 <process_id>
     */
    public static void serverWithShutdown() throws IOException {
        Path currentDir = Path.of(".").toAbsolutePath().normalize();
        int port = 8080;

        HttpServer server = SimpleFileServer.createFileServer(
            new InetSocketAddress(port),
            currentDir,
            SimpleFileServer.OutputLevel.VERBOSE
        );

        // Add shutdown hook for graceful shutdown on Ctrl+C
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("\n🛑 Shutting down server gracefully...");
            server.stop(3); // Wait up to 3 seconds for active requests to complete
            System.out.println("✅ Server stopped successfully!");
        }));

        // Start the server
        server.start();

        System.out.println("🚀 Server started with graceful shutdown support!");
        System.out.println("📁 Serving files from: " + currentDir);
        System.out.println("🌐 Access at: http://localhost:" + port);
        System.out.println("\n⚡ Press Ctrl+C to stop the server gracefully");
        System.out.println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");

        // Keep the main thread alive so server continues running
        try {
            Thread.currentThread().join();
        } catch (InterruptedException e) {
            System.out.println("Server interrupted");
        }
    }
}
