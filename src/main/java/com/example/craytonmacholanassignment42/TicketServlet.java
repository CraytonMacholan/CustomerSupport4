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
public class TicketServlet extends HttpServlet {
    private volatile int ticketId = 1;
    private Map<Integer, Ticket> ticketDB = new LinkedHashMap<>();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");

        String action = request.getParameter("action");

        if (action == null) {
            action = "list";
        }
        switch(action) {
            case "createTicket":
                showPostForm(request, response);
                break;
            case "view":
                viewTicket(request, response);
                break;
            case "download":
                downloadAttachment(request, response);
                break;
            default:
                listTickets(request, response);
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
            case "create":
                createTicket(request, response);
                break;
            default:
                response.sendRedirect("Ticket");
        }
    }

    private void listTickets(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PrintWriter out = response.getWriter();

        out.println("<html><body><h2>Ticket Posts</h2>");
        out.println("<a href=\"Ticket?action=createTicket\">Create Post</a><br><br>");

        if (ticketDB.isEmpty()) {
            out.println("There are no Ticket posts yet...");
        } else {
            for (int id : ticketDB.keySet()) {
                Ticket ticket = ticketDB.get(id);
                out.println("Ticket #" + id);
                out.println(": <a href=\"Ticket?action=view&TicketId=" + id + "\">");
                out.println(ticket.getTitle() + "</a><br>");
            }
        }
        out.println("</body></html>");
    }

    private void createTicket(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Ticket ticket = new Ticket();
        ticket.setTitle(request.getParameter("title"));
        ticket.setDate();
        ticket.setBody(request.getParameter("body"));

        Part file = request.getPart("file1");
        if (file != null) {
            Image image = processImage(file);
            if (image != null) {
                ticket.setImage(image);
            }
        }

        int id;
        synchronized(this) {
            id = this.ticketId++;
            ticketDB.put(id, ticket);
        }

        response.sendRedirect("Ticket?action=view&TicketId=" + id);
    }

    private Image processImage(Part file) throws IOException {
        InputStream in = file.getInputStream();
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        int read;
        final byte[] bytes = new byte[1024];
        while ((read = in.read(bytes)) != -1) {
            out.write(bytes, 0, read);
        }

        Image image = new Image();
        image.setName(file.getSubmittedFileName());
        image.setContents(out.toByteArray());

        return image;
    }

    private void downloadAttachment(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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

    private void viewTicket(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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

    private void showPostForm(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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

    private Ticket getTicket(String idString, HttpServletResponse response) throws ServletException, IOException {
        if (idString == null || idString.length() == 0) {
            response.sendRedirect("Ticket");
            return null;
        }

        try {
            int id = Integer.parseInt(idString);
            Ticket ticket = ticketDB.get(id);
            if (ticket == null) {
                response.sendRedirect("Ticket");
                return null;
            }
            return ticket;
        } catch(Exception e) {
            response.sendRedirect("Ticket");
            return null;
        }
    }
}
