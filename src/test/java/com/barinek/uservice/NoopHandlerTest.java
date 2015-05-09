package com.barinek.uservice;

import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class NoopHandlerTest extends AppRunner {
    @Test
    public void testNoop() throws Exception {
        HttpGet get = new HttpGet("http://localhost:8080");
        HttpClient client = new DefaultHttpClient();
        ResponseHandler<String> response = new BasicResponseHandler();
        String responseBody = client.execute(get, response);
        assertEquals("Noop!", responseBody);
    }
}