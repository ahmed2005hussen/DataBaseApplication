package org.example;

import java.sql.*;

public class Task {
    private static final String CONNECTION_URL = "jdbc:sqlserver://localhost:1433;" +
            "databaseName=Task_Worker_Matching;" +
            "encrypt=true;" +
            "trustServerCertificate=true;" +
            "user=ahmed;" +
            "password=452005";

    public int makeTask(int clientID, int requiredSpecialty) {
        String taskName = "Task" + clientID;
        int avgTime = 0;
        int avgFees = 0;
        int taskID = -1;

        String sql = "INSERT INTO Task (taskName, avgTime, avgfees, requiredSpecialty) " +
                "VALUES (?, ?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(CONNECTION_URL);
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, taskName);
            stmt.setInt(2, avgTime);
            stmt.setInt(3, avgFees);
            stmt.setInt(4, requiredSpecialty);

            int affectedRows = stmt.executeUpdate();

            if (affectedRows > 0) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        taskID = generatedKeys.getInt(1);
                        System.out.println("Task created successfully with ID: " + taskID);
                    } else {
                        throw new SQLException("Creating task failed, no ID returned.");
                    }
                }
            } else {
                throw new SQLException("Creating task failed, no rows affected.");
            }

        } catch (SQLException e) {
            System.out.println("Error creating task: " + e.getMessage());
        }

        return taskID;
    }

    public void updateAvgTime(int taskID, int avgTime) {
        String sql = "UPDATE Task SET avgTime = ? WHERE taskID = ?";

        try (Connection conn = DriverManager.getConnection(CONNECTION_URL);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, avgTime);
            stmt.setInt(2, taskID);

            int updated = stmt.executeUpdate();
            if (updated > 0) {
                System.out.println("avgTime updated successfully.");
            } else {
                System.out.println("No task found with the given ID.");
            }

        } catch (SQLException e) {
            System.out.println("Error updating avgTime: " + e.getMessage());
        }
    }

    public void updateAvgFees(int taskID, int avgFees) {
        String sql = "UPDATE Task SET avgfees = ? WHERE taskID = ?";

        try (Connection conn = DriverManager.getConnection(CONNECTION_URL);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, avgFees);
            stmt.setInt(2, taskID);

            int updated = stmt.executeUpdate();
            if (updated > 0) {
                System.out.println("avgFees updated successfully.");
            } else {
                System.out.println("No task found with the given ID.");
            }

        } catch (SQLException e) {
            System.out.println("Error updating avgFees: " + e.getMessage());
        }
    }

    public void showOffers(int workerID) {
        String sql = """
        SELECT R.requestID, R.reqAddress, R.preferredTimeSlot, T.taskName
        FROM Request R
        JOIN Task T ON R.taskID = T.taskID
        WHERE T.requiredSpecialty IN (
            SELECT specialtyID FROM Has WHERE workerID = ?
        )
        AND R.preferredTimeSlot IN (
            SELECT slotID FROM WorksAt WHERE workerID = ?
        )
        """;

        try (Connection conn = DriverManager.getConnection(CONNECTION_URL);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, workerID);
            stmt.setInt(2, workerID);

            ResultSet rs = stmt.executeQuery();

            System.out.println("Matching Offers for Worker ID: " + workerID);
            while (rs.next()) {
                int requestID = rs.getInt("requestID");
                String address = rs.getString("reqAddress");
                int preferredSlot = rs.getInt("preferredTimeSlot");
                String taskName = rs.getString("taskName");

                System.out.println("Request ID: " + requestID +
                        ", Task: " + taskName +
                        ", Address: " + address +
                        ", Preferred Slot ID: " + preferredSlot);
            }

        } catch (SQLException e) {
            System.out.println("Error while fetching offers: " + e.getMessage());
        }
    }

    public void printClientByRequestID(int reqID) {
        String sql = """
        SELECT c.clientID, c.name, c.userName, c.email, c.address, c.paymentInfo
        FROM Request r
        JOIN Client c ON r.clientID = c.clientID
        WHERE r.requestID = ?
    """;

        try (Connection conn = DriverManager.getConnection(CONNECTION_URL);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, reqID);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                int clientID = rs.getInt("clientID");
                String name = rs.getString("name");
                String userName = rs.getString("userName");
                String email = rs.getString("email");
                String address = rs.getString("address");
                String paymentInfo = rs.getString("paymentInfo");

                System.out.println("Client Info:");
                System.out.println("ID: " + clientID);
                System.out.println("Name: " + name);
                System.out.println("Username: " + userName);
                System.out.println("Email: " + email);
                System.out.println("Address: " + address);
                System.out.println("Payment Info: " + paymentInfo);
            } else {
                System.out.println("No client found for request ID: " + reqID);
            }

        } catch (SQLException e) {
            System.out.println("Error fetching client info: " + e.getMessage());
        }
    }





}
