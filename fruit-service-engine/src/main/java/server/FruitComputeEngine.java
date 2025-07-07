package server;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import server.interfaces.Compute;
import server.interfaces.Task;

// This class implements the Compute interface and provides methods to manage fruit prices,
// calculate costs, and generate receipts. It acts as the RMI server for the fruit service
public class FruitComputeEngine extends UnicastRemoteObject implements Compute {
    private final Map<String, Double> fruitPriceTable;

    protected FruitComputeEngine() throws RemoteException {
        super();
        fruitPriceTable = new HashMap<>();
    }

    public static void main(String[] args) {
        try {
            // Set system properties for RMI hostname
            String hostname = System.getProperty("java.rmi.server.hostname");
            if (hostname == null) {
                // Get local IP address for multi-computer access
                hostname = java.net.InetAddress.getLocalHost().getHostAddress();
                System.setProperty("java.rmi.server.hostname", hostname);
            }
            
            FruitComputeEngine engine = new FruitComputeEngine();
            Registry registry = LocateRegistry.createRegistry(1099);
            registry.rebind("FruitComputeEngine", engine);
            
            System.out.println("========================================");
            System.out.println("Fruit Compute Engine is ready!");
            System.out.println("Server hostname: " + hostname);
            System.out.println("RMI Registry port: 1099");
            System.out.println("Service name: FruitComputeEngine");
            System.out.println("========================================");
            System.out.println("Clients can connect from other computers using:");
            System.out.println("  Server IP: " + hostname);
            System.out.println("  Port: 1099");
            System.out.println("========================================");
        } catch (RemoteException e) {
            System.err.println("RMI Server exception: " + e.toString());
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("General server exception: " + e.toString());
            e.printStackTrace();
        }
    }

    @Override
    public <T> T executeTask(Task<T> task) throws RemoteException {
        try {
            return task.execute();
        } catch (Exception e) {
            throw new RemoteException("Task execution failed", e);
        }
    }

    @Override
    public void addFruitPrice(String fruitName, double price) throws RemoteException {
        fruitPriceTable.put(fruitName, price);
        System.out.println("Added: " + fruitName + " - Ksh" + price);
    }

    @Override
    public void updateFruitPrice(String fruitName, double newPrice) throws RemoteException {
        if (fruitPriceTable.containsKey(fruitName)) {
            fruitPriceTable.put(fruitName, newPrice);
            System.out.println("Updated: " + fruitName + " - Ksh" + newPrice);
        } else {
            System.out.println("Fruit not found: " + fruitName);
        }
    }

    @Override
    public void deleteFruitPrice(String fruitName) throws RemoteException {
        if (fruitPriceTable.remove(fruitName) != null) {
            System.out.println("Deleted: " + fruitName);
        } else {
            System.out.println("Fruit not found: " + fruitName);
        }
    }

    @Override
    public double calculateFruitCost(String fruitName, int quantity) throws RemoteException {
        double price = fruitPriceTable.getOrDefault(fruitName, 0.0);
        if (price == 0.0) {
            System.out.println("Fruit not found: " + fruitName);
            return 0.0;
        }
        double totalCost = price * quantity;
        System.out.println("Calculated cost for " + quantity + " " + fruitName + "(s): Ksh" + totalCost);
        return totalCost;
    }

    @Override
    public String generateReceipt(String cashierName, double totalCost, double amountGiven) throws RemoteException {
        double change = amountGiven - totalCost;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String timestamp = sdf.format(new Date());
        
        StringBuilder receipt = new StringBuilder();
        receipt.append("===== FRUIT STORE RECEIPT =====\n");
        receipt.append("Date: ").append(timestamp).append("\n");
        receipt.append("Cashier: ").append(cashierName).append("\n");
        receipt.append("==============================\n");
        receipt.append("Total Cost: Ksh").append(String.format("%.2f", totalCost)).append("\n");
        receipt.append("Amount Given: Ksh").append(String.format("%.2f", amountGiven)).append("\n");
        receipt.append("Change: Ksh").append(String.format("%.2f", change)).append("\n");
        receipt.append("==============================\n");
        receipt.append("Thank you for your purchase!\n");
        
        return receipt.toString();
    }
}