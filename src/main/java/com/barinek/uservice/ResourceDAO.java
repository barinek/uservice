package com.barinek.uservice;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ResourceDAO {
    private final DataSource dataSource;

    public ResourceDAO(DataSource dataSource) throws ClassNotFoundException, SQLException {
        this.dataSource = dataSource;
    }

    public List<Resource> list() throws SQLException {
        List<Resource> results = new ArrayList<>();
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement("select id, identifier from resources")) {
                try (ResultSet rs = statement.executeQuery()) {
                    while (rs.next()) {
                        results.add(new Resource(rs.getInt(1), rs.getString(2)));
                    }
                }
            }
        }
        return results;
    }

    public Resource create(Resource resource) throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement("insert into resources (identifier) values (?)", Statement.RETURN_GENERATED_KEYS)) {
                statement.setString(1, resource.getIdentifier());
                statement.executeUpdate();
                ResultSet keys = statement.getGeneratedKeys();
                keys.next();
                return new Resource(keys.getInt(1), resource.getIdentifier());
            }
        }
    }

    public void deleteAll() throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement("delete from resources")) {
                statement.executeUpdate();
            }
        }
    }
}
