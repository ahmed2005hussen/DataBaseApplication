package org.example;

import java.sql.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Request {
    private static final String CONNECTION_URL = "jdbc:sqlserver://localhost:1433;" +
            "databaseName=Task_Worker_Matching;" +
            "encrypt=true;" +
            "trustServerCertificate=true;" +
            "user=ahmed;" +
            "password=452005";

    public void printAllSpecialties() {
        PreparedStatement stmt = null;
        ResultSet rs = null;


        try(Connection conn = DriverManager.getConnection(CONNECTION_URL)) {

            String sql = "SELECT specialtyID, name FROM Specialty";
            stmt = conn.prepareStatement(sql);
            rs = stmt.executeQuery();

            System.out.println("List of Specialties: ");
            while (rs.next()) {
                int id = rs.getInt("specialtyID");
                String name = rs.getString("name");

                System.out.println(id + "- Name: " + name );
            }

        } catch (SQLException e) {
            System.out.println("Error while fetching specialties: " + e.getMessage());
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
            } catch (SQLException e) {
                System.out.println("Error closing resources: " + e.getMessage());
            }
        }
    }

    public void printTimeSlotsForSpecialty(int specialtyId) {
        String sql = "SELECT ts.slotID, ts.workerID, ts.period " +
                "FROM TimeSlot ts " +
                "JOIN Has h ON ts.workerID = h.workerID " +
                "WHERE h.specialtyID = ?";

        try (Connection conn = DriverManager.getConnection(CONNECTION_URL);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, specialtyId);

            try (ResultSet rs = stmt.executeQuery()) {
                System.out.println("TimeSlots for workers with specialty ID " + specialtyId + ":");

                while (rs.next()) {
                    int slotID = rs.getInt("slotID");
                    String period = rs.getString("period");

                    System.out.println(" Slot ID: " + slotID + ", Period: " + period);
                }
            }

        } catch (SQLException e) {
            System.out.println("Error while fetching time slots: " + e.getMessage());
        }
    }

    public void makeRequest(int clientID, String address, int preferSlot, int taskID) {
        String sql = "INSERT INTO Request (clientID, reqAddress, preferredTimeSlot, reqPlacementTime, taskID) " +
                "VALUES (?, ?, ?, ?, ?)";

        Timestamp now = new Timestamp(System.currentTimeMillis()); // الوقت الحالي

        try (Connection conn = DriverManager.getConnection(CONNECTION_URL);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, clientID);
            stmt.setString(2, address);
            stmt.setInt(3, preferSlot);  // الوقت اللي اختاره العميل
            stmt.setString(4, now.toString()); // وقت تقديم الطلب (نخزنه كنص)
            stmt.setInt(5, taskID);

            int affectedRows = stmt.executeUpdate();

            if (affectedRows > 0) {
                System.out.println("Request added successfully.");
            } else {
                System.out.println("Failed to add the request.");
            }

        } catch (SQLException e) {
            System.out.println("Error while inserting request: " + e.getMessage());
        }
    }


}
