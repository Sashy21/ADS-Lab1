@echo off
echo ================================================
echo Starting Fruit Service with Web Interface
echo ================================================

cd /d "%~dp0"

echo.
echo Step 1: Compiling Java sources...
call compile.bat
if errorlevel 1 (
    echo ERROR: Compilation failed!
    pause
    exit /b 1
)

echo.
echo Step 2: Starting RMI Server...
start "RMI Server" cmd /k "echo Starting RMI Server... && java -cp target\classes -Djava.rmi.server.hostname=localhost server.FruitComputeEngine"

echo.
echo Waiting for RMI server to start...
timeout /t 5 /nobreak >nul

echo.
echo Step 3: Starting REST Bridge...
start "REST Bridge" cmd /k "echo Starting REST Bridge for Web Interface... && java -cp target\classes server.FruitRestBridge"

echo.
echo Waiting for REST bridge to start...
timeout /t 3 /nobreak >nul

echo.
echo Step 4: Starting Web Interface...
cd src\webapp

where python >nul 2>&1
if not errorlevel 1 (
    echo Starting Python HTTP server on port 8000...
    start "Web Interface" cmd /k "echo Web Interface Started! && echo Open: http://localhost:8000 && python -m http.server 8000"
    timeout /t 2 /nobreak >nul
    
    echo.
    echo ================================================
    echo FRUIT SERVICE IS NOW RUNNING!
    echo ================================================
    echo.
    echo RMI Server: Running on localhost:1099
    echo REST Bridge: Running on localhost:8081  
    echo Web Interface: http://localhost:8000
    echo.
    echo The web interface provides a user-friendly GUI for:
    echo - Adding new fruits and prices
    echo - Updating existing fruit prices  
    echo - Deleting fruits from inventory
    echo - Shopping cart functionality
    echo - Cost calculation and receipt generation
    echo.
    echo Architecture:
    echo 1. RMI Server (port 1099) - Core business logic
    echo 2. REST Bridge (port 8081) - Web API gateway
    echo 3. Web Interface (port 8000) - User interface
    echo.
    echo All three components must be running for full functionality.
    echo ================================================
    
    echo.
    echo Opening web browser...
    timeout /t 2 /nobreak >nul
    start http://localhost:8000
    
) else (
    echo Python not found! Please install Python 3.x to run the web interface.
    echo.
    echo Alternative: You can manually open src\webapp\index.html in your browser
    echo and modify the JavaScript to point to your servlet container.
    echo.
    echo For now, you can use the command-line client:
    start "Client" cmd /k "java -cp target\classes client.FruitClient"
)

echo.
echo Press any key to view startup logs or Ctrl+C to exit...
pause >nul
