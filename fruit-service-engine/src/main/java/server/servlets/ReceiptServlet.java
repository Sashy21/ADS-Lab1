package server.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.Receipt;

// This servlet handles the generation of a receipt after a transaction
@WebServlet("/receipt")
public class ReceiptServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            String cashier = request.getParameter("cashier");
            String totalCostParam = request.getParameter("totalCost");
            String amountGivenParam = request.getParameter("amountGiven");
            
            double totalCost = Double.parseDouble(totalCostParam);
            double amountGiven = Double.parseDouble(amountGivenParam);
            double changeDue = amountGiven - totalCost;

            Receipt receipt = new Receipt(totalCost, amountGiven, changeDue, cashier);
            
            // Here you would typically save the receipt to a database or perform further processing

            response.setContentType("application/json");
            // Create a simple JSON response manually
            String jsonResponse = String.format(
                "{\"totalCost\":%.2f,\"amountGiven\":%.2f,\"changeDue\":%.2f,\"cashier\":\"%s\"}",
                receipt.getTotalCost(), receipt.getAmountGiven(), receipt.getChangeDue(), receipt.getCashier()
            );
            response.getWriter().write(jsonResponse);
        } catch (NumberFormatException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("{\"error\":\"Invalid number format: " + e.getMessage() + "\"}");
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"error\":\"Server error: " + e.getMessage() + "\"}");
        }
    }
}