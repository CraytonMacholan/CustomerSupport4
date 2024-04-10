package com.example.craytonmacholanassignment42;

import java.io.*;
import java.util.LinkedHashMap;
import java.util.Map;

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
        PrintWriter out = response.getWriter();

        //heading and link to create a Ticket
        out.println("<html><body><h2>Ticket Posts</h2>");
        out.println("<a href=\"Ticket?action=createTicket\">Create Post</a><br><br>");

        // list out the Tickets
        if (TicketDB.size() == 0) {
            out.println("There are no Ticket posts yet...");
        }
        else {
            for (int id : TicketDB.keySet()) {
                Ticket ticket = TicketDB.get(id);
                out.println("Ticket #" + id);
                out.println(": <a href=\"Ticket?action=view&TicketId=" + id + "\">");
                out.println(ticket.getTitle() + "</a><br>");
            }
        }
        out.println("</body></html>");

    }

    private void createPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // create the Ticket and set all values up
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

        // add and synchronize
        int id;
        synchronized(this) {
            id = this.Ticket_ID++;
            TicketDB.put(id, ticket);
        }

        //System.out.println(ticket);  // see what is in the Ticket object
        response.sendRedirect("Ticket?action=view&TicketId=" + id);
    }

    private Image processImage(Part file) throws IOException{
        InputStream in = file.getInputStream();
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        // processing the binary data to bytes
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

            PrintWriter out = response.getWriter();
            out.println("<html><body><h2>Ticket Post</h2>");
            out.println("<h3>" + ticket.getTitle()+ "</h3>");
            out.println("<p>Date: " + ticket.getDate() + "</p>");
            out.println("<p>" + ticket.getBody() + "</p>");
            if (ticket.hasImage()) {
                out.println("<a href=\"Ticket?action=download&TicketId=" +
                        idString + "&image="+ ticket.getImage().getName() + "\">" +
                        ticket.getImage().getName() + "</a><br><br>");
            }
            out.println("<a href=\"ticket\">Return to Ticket list</a>");
            out.println("</body></html>");

    }

    private void showPostForm(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        PrintWriter out = response.getWriter();

        out.println("<html><body><h2>Create a Ticket Post</h2>");
        out.println("<form method=\"POST\" action=\"Ticket\" enctype=\"multipart/form-data\">");
        out.println("<input type=\"hidden\" name=\"action\" value=\"create\">");
        out.println("Title:<br>");
        out.println("<input type=\"text\" name=\"title\"><br><br>");
        out.println("Body:<br>");
        out.println("<textarea name=\"body\" rows=\"25\" cols=\"100\"></textarea><br><br>");
        out.println("<b>Image</b><br>");
        out.println("<input type=\"file\" name=\"file1\"><br><br>");
        out.println("<input type=\"submit\" value=\"Submit\">");
        out.println("</form></body></html>");

    }

    private Ticket getTicket(String idString, HttpServletResponse response) throws ServletException, IOException{
        // empty string id
        if (idString == null || idString.length() == 0) {
            response.sendRedirect("Ticket");
            return null;
        }

        // find in the 'database' otherwise return null
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
