package com.example.craytonmacholanassignment42;

import java.io.*;
import java.util.LinkedHashMap;
import java.util.Map;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

@WebServlet(name = "Ticket", value="/Ticket")
@MultipartConfig(fileSizeThreshold = 5_242_880, maxFileSize = 20_971_520L, maxRequestSize = 41_943_040L)
public class TicketServlet extends HttpServlet{
    private volatile int Ticket_ID = 1;
    private Map<Integer, Ticket> TicketDB = new LinkedHashMap<>();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");

        String action = request.getParameter("action");

        if (action == null) {
            action = "list";
        }
        switch(action) {
            case "createTicket" -> showPostForm(request, response);
            case "view" -> viewPost(request, response);
            case "download" -> downloadImage(request, response);
            default -> listPosts(request, response); // this the list and any other
        }

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");

        String action = request.getParameter("action");

        if (action == null) {
            action = "list";
        }
        switch(action) {
            case "create" -> createPost(request, response);
            default -> response.sendRedirect("Ticket"); // this the list and any other
        }
    }

    private void listPosts(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        request.setAttribute("ticketDB", TicketDB);
        RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/jsp/view/listTickets.jsp");
        dispatcher.forward(request, response);
    }

    private void createPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Ticket ticket = new Ticket();
        ticket.setTitle(request.getParameter("title"));
        ticket.setDate();
        ticket.setBody(request.getParameter("body"));

        Part file = request.getPart("file1");
        if (file != null) {
            Image image = this.processImage(file);
            if (image != null) {
                ticket.setImage(image);
            }
        }

        int id;
        synchronized(this) {
            id = this.Ticket_ID++;
            TicketDB.put(id, ticket);
        }

        response.sendRedirect("Ticket?action=view&TicketId=" + id);
    }

    private Image processImage(Part file) throws IOException{
        InputStream in = file.getInputStream();
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        int read;
        final byte[] bytes = new byte[1024];
        while ((read = in.read(bytes)) != -1) {
            out.write(bytes, 0, read);
        }

        com.example.craytonmacholanassignment42.Image image = new com.example.craytonmacholanassignment42.Image();
        image.setName(file.getSubmittedFileName());
        image.setContents(out.toByteArray());

        return image;
    }

    private void downloadImage(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        String idString = request.getParameter("TicketId");

        Ticket ticket = getTicket(idString, response);

        String name = request.getParameter("image");
        if (name == null) {
            response.sendRedirect("Ticket?action=view&TicketId=" + idString);
        }

        Image image = ticket.getImage();
        if (image == null) {
            response.sendRedirect("Ticket?action=view&TicketId=" + idString);
            return;
        }

        response.setHeader("Content-Disposition", "image; filename=" + image.getName());
        response.setContentType("application/octet-stream");

        ServletOutputStream out = response.getOutputStream();
        out.write(image.getContents());
    }

    private void viewPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        String idString = request.getParameter("TicketId");

        Ticket ticket = getTicket(idString, response);

        if (ticket != null) {
            request.setAttribute("ticketId", idString);
            request.setAttribute("ticket", ticket);
            RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/jsp/view/viewTicket.jsp");
            dispatcher.forward(request, response);
        }
    }

    private void showPostForm(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/jsp/view/ticketForm.jsp");
        dispatcher.forward(request, response);
    }

    private Ticket getTicket(String idString, HttpServletResponse response) throws ServletException, IOException{
        if (idString == null || idString.length() == 0) {
            response.sendRedirect("Ticket");
            return null;
        }

        try {
            int id = Integer.parseInt(idString);
            Ticket ticket = TicketDB.get(id);
            if (ticket == null) {
                response.sendRedirect("Ticket");
                return null;
            }
            return ticket;
        }
        catch(Exception e) {
            response.sendRedirect("Ticket");
            return null;
        }
    }

}
