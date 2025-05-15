package org.example;

import java.sql.*;

public class ExecutedReq {
    private static final String CONNECTION_URL = "jdbc:sqlserver://localhost:1433;" +
            "databaseName=Task_Worker_Matching;" +
            "encrypt=true;" +
            "trustServerCertificate=true;" +
            "user=ahmed;" +
            "password=452005";
    public void insertInitialExecutedRequest(int requestID, int workerID) {
        String sql = "INSERT INTO ExecutedReq (reqID, workerID, clientRating, workerRating, actualTime, reqStatus, workerFeedback, clientFeedback) " +
                "VALUES (?, ?, 0.0, 0.0, 0, 'Pending', '', '')";

        try (Connection conn = DriverManager.getConnection(CONNECTION_URL);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, requestID);
            stmt.setInt(2, workerID);

            int rows = stmt.executeUpdate();
            if (rows > 0) {
                System.out.println("Inserted into ExecutedReq with Pending status.");
            } else {
                System.out.println("Insert failed.");
            }

        } catch (SQLException e) {
            System.out.println("Error inserting into ExecutedReq: " + e.getMessage());
        }
    }

    public void updateWorkerRating(int reqID, double rating) {
        String sql = "UPDATE ExecutedReq SET workerRating = ? WHERE reqID = ?";

        try (Connection conn = DriverManager.getConnection(CONNECTION_URL);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setDouble(1, rating);
            stmt.setInt(2, reqID);
            stmt.executeUpdate();

            System.out.println("Worker rating updated.");

        } catch (SQLException e) {
            System.out.println("Error updating worker rating: " + e.getMessage());
        }
    }

    public void updateClientRating(int reqID, double rating) {
        String sql = "UPDATE ExecutedReq SET clientRating = ? WHERE reqID = ?";

        try (Connection conn = DriverManager.getConnection(CONNECTION_URL);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setDouble(1, rating);
            stmt.setInt(2, reqID);
            stmt.executeUpdate();

            System.out.println("Client rating updated.");

        } catch (SQLException e) {
            System.out.println("Error updating client rating: " + e.getMessage());
        }
    }

    public void updateClientFeedback(int reqID, String feedback) {
        String sql = "UPDATE ExecutedReq SET clientFeedback = ? WHERE reqID = ?";

        try (Connection conn = DriverManager.getConnection(CONNECTION_URL);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, feedback);
            stmt.setInt(2, reqID);
            stmt.executeUpdate();

            System.out.println("Client feedback updated.");

        } catch (SQLException e) {
            System.out.println("Error updating client feedback: " + e.getMessage());
        }
    }

    public void updateWorkerFeedback(int reqID, String feedback) {
        String sql = "UPDATE ExecutedReq SET workerFeedback = ? WHERE reqID = ?";

        try (Connection conn = DriverManager.getConnection(CONNECTION_URL);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, feedback);
            stmt.setInt(2, reqID);
            stmt.executeUpdate();

            System.out.println("Worker feedback updated.");

        } catch (SQLException e) {
            System.out.println("Error updating worker feedback: " + e.getMessage());
        }
    }

    public void updateActualTime(int reqID, int time) {
        String sql = "UPDATE ExecutedReq SET actualTime = ? WHERE reqID = ?";

        try (Connection conn = DriverManager.getConnection(CONNECTION_URL);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, time);
            stmt.setInt(2, reqID);
            stmt.executeUpdate();

            System.out.println("Actual time updated.");

        } catch (SQLException e) {
            System.out.println("Error updating actual time: " + e.getMessage());
        }
    }

    public void updateRequestStatus(int reqID, String status) {
        String sql = "UPDATE ExecutedReq SET reqStatus = ? WHERE reqID = ?";

        try (Connection conn = DriverManager.getConnection(CONNECTION_URL);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, status);
            stmt.setInt(2, reqID);
            stmt.executeUpdate();

            System.out.println("Request status updated to: " + status);

        } catch (SQLException e) {
            System.out.println("Error updating request status: " + e.getMessage());
        }
    }

    public void ShowExecutedRequests(int workerID) {
        String sql = """
        SELECT reqID, clientRating, workerRating, actualTime, reqStatus, workerFeedback, clientFeedback
        FROM ExecutedReq
        WHERE workerID = ?
    """;

        try (Connection conn = DriverManager.getConnection(CONNECTION_URL);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, workerID);
            ResultSet rs = stmt.executeQuery();

            boolean hasResults = false;
            while (rs.next()) {
                hasResults = true;
                int reqID = rs.getInt("reqID");
                double clientRating = rs.getDouble("clientRating");
                double workerRating = rs.getDouble("workerRating");
                int actualTime = rs.getInt("actualTime");
                String reqStatus = rs.getString("reqStatus");
                String workerFeedback = rs.getString("workerFeedback");
                String clientFeedback = rs.getString("clientFeedback");

                System.out.println("Executed Request:");
                System.out.println("Request ID: " + reqID);
                System.out.println("Client Rating: " + clientRating);
                System.out.println("Worker Rating: " + workerRating);
                System.out.println("Actual Time: " + actualTime);
                System.out.println("Request Status: " + reqStatus);
                System.out.println("Worker Feedback: " + workerFeedback);
                System.out.println("Client Feedback: " + clientFeedback);
                System.out.println("-------------------------------");
            }

            if (!hasResults) {
                System.out.println("No executed requests found for worker ID: " + workerID);
            }

        } catch (SQLException e) {
            System.out.println("Error fetching executed requests: " + e.getMessage());
        }
    }



}
