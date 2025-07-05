@echo off
echo ================================================
echo    Fruit Service RMI Server (Multi-Computer)
echo ================================================
echo.

REM Get the local IP address
for /f "tokens=2 delims=:" %%a in ('ipconfig ^| findstr /c:"IPv4 Address"') do (
    set "LOCAL_IP=%%a"
    goto :found_ip
)
:found_ip
set LOCAL_IP=%LOCAL_IP: =%

echo Starting RMI Registry on port 1099...
start "RMI Registry" rmiregistry 1099
echo Waiting for RMI Registry to start...
timeout /t 3 /nobreak > nul
echo.

echo Starting Fruit Compute Engine Server...
echo Server will be accessible at: %LOCAL_IP%:1099
echo.
echo ================================================
echo IMPORTANT: Share this information with clients:
echo   Server IP: %LOCAL_IP%
echo   Port: 1099
echo   Service: FruitComputeEngine
echo ================================================
echo.

java -cp target\classes -Djava.rmi.server.hostname=%LOCAL_IP% server.FruitComputeEngine
pause
