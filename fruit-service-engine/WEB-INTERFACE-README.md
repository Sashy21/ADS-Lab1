# Fruit Service Web Interface

## Overview
This web interface provides a modern, user-friendly way to interact with the Fruit Service system. It offers a complete graphical user interface for managing fruit inventory and calculating costs.

## Architecture
The web-enabled fruit service consists of three components:

1. **RMI Server** (Port 1099) - Core business logic and data storage
2. **REST Bridge** (Port 8081) - Web API gateway that translates HTTP requests to RMI calls
3. **Web Interface** (Port 8000) - Modern HTML5/JavaScript frontend

## Features

### 🍎 Fruit Management
- **Add New Fruits**: Add fruits with their prices to the inventory
- **Update Prices**: Modify existing fruit prices
- **Delete Fruits**: Remove fruits from the inventory

### 🛒 Shopping Cart
- **Add Items**: Add multiple fruits with quantities to your cart
- **View Cart**: See all items in your cart with quantities
- **Remove Items**: Remove individual items from the cart

### 🧾 Cost Calculator & Receipt Generator
- **Calculate Total**: Get the total cost for all items in cart
- **Generate Receipt**: Create detailed receipts with:
  - Itemized costs
  - Total amount
  - Amount given
  - Change due
  - Cashier information
  - Timestamp

### 📊 System Monitoring
- **Server Status**: Check if all components are running
- **Real-time Feedback**: Immediate response to all operations

## How to Run

### Quick Start
1. Run the startup script:
   ```batch
   start-web-service.bat
   ```

2. The script will:
   - Compile all Java sources
   - Start the RMI server
   - Start the REST bridge
   - Start the web server
   - Open your browser automatically

3. Access the web interface at: http://localhost:8000

### Manual Start (if needed)
If you prefer to start components manually:

1. **Compile the project:**
   ```batch
   compile.bat
   ```

2. **Start RMI Server:**
   ```batch
   java -cp target\classes server.FruitComputeEngine
   ```

3. **Start REST Bridge:**
   ```batch
   java -cp target\classes server.FruitRestBridge
   ```

4. **Start Web Server:**
   ```batch
   cd src\webapp
   python -m http.server 8000
   ```

## Usage Guide

### Adding Fruits
1. Go to the "Add New Fruit" section
2. Enter fruit name and price
3. Click "Add Fruit"
4. Success message will appear

### Shopping and Cost Calculation
1. Use "Add Items to Cart" to add fruits with quantities
2. View your cart items in the "Cart Items" section
3. Enter cashier name and amount given
4. Click "Calculate & Generate Receipt"
5. View the detailed receipt

### Managing Inventory
1. Use "Update Fruit Price" to modify existing prices
2. Use "Delete Fruit" to remove items from inventory

## Network Access
- **Local Access**: http://localhost:8000
- **Network Access**: Replace `localhost` with your computer's IP address
- **Multi-Computer**: All other computers can access using your IP

## Troubleshooting

### Common Issues
1. **"Server not accessible"**: Make sure the RMI server is running first
2. **"Port already in use"**: Close any existing instances or change ports
3. **"Python not found"**: Install Python 3.x or use an alternative web server

### Port Configuration
- RMI Server: 1099 (configurable in FruitComputeEngine.java)
- REST Bridge: 8081 (configurable in FruitRestBridge.java)
- Web Server: 8000 (configurable in start script)

### System Requirements
- Java 8 or higher
- Python 3.x (for web server)
- Modern web browser with JavaScript enabled

## Technology Stack
- **Backend**: Java RMI, Custom REST Bridge
- **Frontend**: HTML5, CSS3, JavaScript (ES6+)
- **Web Server**: Python HTTP Server
- **Communication**: RESTful API over HTTP

## File Structure
```
src/
├── webapp/
│   ├── index.html          # Main web interface
│   └── WEB-INF/
│       └── web.xml         # Servlet configuration
├── main/java/
│   └── server/
│       ├── FruitComputeEngine.java    # RMI server
│       ├── FruitRestBridge.java       # REST API bridge
│       └── ...
└── ...
```

## Security Notes
- This is a demonstration system for local/educational use
- For production use, consider adding authentication and HTTPS
- CORS is enabled for development convenience

## Browser Compatibility
- Chrome 60+
- Firefox 55+
- Safari 11+
- Edge 16+

Enjoy your modern fruit service management system! 🎉
