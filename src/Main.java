import java.sql.*;
import java.util.Scanner;

public class Main {

    private static final Scanner scanner = new Scanner(System.in);

    private static final String url = "jdbc:sqlserver://localhost:1433;databaseName=Task_Worker_Matching;encrypt=true;trustServerCertificate=true";
    private static final String user = "Menna-AbdElGawad";
    private static final String password = "eng/mennaa2610";

    public static void main(String[] args) {
        testDatabaseConnection();

        boolean exit = false;
        while (!exit) {
            displayMainMenu();
            int choice = getUserChoice(1, 4);

            switch (choice) {
                case 1:
                    loginMenu();
                    break;
                case 2:
                    registerClient();
                    break;
                case 3:
                    registerWorker();
                    break;
                case 4:
                    exit = true;
                    System.out.println("Thank you for using the Task Worker Matching System. Goodbye!");
                    break;
            }
        }

        scanner.close();
    }

    private static void testDatabaseConnection() {
        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            System.out.println("Database connection successful!");
        } catch (SQLException e) {
            System.out.println("Database connection error: " + e.getMessage());
        }
    }

    private static void displayMainMenu() {
        System.out.println("\nMain Menu:");
        System.out.println("1. Login");
        System.out.println("2. Register as Client");
        System.out.println("3. Register as Worker");
        System.out.println("4. Exit");
        System.out.print("Enter your choice: ");
    }

    private static int getUserChoice(int min, int max) {
        int choice = -1;
        boolean validInput = false;

        while (!validInput) {
            try {
                choice = Integer.parseInt(scanner.nextLine().trim());
                if (choice >= min && choice <= max) {
                    validInput = true;
                } else {
                    System.out.print("Please enter a number between " + min + " and " + max + ": ");
                }
            } catch (NumberFormatException e) {
                System.out.print("Invalid input. Please enter a number: ");
            }
        }

        return choice;
    }

    private static void loginMenu() {
        System.out.println("\nLogin as:");
        System.out.println("1. Client");
        System.out.println("2. Worker");
        System.out.print("Enter your choice: ");

        int choice = getUserChoice(1, 2);

        if (choice == 1) {
            loginC();
        } else {
            loginW();
        }
    }

    private static void loginC() {
        System.out.println("\n=== Client Log in ===");
        System.out.print("Username: ");
        String username = scanner.nextLine().trim();
        System.out.print("Password: ");
        String enteredPassword = scanner.nextLine().trim();

        if (validateLoginC(username, enteredPassword)) {
            System.out.println("Login successful! Welcome " + username + "!");
        } else {
            System.out.println("Login failed! Wrong username or password.");
        }
    }

    private static void loginW() {
        System.out.println("\n=== Worker Log in ===");
        System.out.print("Username: ");
        String username = scanner.nextLine().trim();
        System.out.print("Password: ");
        String enteredPassword = scanner.nextLine().trim();

        if (validateLoginW(username, enteredPassword)) {
            System.out.println("Login successful! Welcome " + username + "!");
        } else {
            System.out.println("Login failed! Wrong username or password.");
        }
    }

    private static void registerClient() {
        System.out.println("\n=== Client Sign Up ===");

        System.out.print("Username: ");
        String username = scanner.nextLine().trim();

        if (checkIfUsernameExistsC(username) || checkIfUsernameExistsW(username)) {
            System.out.println("Username already exists. Please choose another one.");
            return;
        }

        System.out.print("Password: ");
        String enteredPassword = scanner.nextLine().trim();
        System.out.print("Email: ");
        String email = scanner.nextLine().trim();
        System.out.print("First Name: ");
        String firstName = scanner.nextLine().trim();
        System.out.print("Last Name: ");
        String lastName = scanner.nextLine().trim();

        ClientSignUp clientSignUp = new ClientSignUp();
        if (clientSignUp.signUp(username, enteredPassword, email, firstName, lastName)) {
            System.out.println("Client registered successfully!");
        } else {
            System.out.println("Failed to register client.");
        }
    }

    private static void registerWorker() {
        System.out.println("\n=== Worker Sign Up ===");

        System.out.print("Username: ");
        String username = scanner.nextLine().trim();

        if (checkIfUsernameExistsC(username) || checkIfUsernameExistsW(username)) {
            System.out.println("Username already exists. Please choose another one.");
            return;
        }

        System.out.print("Password: ");
        String enteredPassword = scanner.nextLine().trim();
        System.out.print("Email: ");
        String email = scanner.nextLine().trim();
        System.out.print("First Name: ");
        String firstName = scanner.nextLine().trim();
        System.out.print("Last Name: ");
        String lastName = scanner.nextLine().trim();

        WorkerSignUp workerSignUp = new WorkerSignUp();
        if (workerSignUp.signUp(username, enteredPassword, email, firstName, lastName)) {
            System.out.println("Worker registered successfully!");
        } else {
            System.out.println("Failed to register worker.");
        }
    }


    public static boolean validateLoginC(String username, String enteredPassword) {
        String sql = "SELECT * FROM Client WHERE username = ? AND password = ?";

        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, username);
            pstmt.setString(2, enteredPassword);

            try (ResultSet rs = pstmt.executeQuery()) {
                return rs.next();
            }

        } catch (SQLException e) {
            System.out.println("Login Error: " + e.getMessage());
            return false;
        }
    }

    public static boolean validateLoginW(String username, String enteredPassword) {
        String sql = "SELECT * FROM Worker WHERE username = ? AND password = ?";

        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, username);
            pstmt.setString(2, enteredPassword);

            try (ResultSet rs = pstmt.executeQuery()) {
                return rs.next();
            }

        } catch (SQLException e) {
            System.out.println("Login Error: " + e.getMessage());
            return false;
        }
    }

    public static boolean checkIfUsernameExistsC(String username) {
        String sql = "SELECT username FROM Client WHERE username = ?";

        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, username);

            try (ResultSet rs = pstmt.executeQuery()) {
                return rs.next();
            }

        } catch (SQLException e) {
            System.out.println("Username Check Error: " + e.getMessage());
            return false;
        }
    }

    public static boolean checkIfUsernameExistsW(String username) {
        String sql = "SELECT username FROM Worker WHERE username = ?";

        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, username);

            try (ResultSet rs = pstmt.executeQuery()) {
                return rs.next();
            }

        } catch (SQLException e) {
            System.out.println("Username Check Error: " + e.getMessage());
            return false;
        }
    }

    public static boolean addClient(String username, String enteredPassword, String email, String userType) {
        String sql = "INSERT INTO Client (username, password, email, user_type, first_name, last_name) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, username);
            pstmt.setString(2, enteredPassword);
            pstmt.setString(3, email);
            pstmt.setString(4, userType);
            pstmt.setString(5, "");
            pstmt.setString(6, "");

            int rows = pstmt.executeUpdate();
            return rows > 0;

        } catch (SQLException e) {
            System.out.println("Insert User Error: " + e.getMessage());
            return false;
        }
    }

    public static boolean addWorker(String username, String enteredPassword, String email, String userType) {
        String sql = "INSERT INTO Worker (username, password, email, user_type, first_name, last_name) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, username);
            pstmt.setString(2, enteredPassword);
            pstmt.setString(3, email);
            pstmt.setString(4, userType);
            pstmt.setString(5, "");
            pstmt.setString(6, "");

            int rows = pstmt.executeUpdate();
            return rows > 0;

        } catch (SQLException e) {
            System.out.println("Insert User Error: " + e.getMessage());
            return false;
        }
    }
}