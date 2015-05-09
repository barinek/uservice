package com.barinek.uservice;

public class Resource {
    private int id;
    private String identifier;

    public Resource() { // for jackson
    }

    public Resource(String identifier) {
        this.identifier = identifier;
    }

    public Resource(int id, String identifier) {
        this.id = id;
        this.identifier = identifier;
    }

    public int getId() {
        return id;
    }

    public String getIdentifier() {
        return identifier;
    }
}
