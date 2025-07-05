package server.tasks;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import server.interfaces.Compute;
import server.interfaces.Task;

public class DeleteFruitPrice implements Task<String> {
    private final String fruitName;

    public DeleteFruitPrice(String fruitName) {
        this.fruitName = fruitName;
    }

    @Override
    public String execute() throws RemoteException {
        try {
            // Get the engine instance and delete the fruit price from its storage
            Registry registry = LocateRegistry.getRegistry("localhost", 1099);
            Compute engine = (Compute) registry.lookup("FruitComputeEngine");
            engine.deleteFruitPrice(fruitName);
            
            System.out.println("Deleting fruit price for: " + fruitName);
            return "Fruit price deleted successfully for " + fruitName;
        } catch (Exception e) {
            throw new RemoteException("Failed to delete fruit price", e);
        }
    }
}