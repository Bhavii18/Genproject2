package com.gen.controller;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.io.IOException;

import com.gen.bean.User;
import com.gen.services.UserService;

//@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private UserService userService;

    public LoginServlet() {
        this.userService = new UserService();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            // Get account number and password from request parameters
            int accountNumber = Integer.parseInt(request.getParameter("username"));
            String password = request.getParameter("password");

            // Authenticate user
            User user = userService.authenticateUser(accountNumber, password);

            if (user != null) {
                // Create a new session
                HttpSession session = request.getSession(true); // Ensures a new session is created

                // Set user and account number attributes in session
                session.setAttribute("user", user);
                session.setAttribute("user_id", accountNumber);
                System.out.println(accountNumber + password);

                // Redirect to respective dashboard based on user role
                if ("Admin".equals(user.getRole())) {
                    response.sendRedirect("adminDashboard.jsp");
                } else if ("Associate".equals(user.getRole())) {
                    response.sendRedirect("taskDashboard.jsp");
                } else {
                    response.sendRedirect("userDashboard.jsp");
                }
            } else {
                // Set error message attribute and redirect back to login page
                request.setAttribute("errorMessage", "Invalid credentials. Please try again.");
                request.getRequestDispatcher("login.jsp").forward(request, response);
            }
        } catch (NumberFormatException e) {
            // Handle invalid number format for account number
            request.setAttribute("errorMessage", "Invalid account number format. Please enter a valid number.");
            request.getRequestDispatcher("login.jsp").forward(request, response);
        } catch (Exception e) {
            // Handle other exceptions
            e.printStackTrace(); // Log the exception for debugging
            request.setAttribute("errorMessage", "An unexpected error occurred. Please try again later.");
            request.getRequestDispatcher("login.jsp").forward(request, response);
        }
    }
}
