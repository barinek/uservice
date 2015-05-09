package com.barinek.uservice;

import java.io.IOException;

public class ResultDAO {
    private final RedisDataSource dataSource;

    public ResultDAO(RedisDataSource dataSource) throws IOException {
        this.dataSource = dataSource;
    }

    public void create(String key, String value) throws Exception {
        try (RedisConnection connection = dataSource.getConnection()) {
            connection.set(key, value);
        }
    }

    public String list(String key) throws Exception {
        try (RedisConnection connection = dataSource.getConnection()) {
            return connection.list(key);
        }
    }
}
