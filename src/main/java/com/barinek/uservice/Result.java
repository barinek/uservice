package com.barinek.uservice;

public class Result {
    private int resourceId;
    private String json;

    public Result() { // for jackson
    }

    public Result(int resourceId, String json) {
        this.resourceId = resourceId;
        this.json = json;
    }
}
