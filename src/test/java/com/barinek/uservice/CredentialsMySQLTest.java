package com.barinek.uservice;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class CredentialsMySQLTest {
    @Test
    public void testFrom() throws Exception {
        String json = "{\"services\":{\"p-mysql\": [{\"credentials\": {\"jdbcUrl\": \"jdbc:mysql://aUrl.com\"}}], \"rediscloud\": [{\"credentials\": {\"hostname\": \"rediscloud.com\",\"password\": \"password\",\"port\": \"1234\"}}]}}";

        CredentialsMySQL credentials = CredentialsMySQL.from(json);

        assertEquals("jdbc:mysql://aUrl.com", credentials.getUrl());
    }
}