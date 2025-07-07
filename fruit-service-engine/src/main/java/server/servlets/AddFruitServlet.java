package server.servlets;

import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.FruitPrice;
import server.FruitComputeEngine;
import server.tasks.AddFruitPrice;

// This servlet handles adding fruit prices to the remote compute engine
@WebServlet("/addFruit")
public class AddFruitServlet extends HttpServlet {
    private FruitComputeEngine computeEngine;

    @Override
    public void init() throws ServletException {
        try {
            Registry registry = LocateRegistry.getRegistry("localhost", 1099);
            computeEngine = (FruitComputeEngine) registry.lookup("FruitComputeEngine");
        } catch (Exception e) {
            throw new ServletException("Failed to connect to RMI registry", e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String fruitName = request.getParameter("fruitName");
        String priceParam = request.getParameter("price");
        
        try {
            double price = Double.parseDouble(priceParam);
            FruitPrice fruitPrice = new FruitPrice(fruitName, price);
            AddFruitPrice task = new AddFruitPrice(fruitPrice);
            
            String result = computeEngine.executeTask(task);
            response.getWriter().write(result);
        } catch (NumberFormatException e) {
            response.getWriter().write("Error: Invalid price format - " + e.getMessage());
        } catch (RemoteException e) {
            response.getWriter().write("Error adding fruit price: " + e.getMessage());
        } catch (Exception e) {
            response.getWriter().write("Unexpected error: " + e.getMessage());
        }
    }
}