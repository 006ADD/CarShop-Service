package utils;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.PostgreSQLContainer;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class PSQLContainer {
    private PostgreSQLContainer<?> postgresContainer;

    @BeforeEach
    public void setUp() {
        postgresContainer = new PostgreSQLContainer<>("postgres:latest")
                .withDatabaseName("carshopservice")
                .withUsername("userPostgres")
                .withPassword("passwordPostgres");
        postgresContainer.start();

        System.setProperty("jdbc.url", postgresContainer.getJdbcUrl());
        System.setProperty("jdbc.username", postgresContainer.getUsername());
        System.setProperty("jdbc.password", postgresContainer.getPassword());

        try (Connection connection = DriverManager.getConnection(
                postgresContainer.getJdbcUrl(),
                postgresContainer.getUsername(),
                postgresContainer.getPassword())) {
            Statement stmt = connection.createStatement();
            stmt.execute("CREATE TABLE test_table (id SERIAL PRIMARY KEY, name VARCHAR(255))");
        } catch (SQLException e) {
            e.printStackTrace();
            Assertions.assertTrue(false, "Error when creating tables: " + e.getMessage());
        }
    }

    @Test
    public void testDatabaseOperation() {
        try (Connection connection = DriverManager.getConnection(
                System.getProperty("jdbc.url"),
                System.getProperty("jdbc.username"),
                System.getProperty("jdbc.password"))) {

            Statement stmt = connection.createStatement();
            stmt.execute("INSERT INTO test_table (name) VALUES ('Test Name')");

            var resultSet = stmt.executeQuery("SELECT * FROM test_table");
            Assertions.assertTrue(resultSet.next());
            Assertions.assertTrue(resultSet.getString("name").equals("Test Name"));
        } catch (SQLException e) {
            e.printStackTrace();
            Assertions.assertTrue(false, "Error when running test: " + e.getMessage());
        }
    }

    @AfterEach
    public void tearDown() {
        if (postgresContainer != null) {
            postgresContainer.stop();
        }
    }
}
