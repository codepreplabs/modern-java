# Simple Web Server (Java 18+)

This module demonstrates the Simple Web Server feature introduced in Java 18 (JEP 408).

## What is Simple Web Server?

The Simple Web Server is a minimal HTTP server for prototyping, testing, and debugging. It serves static files from a directory with zero configuration.

## Prerequisites

- Java 18 or later (you have Java 25 ✓)
- `jwebserver` command available in your PATH

## Running the Server

### Method 1: Using the Helper Script (Recommended)

```cmd
start-server.bat
```

With custom port:
```cmd
start-server.bat -p 9000
```

With custom directory:
```cmd
start-server.bat -d path\to\directory
```

### Method 2: Using Full Path

If `jwebserver` is not in your PATH, use the full path:

```powershell
& "C:\Program Files\Java\jdk-25\bin\jwebserver.exe" -p 9000
```

### Method 3: Add to PATH (Permanent Solution)

Add `%JAVA_HOME%\bin` to your system PATH:

1. Open System Properties → Environment Variables
2. Under System Variables, find `Path`
3. Click Edit → New
4. Add: `C:\Program Files\Java\jdk-25\bin`
5. Click OK and restart your terminal

After this, you can simply run:
```cmd
jwebserver -p 9000
```

## Command Line Options

| Option | Description | Example |
|--------|-------------|---------|
| (none) | Serves current directory on port 8000 | `jwebserver` |
| `-p <port>` | Custom port | `jwebserver -p 9000` |
| `-d <directory>` | Custom directory | `jwebserver -d D:\files` |
| `-b <address>` | Bind address | `jwebserver -b 0.0.0.0` |
| `-o <level>` | Output level (none/info/verbose) | `jwebserver -o verbose` |
| `-h` | Show help | `jwebserver -h` |

## Use Cases

### 1. **Prototyping and Development**
- Quick sharing of HTML/CSS/JS files
- Testing single-page applications locally
- Serving static content without Apache/Nginx

### 2. **Testing and Debugging**
- Creating minimal reproducible test cases
- Testing HTTP client implementations
- Serving mock data files for integration tests

### 3. **Documentation and Demos**
- Serving generated JavaDoc or HTML documentation
- Quick demos of static websites
- Sharing files on local network during presentations

### 4. **Build Tool Integration**
- Serving build artifacts during CI/CD
- Testing static site generators
- Preview builds before deployment

## Example: Serving Static HTML

1. Create some static files in a directory:
```
17web-server/
  static/
    index.html
    styles.css
    script.js
```

2. Start the server:
```cmd
cd static
jwebserver -p 9000
```

3. Open in browser: http://localhost:9000

## How to Stop the Server

### Quick Answer: Press `Ctrl+C` ✅

This is the standard way to stop both command-line and programmatic servers.

### Method 1: Keyboard Interrupt (Ctrl+C)
```powershell
# While server is running, press Ctrl+C
^C
# Server stops immediately
```

### Method 2: Close Terminal Window
Simply close the PowerShell/Command Prompt window running the server.

### Method 3: Kill Process (If Ctrl+C doesn't work)

Find the process using the port:
```powershell
# Find process using port 8080
netstat -ano | findstr :8080

# Or find all Java processes
Get-Process java
```

Kill the specific process:
```powershell
# Replace <PID> with actual Process ID
taskkill /F /PID <PID>
```

### Method 4: Programmatic Stop (In Java Code)
```java
// Immediate stop
server.stop(0);

// Graceful stop - wait up to 5 seconds
server.stop(5);
```

### Method 5: Graceful Shutdown with Hook
See `serverWithShutdown()` method in `com.codeprep.SimpleWebServerExample.java` for a complete example with graceful shutdown handling.

📚 **For detailed instructions**, see [HOW-TO-STOP-SERVER.md](HOW-TO-STOP-SERVER.md)

## Important Limitations

⚠️ **NOT for Production Use**

- No HTTPS support
- No authentication or authorization
- Only HEAD and GET methods (no POST, PUT, DELETE)
- No dynamic content generation
- Limited concurrency
- No security features

## Troubleshooting

### `jwebserver` command not found

**Problem**: PowerShell says `jwebserver : The term 'jwebserver' is not recognized`

**Solutions**:
1. Use the `start-server.bat` script in this directory
2. Use the full path: `& "$env:JAVA_HOME\bin\jwebserver.exe"`
3. Add `%JAVA_HOME%\bin` to your PATH (see Method 3 above)

### Port already in use

**Problem**: `Address already in use`

**Solution**: Either:
- Use a different port: `jwebserver -p 9001`
- Stop the process using that port

### Java version too old

**Problem**: Java version below 18

**Solution**: Upgrade to Java 18 or later

## Programmatic API Example

See `com.codeprep.SimpleWebServerExample.java` for how to use the API programmatically in Java code.

## References

- [JEP 408: Simple Web Server](https://openjdk.org/jeps/408)
- [Java 18 Release Notes](https://www.oracle.com/java/technologies/javase/18-relnote-issues.html)
