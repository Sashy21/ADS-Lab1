package server.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.Receipt;
import server.FruitComputeTaskRegistry;
import server.tasks.CalculateCost;

// This servlet handles the calculation of fruit costs based on user input
@WebServlet("/calculateCost")
public class CalculateCostServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String fruitName = request.getParameter("fruitName");
        String quantityParam = request.getParameter("quantity");
        String amountGivenParam = request.getParameter("amountGiven");
        String cashier = request.getParameter("cashier");

        try {
            int quantity = Integer.parseInt(quantityParam);
            double amountGiven = Double.parseDouble(amountGivenParam);
            
            CalculateCost task = new CalculateCost(fruitName, quantity, amountGiven, cashier);
            FruitComputeTaskRegistry taskRegistry = new FruitComputeTaskRegistry();
            Receipt receipt = taskRegistry.runTask(task);

            response.setContentType("text/plain");
            if (receipt != null) {
                response.getWriter().write(receipt.toString());
            } else {
                response.getWriter().write("Error: Unable to calculate cost");
            }
        } catch (NumberFormatException e) {
            try {
                response.getWriter().write("Error: Invalid number format - " + e.getMessage());
            } catch (IOException ioException) {
                throw new ServletException("Failed to write error response", ioException);
            }
        } catch (Exception e) {
            try {
                response.getWriter().write("Error calculating cost: " + e.getMessage());
            } catch (IOException ioException) {
                throw new ServletException("Failed to write error response", ioException);
            }
        }
    }
}