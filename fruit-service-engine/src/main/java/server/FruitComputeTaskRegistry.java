package server;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import model.FruitPrice;
import server.interfaces.Compute;
import server.interfaces.Task;

public class FruitComputeTaskRegistry {

    private Compute computeEngine;
    // Add shopping cart to track purchases
    private List<CartItem> shoppingCart;
    private double totalCost;

    // Inner class to represent items in the shopping cart
    private static class CartItem {
        String fruitName;
        int quantity;
        double unitPrice;
        double totalPrice;

        CartItem(String fruitName, int quantity, double unitPrice) {
            this.fruitName = fruitName;
            this.quantity = quantity;
            this.unitPrice = unitPrice;
            this.totalPrice = unitPrice * quantity;
        }

        @Override
        public String toString() {
            return String.format("%s x %d @ $%.2f = $%.2f", fruitName, quantity, unitPrice, totalPrice);
        }
    }

    public FruitComputeTaskRegistry() {
        this.shoppingCart = new ArrayList<>();
        this.totalCost = 0.0;
        try {
            Registry registry = LocateRegistry.getRegistry("localhost", 1099);
            computeEngine = (Compute) registry.lookup("FruitComputeEngine");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public FruitComputeTaskRegistry(Compute computeEngine) {
        this.computeEngine = computeEngine;
        this.shoppingCart = new ArrayList<>();
        this.totalCost = 0.0;
    }

    public void executeTask(Task task) {
        try {
            computeEngine.executeTask(task);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public <T> T runTask(Task<T> task) {
        try {
            if (computeEngine != null) {
                return computeEngine.executeTask(task);
            }
            return null;
        } catch (RemoteException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void addFruitPrice(FruitPrice fruitPrice) {
        try {
            if (computeEngine != null) {
                computeEngine.addFruitPrice(fruitPrice.getFruitName(), fruitPrice.getPrice());
            }
            System.out.println("Successfully added fruit price: " + fruitPrice.getFruitName() + " - Ksh" + fruitPrice.getPrice());
        } catch (RemoteException e) {
            System.err.println("Error adding fruit price: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void updateFruitPrice(FruitPrice fruitPrice) {
        try {
            if (computeEngine != null) {
                computeEngine.updateFruitPrice(fruitPrice.getFruitName(), fruitPrice.getPrice());
            }
            System.out.println("Successfully updated fruit price: " + fruitPrice.getFruitName() + " - Ksh" + fruitPrice.getPrice());
        } catch (RemoteException e) {
            System.err.println("Error updating fruit price: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void deleteFruitPrice(String fruitName) {
        try {
            if (computeEngine != null) {
                computeEngine.deleteFruitPrice(fruitName);
            }
            System.out.println("Successfully deleted fruit price for: " + fruitName);
        } catch (RemoteException e) {
            System.err.println("Error deleting fruit price: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void calculateFruitCost(String fruitName, int quantity) {
        try {
            double itemCost = 0.0;
            double unitPrice = 0.0;
            if (computeEngine != null) {
                itemCost = computeEngine.calculateFruitCost(fruitName, quantity);
                // Calculate unit price for cart
                unitPrice = itemCost / quantity;
            }
            
            if (itemCost > 0) {
                // Add item to shopping cart
                CartItem item = new CartItem(fruitName, quantity, unitPrice);
                shoppingCart.add(item);
                totalCost += itemCost;
                
                System.out.println("Added to cart: " + item);
                System.out.println("Cart total: Ksh" + String.format("%.2f", totalCost));
            } else {
                System.out.println("Could not add item to cart - fruit not found or price is 0");
            }
        } catch (RemoteException e) {
            System.err.println("Error calculating fruit cost: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void printReceipt(String cashierName, double amountGiven) {
        if (shoppingCart.isEmpty()) {
            System.out.println("Shopping cart is empty! Add items before printing receipt.");
            return;
        }
        
        // Add detailed receipt with cart items
        StringBuilder detailedReceipt = new StringBuilder();
        detailedReceipt.append("===== FRUIT STORE RECEIPT =====\n");
        detailedReceipt.append("Cashier: ").append(cashierName).append("\n");
        detailedReceipt.append("==============================\n");
        detailedReceipt.append("ITEMS PURCHASED:\n");
        
        for (CartItem item : shoppingCart) {
            detailedReceipt.append(item.toString()).append("\n");
        }
        
        detailedReceipt.append("==============================\n");
        detailedReceipt.append("Total Cost: Ksh").append(String.format("%.2f", this.totalCost)).append("\n");
        detailedReceipt.append("Amount Given: Ksh").append(String.format("%.2f", amountGiven)).append("\n");
        detailedReceipt.append("Change: Ksh").append(String.format("%.2f", amountGiven - this.totalCost)).append("\n");
        detailedReceipt.append("==============================\n");
        detailedReceipt.append("Thank you for your purchase!\n");
        
        System.out.println("Receipt printed successfully!");
        System.out.println(detailedReceipt.toString());
        
        // Clear the shopping cart after printing receipt
        clearCart();
    }
    
    public void clearCart() {
        shoppingCart.clear();
        totalCost = 0.0;
        System.out.println("Shopping cart cleared.");
    }
    
    public void viewCart() {
        if (shoppingCart.isEmpty()) {
            System.out.println("Shopping cart is empty.");
            return;
        }
        
        System.out.println("\n===== SHOPPING CART =====");
        for (CartItem item : shoppingCart) {
            System.out.println(item.toString());
        }
        System.out.println("=========================");
        System.out.println("Total: Ksh" + String.format("%.2f", totalCost));
    }

    public Map<String, Object> getFruitPrices() {
        Map<String, Object> fruitPrices = new HashMap<>();
        // Logic to retrieve fruit prices from the fruitComputeEngine can be added here
        return fruitPrices;
    }
}