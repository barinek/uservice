package com.barinek.uservice;

import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.junit.Test;

import java.util.UUID;

import static org.junit.Assert.assertTrue;

public class ResourceHandlerTest extends AppRunner {
    @Test
    public void testList() throws Exception {
        ResourceDAO dao = new ResourceDAO(TestDataSource.getDataSource());
        dao.deleteAll();

        String name = UUID.randomUUID().toString();
        dao.create(new Resource(name));

        HttpGet get = new HttpGet("http://localhost:8080/resources");
        HttpClient client = new DefaultHttpClient();
        ResponseHandler<String> handler = new BasicResponseHandler();
        String responseBody = client.execute(get, handler);
        assertTrue(responseBody.contains(name));
    }

    @Test
    public void testCreate() throws Exception {
        ResourceDAO dao = new ResourceDAO(TestDataSource.getDataSource());
        dao.deleteAll();

        String name = UUID.randomUUID().toString();
        dao.create(new Resource(name));

        HttpPost post = new HttpPost("http://localhost:8080/resources");
        post.addHeader("Content-type", "application/json");
        post.setEntity(new StringEntity(String.format("{\"identifier\": \"%s\"}", name)));

        HttpClient client = new DefaultHttpClient();
        ResponseHandler<String> handler = new BasicResponseHandler();
        String responseBody = client.execute(post, handler);
        assertTrue(responseBody.contains(name));
    }
}