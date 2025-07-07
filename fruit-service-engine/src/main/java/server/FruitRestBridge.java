package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URLDecoder;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import server.interfaces.Compute;

// FruitRestBridge is a simple HTTP server that acts as a bridge between a RESTful API and an RMI-based compute engine.
// It listens for HTTP requests, processes them, and forwards the necessary actions to the RMI compute engine.
// It supports CORS for web interface compatibility and handles various fruit-related operations
public class FruitRestBridge {
    private static final int PORT = 8081;
    private static Compute computeEngine;

    public static void main(String[] args) throws Exception {
        // Initialize RMI connection
        try {
            Registry registry = LocateRegistry.getRegistry("localhost", 1099);
            computeEngine = (Compute) registry.lookup("FruitComputeEngine");
            System.out.println("Connected to RMI server successfully!");
        } catch (Exception e) {
            System.err.println("Failed to connect to RMI server. Make sure the RMI server is running.");
            System.err.println("Error: " + e.getMessage());
            return;
        }

        // Create simple HTTP server
        ServerSocket serverSocket = new ServerSocket(PORT);
        System.out.println("========================================");
        System.out.println("Fruit REST Bridge started on port " + PORT);
        System.out.println("CORS enabled for web interface");
        System.out.println("========================================");

        while (true) {
            Socket clientSocket = serverSocket.accept();
            new Thread(() -> handleRequest(clientSocket)).start();
        }
    }

    private static void handleRequest(Socket clientSocket) {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
             PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)) {

            // Read HTTP request
            String requestLine = in.readLine();
            if (requestLine == null) return;

            String[] requestParts = requestLine.split(" ");
            String method = requestParts[0];
            String path = requestParts[1];

            // Read headers
            String line;
            int contentLength = 0;
            while ((line = in.readLine()) != null && !line.isEmpty()) {
                if (line.startsWith("Content-Length:")) {
                    contentLength = Integer.parseInt(line.substring(16).trim());
                }
            }

            // Read POST body if present
            String body = "";
            if ("POST".equals(method) && contentLength > 0) {
                char[] buffer = new char[contentLength];
                in.read(buffer, 0, contentLength);
                body = new String(buffer);
            }

            // Process request
            String response = processRequest(method, path, body);

            // Send HTTP response with CORS headers
            out.println("HTTP/1.1 200 OK");
            out.println("Content-Type: text/plain; charset=UTF-8");
            out.println("Access-Control-Allow-Origin: *");
            out.println("Access-Control-Allow-Methods: GET, POST, OPTIONS");
            out.println("Access-Control-Allow-Headers: Content-Type");
            out.println("Content-Length: " + response.getBytes("UTF-8").length);
            out.println();
            out.print(response);

        } catch (Exception e) {
            System.err.println("Error handling request: " + e.getMessage());
        } finally {
            try {
                clientSocket.close();
            } catch (IOException e) {
                // Ignore
            }
        }
    }

    private static String processRequest(String method, String path, String body) {
        try {
            if ("OPTIONS".equals(method)) {
                return ""; // Handle CORS preflight
            }

            if (!"/api".equals(path)) {
                return "Error: Invalid endpoint";
            }

            // Parse form data from body
            String[] params = body.split("&");
            String action = null;
            String fruitName = null;
            String price = null;
            String quantity = null;
            String amountGiven = null;
            String cashier = null;

            for (String param : params) {
                String[] keyValue = param.split("=", 2);
                if (keyValue.length == 2) {
                    String key = URLDecoder.decode(keyValue[0], "UTF-8");
                    String value = URLDecoder.decode(keyValue[1], "UTF-8");
                    
                    switch (key) {
                        case "action": action = value; break;
                        case "fruitName": fruitName = value; break;
                        case "price": price = value; break;
                        case "quantity": quantity = value; break;
                        case "amountGiven": amountGiven = value; break;
                        case "cashier": cashier = value; break;
                    }
                }
            }

            if (action == null) {
                return "Error: No action specified";
            }

            switch (action) {
                case "addFruit":
                    if (fruitName == null || price == null) {
                        return "Error: Missing fruit name or price";
                    }
                    double priceVal = Double.parseDouble(price);
                    computeEngine.addFruitPrice(fruitName, priceVal);
                    return "Fruit price added successfully for " + fruitName + " - Ksh" + priceVal;

                case "updateFruit":
                    if (fruitName == null || price == null) {
                        return "Error: Missing fruit name or price";
                    }
                    double newPriceVal = Double.parseDouble(price);
                    computeEngine.updateFruitPrice(fruitName, newPriceVal);
                    return "Fruit price updated successfully for " + fruitName + " to Ksh" + newPriceVal;

                case "deleteFruit":
                    if (fruitName == null) {
                        return "Error: Missing fruit name";
                    }
                    computeEngine.deleteFruitPrice(fruitName);
                    return "Fruit price deleted successfully for " + fruitName;

                case "calculateCost":
                    if (fruitName == null || quantity == null || amountGiven == null || cashier == null) {
                        return "Error: Missing required parameters";
                    }
                    int quantityVal = Integer.parseInt(quantity);
                    double amountGivenVal = Double.parseDouble(amountGiven);
                    
                    // Calculate cost directly using the engine
                    double totalCost = computeEngine.calculateFruitCost(fruitName, quantityVal);
                    if (totalCost <= 0) {
                        return "Error: Fruit not found or invalid price";
                    }
                    
                    // Generate receipt directly using the engine
                    String receiptText = computeEngine.generateReceipt(cashier, totalCost, amountGivenVal);
                    return receiptText;

                default:
                    return "Error: Unknown action: " + action;
            }

        } catch (NumberFormatException e) {
            return "Error: Invalid number format";
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }
}
