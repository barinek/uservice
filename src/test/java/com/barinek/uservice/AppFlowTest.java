package com.barinek.uservice;

import org.junit.Test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.Assert.assertTrue;

public class AppFlowTest extends AppRunner {
    @Test
    public void testBasicFlow() throws Exception {
        ResourceDAO dao = new ResourceDAO(TestDataSource.getDataSource());
        dao.deleteAll();

        String response;

        response = doPost("http://localhost:8080/resources", "{\"identifier\": \"anIdentifier\"}");
        assertTrue(response.contains("\"identifier\":\"anIdentifier\""));

        Pattern envPattern = Pattern.compile("id\":(\\d+),");
        Matcher matcher = envPattern.matcher(response);
        matcher.find();
        String id = matcher.group(1);

        response = doGet("http://localhost:8080/resources");
        assertTrue(response.contains("\"identifier\":\"anIdentifier\""));

        response = doPost(String.format("http://localhost:8080/resources/%s/results", id), "someData");
        assertTrue(response.contains("success"));

        response = doGet(String.format("http://localhost:8080/resources/%s/results", id));
        assertTrue(response.contains("someData"));
    }
}
