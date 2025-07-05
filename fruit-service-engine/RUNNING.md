# Fruit Service Engine - Running Instructions

## Prerequisites
- Java 8 or higher installed
- JAVA_HOME environment variable set

## Compilation
1. Run the compilation script:
   ```batch
   compile.bat
   ```

## Running the Application

### Method 1: Using Batch Files
1. **Start the Server (in one terminal):**
   ```batch
   start-server.bat
   ```
   
2. **Start the Client (in another terminal):**
   ```batch
   start-client.bat
   ```

### Method 2: Manual Command Line
1. **Compile the project:**
   ```batch
   javac -cp "src\main\java;target\classes" -d target\classes src\main\java\server\interfaces\*.java
   javac -cp "src\main\java;target\classes" -d target\classes src\main\java\model\*.java
   javac -cp "src\main\java;target\classes" -d target\classes src\main\java\server\*.java
   javac -cp "src\main\java;target\classes" -d target\classes src\main\java\client\*.java
   ```

2. **Start RMI Registry (in one terminal):**
   ```batch
   rmiregistry 1099
   ```

3. **Start the Server (in another terminal):**
   ```batch
   java -cp target\classes -Djava.rmi.server.hostname=localhost server.FruitComputeEngine
   ```

4. **Start the Client (in a third terminal):**
   ```batch
   java -cp target\classes client.FruitClient
   ```

## Troubleshooting

### Error: "Could not find or load main class"
- Ensure all Java files are compiled without errors
- Check that the classpath includes `target\classes`
- Verify Java is installed and in your PATH

### Error: "java.rmi.ConnectException"
- Ensure the RMI registry is running on port 1099
- Ensure the server is started before the client
- Check firewall settings

### Error: "ClassNotFoundException"
- Recompile all classes using the compilation script
- Ensure all dependencies are on the classpath

## Features
- Add fruit prices to the server database
- Update existing fruit prices
- Delete fruit entries
- Calculate costs based on quantity
- Generate receipts with cashier information

## Usage
The client provides an interactive menu with the following options:
1. Add Fruit Price
2. Update Fruit Price  
3. Delete Fruit Price
4. Calculate Fruit Cost
5. Print Receipt
6. Exit
