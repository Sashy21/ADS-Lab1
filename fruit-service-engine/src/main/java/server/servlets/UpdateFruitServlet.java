package server.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import server.FruitComputeTaskRegistry;
import server.tasks.UpdateFruitPrice;

@WebServlet("/updateFruit")
public class UpdateFruitServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String fruitName = request.getParameter("fruitName");
        String newPriceParam = request.getParameter("newPrice");
        
        try {
            double newPrice = Double.parseDouble(newPriceParam);
            
            UpdateFruitPrice updateTask = new UpdateFruitPrice(fruitName, newPrice);
            FruitComputeTaskRegistry taskRegistry = new FruitComputeTaskRegistry();
            
            String result = taskRegistry.runTask(updateTask);
            response.getWriter().write(result != null ? result : "Fruit price updated successfully.");
        } catch (NumberFormatException e) {
            response.getWriter().write("Error: Invalid price format - " + e.getMessage());
        } catch (Exception e) {
            response.getWriter().write("Error updating fruit price: " + e.getMessage());
        }
    }
}