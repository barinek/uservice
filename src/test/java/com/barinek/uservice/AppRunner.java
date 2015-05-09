package com.barinek.uservice;

import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.junit.AfterClass;
import org.junit.BeforeClass;

public class AppRunner {
    protected static App app;

    @BeforeClass
    public static void setUp() throws Exception {
        app = new App();
        app.start();
    }

    @AfterClass
    public static void tearDown() throws Exception {
        app.stop();
    }

    protected String doGet(String ep) throws java.io.IOException {
        HttpGet get = new HttpGet(ep);
        HttpClient client = new DefaultHttpClient();
        ResponseHandler<String> handler = new BasicResponseHandler();
        return client.execute(get, handler);
    }

    protected String doPost(String endpoint, String data) throws java.io.IOException {
        HttpPost post = new HttpPost(endpoint);
        post.addHeader("Content-type", "application/json");
        post.setEntity(new StringEntity(data));

        HttpClient client = new DefaultHttpClient();
        ResponseHandler<String> handler = new BasicResponseHandler();
        return client.execute(post, handler);
    }
}
