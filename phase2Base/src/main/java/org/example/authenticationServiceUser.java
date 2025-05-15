package org.example;

import java.sql.*;

public class authenticationServiceUser implements authenticationServiceSignupUser , authenticationServiceLogin {
    private static final String CONNECTION_URL = "jdbc:sqlserver://localhost:1433;" +
            "databaseName=Task_Worker_Matching;" +
            "encrypt=true;" +
            "trustServerCertificate=true;" +
            "user=ahmed;" +
            "password=452005";

    private String name;
    private String userName;
    private String password;
    private String address;
    private String email;
    private String paymentInfo;
    private int clientID;


    @Override
    public int login(String username, String password)
    {
        try (Connection conn = DriverManager.getConnection(CONNECTION_URL);
             PreparedStatement stmt = conn.prepareStatement(
                     "SELECT clientID FROM Client WHERE userName = ? AND password = ?")) {

            stmt.setString(1, username);
            stmt.setString(2, password);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    clientID = rs.getInt("clientID");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return clientID;
    }


    @Override
    public boolean signup(String name, String userName, String password, String address, String email, String paymentInfo) {
        this.name = name;
        this.userName = userName;
        this.password = password;
        this.address = address;
        this.email = email;
        this.paymentInfo = paymentInfo;
        try {
            return signup();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }

    private boolean signup() throws SQLException, ClassNotFoundException {
        if (isUserNameExists(userName)) {
            return false;
        }

        Connection conn = null;
        PreparedStatement clientStmt = null;


        try {
            // Load the SQL Server JDBC driver
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");

            // Get connection and set auto-commit to false
            conn = DriverManager.getConnection(CONNECTION_URL);
            conn.setAutoCommit(false);

            // Insert worker
            String clientSql = "INSERT INTO Client (name, userName, password,  email, address, paymentInfo) " +
                    "VALUES (?, ?, ?, ?, ?,?)";

            clientStmt = conn.prepareStatement(clientSql, Statement.RETURN_GENERATED_KEYS);
            clientStmt.setString(1, name);
            clientStmt.setString(2, userName);
            clientStmt.setString(3, password);
            clientStmt.setString(4, email); // Default rating for new worker
            clientStmt.setString(5, address);
            clientStmt.setString(6, paymentInfo);

            int affectedRows = clientStmt.executeUpdate();

            if (affectedRows > 0) {
                try (ResultSet generatedKeys = clientStmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {

                        clientID = generatedKeys.getInt(1);

                    } else {
                        throw new SQLException("Creating Client failed, no ID obtained.");
                    }
                }
            }

            // Commit the transaction
            conn.commit();
            return true;

        } catch (SQLException | ClassNotFoundException e) {
            // Roll back the transaction in case of error
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }

            }
            throw e;
        } finally {
            // Close resources
            closeQuietly(clientStmt);
            closeQuietly(conn);
        }
    }

    private boolean isUserNameExists(String userName) throws SQLException {
        try (Connection conn = DriverManager.getConnection(CONNECTION_URL);
             PreparedStatement stmt = conn.prepareStatement("SELECT 1 FROM Client WHERE userName = ?")) {

            stmt.setString(1, userName);

            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next(); // Returns true if username exists
            }
        }
    }

    private void closeQuietly(AutoCloseable resource) {
        if (resource != null) {
            try {
                resource.close();
            } catch (Exception e) {
                // Ignore
            }
        }
    }




}
