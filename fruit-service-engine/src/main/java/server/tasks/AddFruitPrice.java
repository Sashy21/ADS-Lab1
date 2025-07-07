package server.tasks;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import model.FruitPrice;
import server.interfaces.Compute;
import server.interfaces.Task;

// This task is responsible for adding a fruit price to the remote compute engine
public class AddFruitPrice implements Task<String> {
    private FruitPrice fruitPrice;

    public AddFruitPrice(FruitPrice fruitPrice) {
        this.fruitPrice = fruitPrice;
    }

    @Override
    public String execute() throws RemoteException {
        try {
            // Get the engine instance and add the fruit price to its storage
            Registry registry = LocateRegistry.getRegistry("localhost", 1099);
            Compute engine = (Compute) registry.lookup("FruitComputeEngine");
            engine.addFruitPrice(fruitPrice.getFruitName(), fruitPrice.getPrice());
            
            System.out.println("Adding fruit price: " + fruitPrice.getFruitName() + " - Ksh" + fruitPrice.getPrice());
            return "Fruit price added successfully for " + fruitPrice.getFruitName() + " - Ksh" + fruitPrice.getPrice();
        } catch (Exception e) {
            throw new RemoteException("Failed to add fruit price", e);
        }
    }
}