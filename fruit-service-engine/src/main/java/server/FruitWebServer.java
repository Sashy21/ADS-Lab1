package server;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.file.Files;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.HashMap;
import java.util.Map;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import model.FruitPrice;
import server.tasks.AddFruitPrice;
import server.tasks.CalculateCost;
import server.tasks.DeleteFruitPrice;
import server.tasks.UpdateFruitPrice;

public class FruitWebServer {
    private static final int PORT = 8080;
    private static FruitComputeEngine computeEngine;
    private static FruitComputeTaskRegistry taskRegistry;

    public static void main(String[] args) throws Exception {
        // Initialize RMI connection
        try {
            Registry registry = LocateRegistry.getRegistry("localhost", 1099);
            computeEngine = (FruitComputeEngine) registry.lookup("FruitComputeEngine");
            taskRegistry = new FruitComputeTaskRegistry();
            System.out.println("Connected to RMI server successfully!");
        } catch (Exception e) {
            System.err.println("Failed to connect to RMI server. Make sure the RMI server is running.");
            System.err.println("Error: " + e.getMessage());
            return;
        }

        // Create HTTP server
        HttpServer server = HttpServer.create(new InetSocketAddress(PORT), 0);
        
        // Add handlers
        server.createContext("/", new StaticFileHandler());
        server.createContext("/addFruit", new AddFruitHandler());
        server.createContext("/updateFruit", new UpdateFruitHandler());
        server.createContext("/deleteFruit", new DeleteFruitHandler());
        server.createContext("/calculateCost", new CalculateCostHandler());
        
        server.setExecutor(null);
        server.start();
        
        System.out.println("========================================");
        System.out.println("Fruit Web Server started successfully!");
        System.out.println("Web Interface: http://localhost:" + PORT);
        System.out.println("========================================");
        System.out.println("Available endpoints:");
        System.out.println("  GET  /              - Web Interface");
        System.out.println("  POST /addFruit      - Add new fruit");
        System.out.println("  POST /updateFruit   - Update fruit price");
        System.out.println("  POST /deleteFruit   - Delete fruit");
        System.out.println("  POST /calculateCost - Calculate cost");
        System.out.println("========================================");
    }

    static class StaticFileHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String path = exchange.getRequestURI().getPath();
            if (path.equals("/") || path.equals("/index.html")) {
                path = "/index.html";
            }
            
            try {
                // Try to read the file from webapp directory
                String filePath = "src/webapp" + path;
                File file = new File(filePath);
                
                if (file.exists() && file.isFile()) {
                    String contentType = getContentType(path);
                    exchange.getResponseHeaders().set("Content-Type", contentType);
                    
                    byte[] content = Files.readAllBytes(file.toPath());
                    exchange.sendResponseHeaders(200, content.length);
                    OutputStream os = exchange.getResponseBody();
                    os.write(content);
                    os.close();
                } else {
                    // File not found
                    String notFound = "File not found: " + path;
                    exchange.sendResponseHeaders(404, notFound.length());
                    OutputStream os = exchange.getResponseBody();
                    os.write(notFound.getBytes());
                    os.close();
                }
            } catch (Exception e) {
                String error = "Error serving file: " + e.getMessage();
                exchange.sendResponseHeaders(500, error.length());
                OutputStream os = exchange.getResponseBody();
                os.write(error.getBytes());
                os.close();
            }
        }
        
        private String getContentType(String path) {
            if (path.endsWith(".html")) return "text/html";
            if (path.endsWith(".css")) return "text/css";
            if (path.endsWith(".js")) return "application/javascript";
            if (path.endsWith(".json")) return "application/json";
            return "text/plain";
        }
    }

    static class AddFruitHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if (!"POST".equals(exchange.getRequestMethod())) {
                exchange.sendResponseHeaders(405, 0);
                exchange.getResponseBody().close();
                return;
            }

            try {
                Map<String, String> params = parseFormData(exchange);
                String fruitName = params.get("fruitName");
                String priceStr = params.get("price");
                
                if (fruitName == null || priceStr == null) {
                    sendResponse(exchange, "Error: Missing parameters");
                    return;
                }
                
                double price = Double.parseDouble(priceStr);
                FruitPrice fruitPrice = new FruitPrice(fruitName, price);
                AddFruitPrice task = new AddFruitPrice(fruitPrice);
                
                String result = computeEngine.executeTask(task);
                sendResponse(exchange, result);
                
            } catch (NumberFormatException e) {
                sendResponse(exchange, "Error: Invalid price format");
            } catch (Exception e) {
                sendResponse(exchange, "Error: " + e.getMessage());
            }
        }
    }

    static class UpdateFruitHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if (!"POST".equals(exchange.getRequestMethod())) {
                exchange.sendResponseHeaders(405, 0);
                exchange.getResponseBody().close();
                return;
            }

            try {
                Map<String, String> params = parseFormData(exchange);
                String fruitName = params.get("fruitName");
                String priceStr = params.get("price");
                
                if (fruitName == null || priceStr == null) {
                    sendResponse(exchange, "Error: Missing parameters");
                    return;
                }
                
                double price = Double.parseDouble(priceStr);
                UpdateFruitPrice task = new UpdateFruitPrice(fruitName, price);
                
                String result = computeEngine.executeTask(task);
                sendResponse(exchange, result);
                
            } catch (NumberFormatException e) {
                sendResponse(exchange, "Error: Invalid price format");
            } catch (Exception e) {
                sendResponse(exchange, "Error: " + e.getMessage());
            }
        }
    }

    static class DeleteFruitHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if (!"POST".equals(exchange.getRequestMethod())) {
                exchange.sendResponseHeaders(405, 0);
                exchange.getResponseBody().close();
                return;
            }

            try {
                Map<String, String> params = parseFormData(exchange);
                String fruitName = params.get("fruitName");
                
                if (fruitName == null) {
                    sendResponse(exchange, "Error: Missing fruit name");
                    return;
                }
                
                DeleteFruitPrice task = new DeleteFruitPrice(fruitName);
                String result = computeEngine.executeTask(task);
                sendResponse(exchange, result);
                
            } catch (Exception e) {
                sendResponse(exchange, "Error: " + e.getMessage());
            }
        }
    }

    static class CalculateCostHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if (!"POST".equals(exchange.getRequestMethod())) {
                exchange.sendResponseHeaders(405, 0);
                exchange.getResponseBody().close();
                return;
            }

            try {
                Map<String, String> params = parseFormData(exchange);
                String fruitName = params.get("fruitName");
                String quantityStr = params.get("quantity");
                String amountGivenStr = params.get("amountGiven");
                String cashier = params.get("cashier");
                
                if (fruitName == null || quantityStr == null || amountGivenStr == null || cashier == null) {
                    sendResponse(exchange, "Error: Missing parameters");
                    return;
                }
                
                int quantity = Integer.parseInt(quantityStr);
                double amountGiven = Double.parseDouble(amountGivenStr);
                
                CalculateCost task = new CalculateCost(fruitName, quantity, amountGiven, cashier);
                model.Receipt receipt = taskRegistry.runTask(task);
                
                if (receipt != null) {
                    sendResponse(exchange, receipt.toString());
                } else {
                    sendResponse(exchange, "Error: Unable to calculate cost");
                }
                
            } catch (NumberFormatException e) {
                sendResponse(exchange, "Error: Invalid number format");
            } catch (Exception e) {
                sendResponse(exchange, "Error: " + e.getMessage());
            }
        }
    }

    private static Map<String, String> parseFormData(HttpExchange exchange) throws IOException {
        Map<String, String> params = new HashMap<>();
        
        InputStream is = exchange.getRequestBody();
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder body = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            body.append(line);
        }
        
        String[] pairs = body.toString().split("&");
        for (String pair : pairs) {
            String[] keyValue = pair.split("=", 2);
            if (keyValue.length == 2) {
                String key = java.net.URLDecoder.decode(keyValue[0], "UTF-8");
                String value = java.net.URLDecoder.decode(keyValue[1], "UTF-8");
                params.put(key, value);
            }
        }
        
        return params;
    }

    private static void sendResponse(HttpExchange exchange, String response) throws IOException {
        exchange.getResponseHeaders().set("Content-Type", "text/plain; charset=UTF-8");
        exchange.getResponseHeaders().set("Access-Control-Allow-Origin", "*");
        exchange.sendResponseHeaders(200, response.getBytes("UTF-8").length);
        OutputStream os = exchange.getResponseBody();
        os.write(response.getBytes("UTF-8"));
        os.close();
    }
}
