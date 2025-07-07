# Fruit Service Engine

## Overview
The Fruit Service Engine is a distributed application system that provides fruit price management through multiple interfaces. The system supports both traditional RMI-based client-server communication and modern web-based interactions. It features a shopping cart system,and receipt generation.

The application demonstrates various distributed computing concepts including:
- **RMI (Remote Method Invocation)** for client-server communication
- **Task-based architecture** for modular operations
- **Web services** with REST API integration
- **Multi-tier architecture** with separate presentation, business, and data layers

## Project Structure
The project is organized as follows:

```
fruit-service-engine/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   ├── server/
│   │   │   │   ├── FruitComputeEngine.java        # Main RMI server
│   │   │   │   ├── FruitComputeTaskRegistry.java  # Task registry with shopping cart
│   │   │   │   ├── FruitRestBridge.java           # REST API bridge
│   │   │   │   ├── FruitWebServer.java            # Web server implementation
│   │   │   │   ├── interfaces/
│   │   │   │   │   ├── Compute.java               # RMI compute interface
│   │   │   │   │   └── Task.java                  # Task interface
│   │   │   │   ├── servlets/
│   │   │   │   │   ├── AddFruitServlet.java       # Add fruit web servlet
│   │   │   │   │   ├── UpdateFruitServlet.java    # Update fruit web servlet
│   │   │   │   │   ├── DeleteFruitServlet.java    # Delete fruit web servlet
│   │   │   │   │   ├── CalculateCostServlet.java  # Calculate cost web servlet
│   │   │   │   │   └── ReceiptServlet.java        # Receipt generation servlet
│   │   │   │   ├── tasks/
│   │   │   │   │   ├── AddFruitPrice.java         # Add fruit price task
│   │   │   │   │   ├── UpdateFruitPrice.java      # Update fruit price task
│   │   │   │   │   ├── DeleteFruitPrice.java      # Delete fruit price task
│   │   │   │   │   └── CalculateCost.java         # Calculate cost task
│   │   │   │   └── util/
│   │   │   │       └── RMIConnectionHelper.java   # RMI connection utilities
│   │   │   ├── client/
│   │   │   │   └── FruitClient.java               # Console client application
│   │   │   └── model/
│   │   │       ├── FruitPrice.java                # Fruit price data model
│   │   │       └── Receipt.java                   # Receipt data model
│   │   └── webapp/
│   │       ├── index.html                         # Web interface
│   │       └── WEB-INF/
│   │           └── web.xml                        # Web app configuration
├── compile.bat                                    # Compilation script
├── start-server.bat                               # Start RMI server
├── start-client.bat                               # Start console client
├── start-web-service.bat                          # Start full web service
├── start-server-remote.bat                        # Start server for remote access
├── start-client-remote.bat                        # Connect to remote server
├── pom.xml                                        # Maven configuration
├── .gitignore                                     # Git ignore file
└── README.md                                      # This file
```

## Features
1. **Add Fruit Price**: Add new fruit price entries to the system
2. **Update Fruit Price**: Update existing fruit price entries
3. **Delete Fruit Price**: Remove fruit price entries from the system
4. **Calculate Fruit Cost**: Calculate total cost based on quantity and add to shopping cart
5. **View Shopping Cart**: Display current items in the shopping cart
6. **Clear Shopping Cart**: Remove all items from the cart
7. **Generate Receipt**: Generate detailed receipts with costs, payment, change, and cashier information
8. **Web Interface**: Modern web-based interface for easy interaction
9. **REST API**: RESTful web services for integration with other systems
10. **Multi-Client Support**: Multiple clients can connect simultaneously via RMI

## Architecture
The application uses a **distributed architecture** with multiple components:

- **RMI Server** (`FruitComputeEngine`): Core business logic and data management
- **Task Registry** (`FruitComputeTaskRegistry`): Manages shopping cart and task execution
- **Web Server** (`FruitWebServer`): Serves the web interface
- **REST Bridge** (`FruitRestBridge`): Provides REST API endpoints
- **Console Client** (`FruitClient`): Command-line interface for direct interaction
- **Web Client**: Browser-based interface via HTML/JavaScript

## Setup Instructions

### Prerequisites
- Java Development Kit (JDK) 8 or higher
- Apache Maven 3.6 or higher
- Git (for cloning the repository)

### Quick Start

#### Using Batch Files (Windows)
1. **Clone the Repository**:
   ```bash
   git clone <repository-url>
   cd fruit-service-engine
   ```

2. **Start the Complete Web Service**:
   ```bash
   # This will compile, start RMI server, REST bridge, and web interface
   start-web-service.bat
   ```

3. **Access the Application**:
   - Web Interface: `http://localhost:8000`
   - Console Client: Run `start-client.bat`

## Usage

### Console Client
1. Run the client application
2. Enter server hostname/IP (default: localhost)
3. Enter server port (default: 1099)
4. Use the interactive menu to:
   - Add/update/delete fruit prices
   - Calculate costs and add to cart
   - View shopping cart
   - Generate receipts

### Web Interface
1. Open `http://localhost:8000` in your browser
2. Use the modern web interface to manage fruit prices
3. Add items to cart and generate receipts
4. All operations are available through the web UI

### REST API
The system provides REST endpoints for integration:
- `POST /api/fruits` - Add fruit price
- `PUT /api/fruits/{name}` - Update fruit price
- `DELETE /api/fruits/{name}` - Delete fruit price
- `POST /api/calculate` - Calculate cost and add to cart
- `GET /api/cart` - View shopping cart
- `POST /api/receipt` - Generate receipt

### Port Configuration
- **RMI Registry**: 1099 (default)
- **Web Interface**: 8000 (default)
- **REST API**: Embedded with web server

## Development Notes
- The project uses Java 8 compatibility for broader system support
- RMI communication requires proper hostname configuration for remote access
- Web interface uses simple HTTP server for development purposes
- Shopping cart state is maintained server-side per client session

## References
- Tanenbaum. S Distributed Systems: Principles and Paradigms
- Github copilot