package com.example.craytonmacholanassignment42;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        // Validate username and password
        if ("CraytonMac".equals(username) && "Cm5321".equals(password)) {
            // Create session and store user information
            HttpSession session = request.getSession();
            session.setAttribute("username", username);

            // Redirect to home page or some other authenticated page
            response.sendRedirect("home.jsp");
        } else {
            // Invalid credentials, redirect back to login page
            response.sendRedirect("login.jsp");
        }
    }
}
