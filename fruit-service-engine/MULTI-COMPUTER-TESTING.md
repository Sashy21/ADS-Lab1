# Multi-Computer RMI Testing Guide

## Overview
This guide helps you test the Fruit Service RMI application across different computers to demonstrate distributed computing capabilities with proper RMI interface implementation.

## RMI Interface Implementation ✅
Your application correctly implements RMI interfaces:
- **Compute Interface**: `server.interfaces.Compute` extends `Remote`
- **Task Interface**: `server.interfaces.Task<T>` extends `Serializable`
- **Implementation**: `FruitComputeEngine` implements `Compute`
- **All methods throw `RemoteException`** as required by RMI

## Prerequisites

### Network Requirements
1. **Both computers must be on the same network** (LAN/WiFi)
2. **Firewall Configuration**:
   - Allow Java/javaw.exe through Windows Firewall
   - Open port 1099 for RMI Registry
   - Open dynamic ports for RMI communication (or configure fixed ports)

### Software Requirements
- Java JDK 8 or higher on both computers
- Compiled Fruit Service application

## Step-by-Step Testing Process

### Computer 1 (Server Machine)

1. **Compile the application**:
   ```batch
   compile.bat
   ```

2. **Start the RMI Server**:
   ```batch
   start-server-remote.bat
   ```
   
3. **Note the displayed information**:
   ```
   Server IP: 192.168.1.100  (example)
   Port: 1099
   Service: FruitComputeEngine
   ```

4. **Share this IP address with the client computer**

### Computer 2 (Client Machine)

1. **Copy the compiled application** to the client computer:
   - Copy the entire `fruit-service-engine` folder
   - OR copy just the `target/classes` folder and client batch file

2. **Start the RMI Client**:
   ```batch
   start-client-remote.bat
   ```

3. **Enter server details when prompted**:
   ```
   Enter server hostname/IP: 192.168.1.100
   Enter server port: 1099
   ```

4. **Test RMI operations**:
   - Add fruit prices
   - Update fruit prices
   - Calculate costs
   - Generate receipts

## Running on Single Machine (Local Testing)

### Quick Local Test (Original Method)
The original batch files still work for local testing:

```batch
# Terminal 1: Start server
.\start-server.bat

# Terminal 2: Start client
.\start-client.bat
```

### Enhanced Local Test (New Method)
The new scripts also work locally with better diagnostics:

```batch
# Terminal 1: Start server
.\start-server-remote.bat

# Terminal 2: Start client
.\start-client-remote.bat
# When prompted:
# - Server hostname/IP: [Press Enter for localhost]
# - Port: [Press Enter for 1099]
```

### What You'll See Locally
**Server Output:**
```
========================================
Fruit Compute Engine is ready!
Server hostname: 192.168.1.100
RMI Registry port: 1099
Service name: FruitComputeEngine
========================================
```

**Client Output:**
```
=== Fruit Service Client ===
Enter server hostname/IP (or press Enter for localhost): [Press Enter]
Enter server port (or press Enter for 1099): [Press Enter]
Connecting to server at localhost:1099
Successfully connected to remote server!
```

## Testing Scenarios

### Test 1: Basic RMI Interface Interaction
```
Client: Add Apple, $2.50
Server: Stores in HashMap via RMI interface
Client: Calculate cost for 3 apples
Server: Returns $7.50 via RMI interface
```

### Test 2: Cross-Computer Data Persistence
```
Computer A (Client): Add Banana, $1.25
Computer B (Client): Calculate cost for 5 bananas
Result: Should return $6.25 (proves data is shared)
```

### Test 3: Exception Handling
```
Client: Calculate cost for non-existent fruit
Server: Returns appropriate error via RMI interface
```

## Troubleshooting

### Connection Issues
1. **Check network connectivity**:
   ```batch
   ping [server-ip]
   telnet [server-ip] 1099
   ```

2. **Firewall problems**:
   - Temporarily disable firewall to test
   - Add Java to firewall exceptions
   - Open port 1099

3. **RMI hostname issues**:
   ```batch
   java -Djava.rmi.server.hostname=[actual-ip] -cp target\classes server.FruitComputeEngine
   ```

### Common Error Messages

- **"Connection refused"**: Server not running or firewall blocking
- **"Service not bound"**: RMI registry not started or service name incorrect
- **"ClassNotFoundException"**: Client missing compiled classes

## Common Issues and Solutions

### Issue 1: "Service not bound" Error
```
java.rmi.NotBoundException: FruitComputeEngine
```

**Cause**: Server isn't running or failed to start properly.

**Solutions**:
1. **Check Java Version Compatibility**:
   ```batch
   java -version    # Should show Java 8
   javac -version   # May show Java 21
   ```
   If versions mismatch, recompile with Java 8 compatibility:
   ```batch
   .\compile.bat    # Now includes Java 8 compatibility
   ```

2. **Start Server Manually** (to see errors):
   ```batch
   java -cp target\classes server.FruitComputeEngine
   ```

3. **Check if Port 1099 is Available**:
   ```batch
   netstat -an | findstr :1099
   ```

### Issue 2: Java Version Mismatch
**Error**: "only recognizes class file versions up to 52.0"

**Solution**: Code was compiled with Java 21 but running with Java 8.
- Use the updated `compile.bat` script (now includes `-source 8 -target 8`)
- OR upgrade runtime to Java 21
- OR downgrade compiler to Java 8

### Issue 3: Class File Version Compatibility
| Java Version | Class File Version |
|--------------|-------------------|
| Java 8       | 52.0              |
| Java 11      | 55.0              |
| Java 17      | 61.0              |
| Java 21      | 65.0              |

## Verification of RMI Interface Implementation

Your application demonstrates proper RMI interface usage:

1. **Remote Interface**: `Compute` extends `Remote`
2. **Remote Methods**: All methods throw `RemoteException`
3. **Serializable Parameters**: All parameters are serializable
4. **Registry Binding**: Server binds to RMI registry
5. **Client Lookup**: Client looks up remote object
6. **Distributed Execution**: Tasks execute on server, results return to client

## Success Criteria

✅ **Client and server run on different computers**
✅ **RMI communication works across network**
✅ **Interface-based interaction implemented**
✅ **Data persistence shared between computers**
✅ **Error handling via RMI exceptions**

## Network Security Considerations

- Use this setup only on trusted networks
- Consider implementing authentication for production
- RMI uses dynamic ports - configure fixed ports for firewalls if needed
- Monitor network traffic for security assessment

## Advanced Configuration (Optional)

### Fixed Port Configuration
To avoid firewall issues with dynamic ports:

```java
// In FruitComputeEngine
System.setProperty("java.rmi.server.port", "1100");
```

### Security Manager (If Required)
```java
System.setSecurityManager(new SecurityManager());
```

This guide ensures your RMI application meets the requirements for distributed testing with proper interface implementation.
