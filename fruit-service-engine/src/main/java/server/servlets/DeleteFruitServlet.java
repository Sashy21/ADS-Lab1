package server.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import server.FruitComputeTaskRegistry;
import server.tasks.DeleteFruitPrice;

@WebServlet("/deleteFruit")
public class DeleteFruitServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String fruitName = request.getParameter("fruitName");

        if (fruitName == null || fruitName.isEmpty()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Fruit name is required");
            return;
        }

        DeleteFruitPrice deleteTask = new DeleteFruitPrice(fruitName);
        FruitComputeTaskRegistry taskRegistry = new FruitComputeTaskRegistry();
        String result = taskRegistry.runTask(deleteTask);

        if (result != null && result.contains("successfully")) {
            response.getWriter().write(result);
        } else {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Failed to delete fruit price");
        }
    }
}