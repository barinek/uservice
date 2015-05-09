package com.barinek.uservice;

import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.junit.Test;

import java.util.UUID;

import static org.junit.Assert.assertTrue;

public class ResultHandlerTest extends AppRunner {
    @Test
    public void testList() throws Exception {
        String name = UUID.randomUUID().toString();

        ResourceDAO dao = new ResourceDAO(TestDataSource.getDataSource());
        dao.deleteAll();
        Resource resource = dao.create(new Resource(name));

        ResultDAO redis = new ResultDAO(new RedisDataSource("localhost", 6379, "password"));
        redis.create(String.format("/resources/%s/results", resource.getId()), "someValue");

        HttpGet get = new HttpGet(String.format("http://localhost:8080/resources/%s/results", resource.getId()));
        HttpClient client = new DefaultHttpClient();
        ResponseHandler<String> handler = new BasicResponseHandler();
        String responseBody = client.execute(get, handler);
        assertTrue(responseBody.contains("someValue"));
    }
}