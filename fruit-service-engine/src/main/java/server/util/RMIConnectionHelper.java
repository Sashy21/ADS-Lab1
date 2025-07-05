package server.util;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import server.FruitComputeEngine;

public class RMIConnectionHelper {
    private static final String DEFAULT_HOST = "localhost";
    private static final int DEFAULT_PORT = 1099;
    private static final String SERVICE_NAME = "FruitComputeEngine";
    
    public static FruitComputeEngine getEngineConnection() throws Exception {
        return getEngineConnection(DEFAULT_HOST, DEFAULT_PORT);
    }
    
    public static FruitComputeEngine getEngineConnection(String host, int port) throws Exception {
        Registry registry = LocateRegistry.getRegistry(host, port);
        return (FruitComputeEngine) registry.lookup(SERVICE_NAME);
    }
}
