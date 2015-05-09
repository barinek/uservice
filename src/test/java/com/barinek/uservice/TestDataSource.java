package com.barinek.uservice;

import com.zaxxer.hikari.HikariDataSource;

import javax.sql.DataSource;

public class TestDataSource {
    public static DataSource getDataSource() {
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setJdbcUrl("jdbc:mysql://localhost:3306/uservice_test?user=uservice&password=uservice");
        return dataSource;
    }
}
