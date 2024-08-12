package utils;

import liquibase.Contexts;
import liquibase.Liquibase;
import liquibase.database.DatabaseConnection;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.LiquibaseException;
import liquibase.resource.ClassLoaderResourceAccessor;
import liquibase.resource.ResourceAccessor;
import org.postgresql.Driver;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class LiquibaseMigration {
    private static final String JDBC_URL = "jdbc:postgresql://localhost:5432/carshopservice";
    private static final String USERNAME = "userPostgres";
    private static final String PASSWORD = "passwordPostgres";
    private static final String CHANGELOG_FILE = "db/changelog/db.changelog-master.yaml";

    public static void main(String[] args) {
        Connection connection = null;
        try {
            DriverManager.registerDriver(new Driver());

            connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);

            DatabaseConnection databaseConnection = new JdbcConnection(connection);
            ResourceAccessor resourceAccessor = new ClassLoaderResourceAccessor();
            Liquibase liquibase = new Liquibase(CHANGELOG_FILE, resourceAccessor, databaseConnection);

            liquibase.update(new Contexts());

            System.out.println("Migrations applied successfully!");

        } catch (SQLException | LiquibaseException e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
