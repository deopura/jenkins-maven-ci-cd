package com.example;

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class App extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        out.println("<h1>Hello from Jenkins Maven CI/CD Pipeline!</h1>");
    }
}
