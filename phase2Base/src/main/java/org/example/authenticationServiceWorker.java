
package org.example;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class authenticationServiceWorker implements authenticationServiceLogin, authenticationServiceSignupWorker {

    private static final String CONNECTION_URL = "jdbc:sqlserver://localhost:1433;" +
            "databaseName=Task_Worker_Matching;" +
            "encrypt=true;" +
            "trustServerCertificate=true;" +
            "user=ahmed;" +
            "password=452005";

    private String name;
    private String address;
    private String userName;
    private String password;
    private int workerID;


    private List<String> timeSlots = new ArrayList<>();
    private List<String> specialties = new ArrayList<>();

    @Override
    public int login(String username, String password) {
        try (Connection conn = DriverManager.getConnection(CONNECTION_URL);
             PreparedStatement stmt = conn.prepareStatement(
                     "SELECT workerID FROM Worker WHERE userName = ? AND password = ?")) {

            stmt.setString(1, username);
            stmt.setString(2, password);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    workerID = rs.getInt("workerID");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return workerID;
    }


    public void getSpeciality(String speciality) {
        specialties.add(speciality);
    }

    public void getSlot(String slot) {
        timeSlots.add(slot);
    }


    @Override
    public boolean signup(String name, String userName, String password, String address) {
        this.name = name;
        this.address = address;
        this.userName = userName;
        this.password = password;

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
        PreparedStatement workerStmt = null;
        PreparedStatement locationStmt = null;
        PreparedStatement timeSlotStmt = null;
        PreparedStatement specialtyStmt = null;

        try {
            // Load the SQL Server JDBC driver
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");

            // Get connection and set auto-commit to false
            conn = DriverManager.getConnection(CONNECTION_URL);
            conn.setAutoCommit(false);

            // Insert worker
            String workerSql = "INSERT INTO Worker (name, userName, password,rating,address) " +
                    "VALUES (?, ?, ?, ?, ?)";

            workerStmt = conn.prepareStatement(workerSql, Statement.RETURN_GENERATED_KEYS);
            workerStmt.setString(1, name);
            workerStmt.setString(2, userName);
            workerStmt.setString(3, password);
            workerStmt.setDouble(4, 0.0); // Default rating for new worker
            workerStmt.setString(5, address);

            int affectedRows = workerStmt.executeUpdate();
            int workerId = -1;

            if (affectedRows > 0) {
                try (ResultSet generatedKeys = workerStmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        workerId = generatedKeys.getInt(1);
                        workerID = generatedKeys.getInt(1);

                    } else {
                        throw new SQLException("Creating worker failed, no ID obtained.");
                    }
                }
            }


            // Insert time slots
            if (workerId != -1 && !timeSlots.isEmpty()) {
                for (String timeSlot : timeSlots) {
                    String timeSlotSql = "INSERT INTO TimeSlot (workerID, period) VALUES (?, ?)";
                    timeSlotStmt = conn.prepareStatement(timeSlotSql, Statement.RETURN_GENERATED_KEYS);
                    timeSlotStmt.setInt(1, workerId);
                    timeSlotStmt.setString(2, timeSlot);
                    timeSlotStmt.executeUpdate();

                    int slotId = -1;
                    try (ResultSet generatedKeys = timeSlotStmt.getGeneratedKeys()) {
                        if (generatedKeys.next()) {
                            slotId = generatedKeys.getInt(1);
                        }
                    }


//                    // Insert into Has table
                    if (slotId != -1) {
                        String hasSql = "INSERT INTO WorksAt (workerID, slotID) VALUES (?, ?)";
                        try (PreparedStatement hasStmt = conn.prepareStatement(hasSql)) {
                            hasStmt.setInt(1, workerId);
                            hasStmt.setInt(2, slotId);
                            hasStmt.executeUpdate();
                        }
                    }
                }
            }


            if (workerId != -1 && !specialties.isEmpty()) {
                for (String specialty : specialties) {
                    // Check if specialty exists or create it
                    int specialtyID = getOrCreateSpecialty(conn, specialty);

                    if (specialtyID != -1) {
                        String hasSql = "INSERT INTO Has (workerID, specialtyID) VALUES (?, ?)";
                        try (PreparedStatement hasStmt = conn.prepareStatement(hasSql)) {
                            hasStmt.setInt(1, workerId);
                            hasStmt.setInt(2, specialtyID);
                            hasStmt.executeUpdate();
                        }
                    }
                }
            }

            //--------------------------


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
            closeQuietly(specialtyStmt);
            closeQuietly(timeSlotStmt);
            closeQuietly(locationStmt);
            closeQuietly(workerStmt);
            closeQuietly(conn);
        }
    }

    private boolean isUserNameExists(String userName) throws SQLException {
        try (Connection conn = DriverManager.getConnection(CONNECTION_URL);
             PreparedStatement stmt = conn.prepareStatement("SELECT 1 FROM Worker WHERE userName = ?")) {

            stmt.setString(1, userName);

            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next(); // Returns true if username exists
            }
        }
    }

    private int getOrCreateSpecialty(Connection conn, String specialtyName) throws SQLException {
        // First check if specialty exists
        try (PreparedStatement stmt = conn.prepareStatement("SELECT specialtyID FROM Specialty WHERE name = ?")) {
            stmt.setString(1, specialtyName);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("specialtyID");
                }
            }
        }

        // If not exists, create it
        try (PreparedStatement stmt = conn.prepareStatement(
                "INSERT INTO Specialty (name) VALUES (?)",
                Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, specialtyName);

            int affectedRows = stmt.executeUpdate();

            if (affectedRows > 0) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        return generatedKeys.getInt(1);
                    }
                }
            }
        }

        return -1; // Failed to get or create specialty
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