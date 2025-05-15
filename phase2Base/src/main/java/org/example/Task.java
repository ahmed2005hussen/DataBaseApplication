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

}
