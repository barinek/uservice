package com.barinek.uservice;

import org.junit.Test;

public class RedisConnectionTest {

    @Test
    public void test() throws Exception {
        assert (new RedisDataSource("localhost", 6379, "password").getConnection().connected());
    }
}