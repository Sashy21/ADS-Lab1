package server.tasks;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import model.Receipt;
import server.interfaces.Compute;
import server.interfaces.Task;

// This task is responsible for calculating the cost of fruits based on the quantity and fruit name
//combined with CalFruitcost
// It also generates a receipt with the total cost, amount given, and change due
public class CalculateCost implements Task<Receipt> {
    private final String fruitName;
    private final int quantity;
    private final double amountGiven;
    private final String cashier;
    private double totalCost;

    // Constructor matching the servlet call
    public CalculateCost(String fruitName, int quantity, double amountGiven, String cashier) {
        this.fruitName = fruitName;
        this.quantity = quantity;
        this.amountGiven = amountGiven;
        this.cashier = cashier;
        this.totalCost = 0; // Will be calculated in execute
    }

    // Original constructor for backward compatibility
    public CalculateCost(String cashier, double totalCost, double amountGiven) {
        this.cashier = cashier;
        this.totalCost = totalCost;
        this.amountGiven = amountGiven;
        this.fruitName = null;
        this.quantity = 0;
    }

    @Override
    public Receipt execute() throws RemoteException {
        // If totalCost is 0, we need to calculate it based on fruit name and quantity
        if (totalCost == 0 && fruitName != null) {
            try {
                // Get actual fruit price from the engine's storage
                Registry registry = LocateRegistry.getRegistry("localhost", 1099);
                Compute engine = (Compute) registry.lookup("FruitComputeEngine");
                totalCost = engine.calculateFruitCost(fruitName, quantity);
            } catch (Exception e) {
                throw new RemoteException("Failed to calculate fruit cost", e);
            }
        }
        
        double changeDue = amountGiven - totalCost;
        return new Receipt(totalCost, amountGiven, changeDue, cashier);
    }
}