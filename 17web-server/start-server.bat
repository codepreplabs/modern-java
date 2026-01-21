@echo off
REM Simple Web Server Launcher Script
REM This script helps launch jwebserver if it's not in your PATH

echo Starting Simple Web Server...
echo.

REM Check if JAVA_HOME is set
if "%JAVA_HOME%"=="" (
    echo ERROR: JAVA_HOME is not set
    echo Please set JAVA_HOME to your JDK installation directory
    pause
    exit /b 1
)

REM Check if jwebserver exists
if not exist "%JAVA_HOME%\bin\jwebserver.exe" (
    echo ERROR: jwebserver.exe not found in %JAVA_HOME%\bin
    echo Please ensure you have Java 18 or later installed
    pause
    exit /b 1
)

REM Default values
set PORT=8080
set DIRECTORY=%CD%

REM Parse command line arguments
:parse_args
if "%~1"=="" goto run_server
if /i "%~1"=="-p" (
    set PORT=%~2
    shift
    shift
    goto parse_args
)
if /i "%~1"=="-d" (
    set DIRECTORY=%~2
    shift
    shift
    goto parse_args
)
shift
goto parse_args

:run_server
echo Running: %JAVA_HOME%\bin\jwebserver.exe -p %PORT% -d "%DIRECTORY%"
echo Server will be available at: http://localhost:%PORT%
echo Press Ctrl+C to stop the server
echo.
"%JAVA_HOME%\bin\jwebserver.exe" -p %PORT% -d "%DIRECTORY%"
