package client;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Scanner;

import model.FruitPrice;
import server.FruitComputeTaskRegistry;
import server.interfaces.Compute;

public class FruitClient {
    private Compute computeEngine;
    private FruitComputeTaskRegistry taskRegistry;

    public FruitClient(String host, int port) {
        try {
            Registry registry = LocateRegistry.getRegistry(host, port);
            computeEngine = (Compute) registry.lookup("FruitComputeEngine");
            // Create a single instance of taskRegistry to maintain shopping cart state
            taskRegistry = new FruitComputeTaskRegistry(computeEngine);
        } catch (RemoteException e) {
            System.err.println("RMI Client exception: " + e.toString());
            e.printStackTrace();
        } catch (NotBoundException e) {
            System.err.println("Service not bound: " + e.toString());
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("General client exception: " + e.toString());
            e.printStackTrace();
        }
    }

    public void addFruitPrice(String fruitName, double price) {
        FruitPrice fruitPrice = new FruitPrice(fruitName, price);
        taskRegistry.addFruitPrice(fruitPrice);
    }

    public void updateFruitPrice(String fruitName, double newPrice) {
        FruitPrice fruitPrice = new FruitPrice(fruitName, newPrice);
        taskRegistry.updateFruitPrice(fruitPrice);
    }

    public void deleteFruitPrice(String fruitName) {
        taskRegistry.deleteFruitPrice(fruitName);
    }

    public void calculateFruitCost(String fruitName, int quantity) {
        taskRegistry.calculateFruitCost(fruitName, quantity);
    }

    public void viewCart() {
        taskRegistry.viewCart();
    }

    public void clearCart() {
        taskRegistry.clearCart();
    }

    public void printReceipt(String cashierName, double amountGiven) {
        taskRegistry.printReceipt(cashierName, amountGiven);
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
        // Get server details from user
        System.out.println("=== Fruit Service Client ===");
        System.out.print("Enter server hostname/IP (or press Enter for localhost): ");
        String serverHost = scanner.nextLine().trim();
        if (serverHost.isEmpty()) {
            serverHost = "localhost";
        }
        
        System.out.print("Enter server port (or press Enter for 1099): ");
        String portInput = scanner.nextLine().trim();
        int port = 1099;
        if (!portInput.isEmpty()) {
            try {
                port = Integer.parseInt(portInput);
            } catch (NumberFormatException e) {
                System.out.println("Invalid port, using default 1099");
            }
        }
        
        System.out.println("Connecting to server at " + serverHost + ":" + port);
        FruitClient client = new FruitClient(serverHost, port);
        
        if (client.computeEngine == null || client.taskRegistry == null) {
            System.err.println("Failed to connect to server. Please check:");
            System.err.println("1. Server is running on " + serverHost + ":" + port);
            System.err.println("2. Network connectivity between client and server");
            System.err.println("3. Firewall settings allow RMI communication");
            scanner.close();
            return;
        }
        
        System.out.println("Successfully connected to remote server!");
        
        while (true) {
            System.out.println("\n=== Fruit Service Client ===");
            System.out.println("Connected to: " + serverHost + ":" + port);
            System.out.println("1. Add Fruit Price");
            System.out.println("2. Update Fruit Price");
            System.out.println("3. Delete Fruit Price");
            System.out.println("4. Calculate Fruit Cost (Add to Cart)");
            System.out.println("5. View Shopping Cart");
            System.out.println("6. Clear Shopping Cart");
            System.out.println("7. Print Receipt");
            System.out.println("8. Exit");
            System.out.print("Choose option: ");
            
            int choice = scanner.nextInt();
            scanner.nextLine(); // consume newline
            
            switch (choice) {
                case 1:
                    System.out.print("Enter fruit name: ");
                    String addName = scanner.nextLine();
                    System.out.print("Enter price: ");
                    double price = scanner.nextDouble();
                    client.addFruitPrice(addName, price);
                    break;
                case 2:
                    System.out.print("Enter fruit name to update: ");
                    String updateName = scanner.nextLine();
                    System.out.print("Enter new price: ");
                    double newPrice = scanner.nextDouble();
                    client.updateFruitPrice(updateName, newPrice);
                    break;
                case 3:
                    System.out.print("Enter fruit name to delete: ");
                    String deleteName = scanner.nextLine();
                    client.deleteFruitPrice(deleteName);
                    break;
                case 4:
                    System.out.print("Enter fruit name: ");
                    String calcName = scanner.nextLine();
                    System.out.print("Enter quantity: ");
                    int quantity = scanner.nextInt();
                    client.calculateFruitCost(calcName, quantity);
                    break;
                case 5:
                    client.viewCart();
                    break;
                case 6:
                    client.clearCart();
                    break;
                case 7:
                    System.out.print("Enter cashier name: ");
                    String cashier = scanner.nextLine();
                    System.out.print("Enter amount given: ");
                    double amountGiven = scanner.nextDouble();
                    client.printReceipt(cashier, amountGiven);
                    break;
                case 8:
                    System.out.println("Exiting...");
                    scanner.close();
                    System.exit(0);
                    break;
                default:
                    System.out.println("Invalid option!");
            }
        }
    }
}