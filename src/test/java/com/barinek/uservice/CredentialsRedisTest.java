package com.barinek.uservice;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class CredentialsRedisTest {
    @Test
    public void testFrom() throws Exception {
        String json = "{\"services\":{\"p-mysql\": [{\"credentials\": {\"jdbcUrl\": \"jdbc:mysql://aUrl.com\"}}], \"rediscloud\": [{\"credentials\": {\"hostname\": \"rediscloud.com\",\"password\": \"password\",\"port\": \"1234\"}}]}}";

        CredentialsRedis credentials = CredentialsRedis.from(json);

        assertEquals("rediscloud.com", credentials.getHostname());
        assertEquals("password", credentials.getPassword());
        assertEquals(1234, credentials.getPort());
    }
}