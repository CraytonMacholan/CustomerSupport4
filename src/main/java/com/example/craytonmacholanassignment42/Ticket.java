package com.example.craytonmacholanassignment42;


import java.util.ArrayList;
import java.util.List;
import java.time.LocalDate;

public class Ticket {
    private String customerName;
    private String title;
    private String subject;
    private String body;
    private LocalDate date;
    private Image image;
    private List<Attachment> attachments; // If using ArrayList
    //private Map<Integer, Attachment> attachments; // If using HashMap

    public Ticket() {
        // Default constructor
    }

    public Ticket(String customerName, String subject, String body, List<Attachment> attachments) {
        this.customerName = customerName;
        this.subject = subject;
        this.body = body;
        this.attachments = attachments;
    }

    // If using HashMap for attachments, provide another constructor
     //public Ticket(String customerName, String subject, String body, Map<Integer, Attachment> attachments) {
         //this.customerName = customerName;
         //this.subject = subject;
       //  this.body = body;
         //this.attachments = attachments;
     //}

    // Getters and Setters
    public String getCustomerName() {
        return customerName;
    }

    public String getTitle() {
        return title;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public List<Attachment> getAttachments() {
        return attachments;
    }

    public void setAttachments(List<Attachment> attachments) {
        this.attachments = attachments;
    }

    // Helper methods
    public void addAttachment(Attachment attachment) {
        if (attachments == null) {
            attachments = new ArrayList<>();
        }
        attachments.add(attachment);
    }

    public int getNumberOfAttachments() {
        return attachments != null ? attachments.size() : 0;
    }

    public Attachment getAttachment(int index) {
        if (attachments != null && index >= 0 && index < attachments.size()) {
            return attachments.get(index);
        }
        return null;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDate() {
        this.date = date;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public Image getImage() {
        return image;
    }

    public LocalDate getDate() {
        return date;
    }

    public boolean hasImage() {
        return image != null;
    }

    // If using HashMap for attachments
    // public void addAttachment(int index, Attachment attachment) {
    //     if (attachments == null) {
    //         attachments = new HashMap<>();
    //     }
    //     attachments.put(index, attachment);
    // }

    // public int getNumberOfAttachments() {
    //     return attachments != null ? attachments.size() : 0;
    // }

    // public Attachment getAttachment(int index) {
    //     return attachments != null ? attachments.get(index) : null;
    // }

    // public Map<Integer, Attachment> getAllAttachments() {
    //     return attachments;
    // }
}
