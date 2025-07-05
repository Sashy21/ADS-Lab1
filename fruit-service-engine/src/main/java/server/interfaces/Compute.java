package server.interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Compute extends Remote {
    <T> T executeTask(Task<T> task) throws RemoteException;
    void addFruitPrice(String fruitName, double price) throws RemoteException;
    void updateFruitPrice(String fruitName, double newPrice) throws RemoteException;
    void deleteFruitPrice(String fruitName) throws RemoteException;
    double calculateFruitCost(String fruitName, int quantity) throws RemoteException;
    String generateReceipt(String cashierName, double totalCost, double amountGiven) throws RemoteException;
}