public class authenticationService {
    public boolean login(String username, String password) {
        if (Main.validateLoginC(username, password)) {
            return true;
        }
        return Main.validateLoginW(username, password);
    }

    public boolean register(String username, String password, String email, String userType) {
        if (Main.checkIfUsernameExistsC(username) || Main.checkIfUsernameExistsW(username)) {
            return false;
        }

        if (userType.equals("client")) {
            return Main.addClient(username, password, email, userType);
        } else if (userType.equals("worker")) {
            return Main.addWorker(username, password, email, userType);
        }

        return false;
    }
}