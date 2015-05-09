package com.barinek.uservice;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;

public class CredentialsMySQL {
    private final String url;

    public CredentialsMySQL(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public static CredentialsMySQL from(String json) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree(json);
        JsonNode mysql = root.findValue("p-mysql");
        JsonNode credentials = mysql.findValue("credentials");
        return new CredentialsMySQL(credentials.findValue("jdbcUrl").getTextValue());
    }
}
