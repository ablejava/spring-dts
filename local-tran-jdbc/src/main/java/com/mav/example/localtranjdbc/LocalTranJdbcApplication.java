package com.mav.example.localtranjdbc;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class LocalTranJdbcApplication {

    private static final Logger LOG = LoggerFactory.getLogger(LocalTranJdbcApplication.class);

    public static void main(String[] args) throws SQLException {

        Connection dbConnection = null;
        PreparedStatement preparedStatementInsert = null;
        PreparedStatement preparedStatementUpdate = null;

        String insertTableSQL = "INSERT INTO T_USER (ID, USERNAME) VALUES (?,?)";
        String updateTableSQL = "UPDATE T_USER SET USERNAME =? WHERE ID = ?";

        try {
            dbConnection = getDBConnection();
            LOG.debug("Begin");
//            dbConnection.setAutoCommit(false);

            preparedStatementInsert = dbConnection.prepareStatement(insertTableSQL);
            preparedStatementInsert.setLong(1, 1234);
            preparedStatementInsert.setString(2, "M.T.");
            preparedStatementInsert.executeUpdate();

            simulateError();

            preparedStatementUpdate = dbConnection.prepareStatement(updateTableSQL);
            preparedStatementUpdate.setString(1, "Super Man");
            preparedStatementUpdate.setInt(2, 1234);
            preparedStatementUpdate.executeUpdate();

//            dbConnection.commit();
            LOG.debug("Done!");
        } catch (SQLException e) {
            LOG.error(e.getMessage(), e);
            dbConnection.rollback();
        } finally {

            if (preparedStatementInsert != null) {
                preparedStatementInsert.close();
            }
            if (preparedStatementUpdate != null) {
                preparedStatementUpdate.close();
            }
            if (dbConnection != null) {
                dbConnection.close();
            }
        }
    }

    private static void simulateError() throws SQLException {
        throw new SQLException("Simulate some error!");
    }

    private static Connection getDBConnection() throws SQLException {
        String DB_DRIVER = "com.mysql.jdbc.Driver";
        String DB_CONNECTION = "jdbc:mysql://localhost:3306/dist_tran_course";
        String DB_USER = "mt";
        String DB_PASSWORD = "111111";
        try {
            Class.forName(DB_DRIVER);
        } catch (ClassNotFoundException e) {
            LOG.error(e.getMessage());
        }
        return DriverManager.getConnection(DB_CONNECTION, DB_USER, DB_PASSWORD);
    }
}
