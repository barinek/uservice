package com.barinek.uservice;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class MigrationsMySQL {
    private final DataSource dataSource;

    public MigrationsMySQL(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void tryMigrations() throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement("create table if not exists resources (id int not null auto_increment primary key, identifier varchar(256));")) {
                statement.executeUpdate();
            }
        }
    }
}
