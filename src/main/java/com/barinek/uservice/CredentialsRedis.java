package com.barinek.uservice;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;

public class CredentialsRedis {
    private final String hostname;
    private final String password;
    private final int port;

    public CredentialsRedis(String hostname, String password, int port) {
        this.hostname = hostname;
        this.password = password;
        this.port = port;
    }

    public String getHostname() {
        return hostname;
    }

    public String getPassword() {
        return password;
    }

    public int getPort() {
        return port;
    }

    public static CredentialsRedis from(String json) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree(json);
        JsonNode redis = root.findValue("rediscloud");
        JsonNode credentials = redis.findValue("credentials");

        return new CredentialsRedis(
                credentials.findValue("hostname").getTextValue(),
                credentials.findValue("password").getTextValue(),
                Integer.parseInt(credentials.findValue("port").getTextValue())
        );
    }
}
