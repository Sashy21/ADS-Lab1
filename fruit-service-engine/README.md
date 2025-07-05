# Fruit Service Engine

## Overview
The Fruit Service Engine is a mobile distributed application that allows clients to interact with a server-side fruit price management system. The application supports various operations such as adding, updating, deleting fruit prices, calculating costs based on quantity, and generating receipts for transactions.

## Project Structure
The project is organized as follows:

```
fruit-service-engine
├── src
│   ├── main
│   │   └── java
│   │       ├── server
│   │       │   ├── FruitComputeEngine.java
│   │       │   ├── FruitComputeTaskRegistry.java
│   │       │   ├── tasks
│   │       │   │   ├── AddFruitPrice.java
│   │       │   │   ├── UpdateFruitPrice.java
│   │       │   │   ├── DeleteFruitPrice.java
│   │       │   │   ├── CalFruitCost.java
│   │       │   │   └── CalculateCost.java
│   │       │   ├── interfaces
│   │       │   │   ├── Compute.java
│   │       │   │   └── Task.java
│   │       │   └── servlets
│   │       │       ├── AddFruitServlet.java
│   │       │       ├── UpdateFruitServlet.java
│   │       │       ├── DeleteFruitServlet.java
│   │       │       ├── CalculateCostServlet.java
│   │       │       └── ReceiptServlet.java
│   │       ├── client
│   │       │   └── FruitClient.java
│   │       └── model
│   │           ├── FruitPrice.java
│   │           └── Receipt.java
│   └── webapp
│       └── WEB-INF
│           └── web.xml
├── pom.xml
└── README.md
```

## Features
1. **Add Fruit Price**: Clients can add new fruit price entries to the database.
2. **Update Fruit Price**: Clients can update existing fruit price entries.
3. **Delete Fruit Price**: Clients can delete fruit price entries from the database.
4. **Calculate Fruit Cost**: Clients can query the price of a fruit and calculate the total cost based on the quantity.
5. **Generate Receipt**: Clients can receive a detailed receipt of their transactions, including costs, amounts given, change due, and cashier information.

## Setup Instructions
1. **Prerequisites**: Ensure you have Java Development Kit (JDK) and Apache Maven installed on your machine.
2. **Clone the Repository**: Clone this repository to your local machine.
3. **Build the Project**: Navigate to the project directory and run `mvn clean install` to build the project.
4. **Start RMI Registry**: Start the RMI registry on port 1099 using the command `rmiregistry 1099` in the terminal.
5. **Run the Server**: Execute the `FruitComputeEngine` class to start the server.
6. **Run the Client**: Execute the `FruitClient` class to interact with the server.

## Usage
- Use the client application to perform various tasks related to fruit prices.
- The client will communicate with the server via RMI to execute tasks and retrieve results.

## License
This project is licensed under the MIT License. See the LICENSE file for more details.