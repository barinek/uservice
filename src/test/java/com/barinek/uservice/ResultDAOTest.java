package com.barinek.uservice;

import org.junit.Test;

import static org.junit.Assert.assertTrue;


public class ResultDAOTest {
    @Test
    public void testRead() throws Exception {
        String key = "db";
        String value = "redis";

        ResultDAO redis = new ResultDAO(new RedisDataSource("localhost", 6379, "password"));
        redis.create(key, value);
        String actual = redis.list(key);

        assertTrue(actual.contains(value)); // actual has '/r/n'
    }
}