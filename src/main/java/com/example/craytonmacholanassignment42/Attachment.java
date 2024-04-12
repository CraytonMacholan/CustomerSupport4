package com.example.craytonmacholanassignment42;
import java.io.Serializable;

public class Attachment implements Serializable {
    private String name;
    private byte[] contents;

    // Getter for name
    public String getName() {
        return name;
    }

    // Setter for name
    public void setName(String name) {
        this.name = name;
    }

    // Getter for contents
    public byte[] getContents() {
        return contents;
    }

    // Setter for contents
    public void setContents(byte[] contents) {
        this.contents = contents;
    }
}
