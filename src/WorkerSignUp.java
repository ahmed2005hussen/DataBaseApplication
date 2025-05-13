public class WorkerSignUp implements SignUp {
    private authenticationService authService;

    public WorkerSignUp() {
        this.authService = new authenticationService();
    }

    @Override
    public boolean signUp(String username, String password, String email, String... additionalInfo) {
        if (additionalInfo.length < 2) {
            System.out.println("Missing required information for worker signup");
            return false;
        }

        String firstName = additionalInfo[0];
        String lastName = additionalInfo[1];

        return register(username, password, email, firstName, lastName);
    }

    @Override
    public boolean register(String username, String password, String email, String firstName, String lastName) {
        if (!validatePassword(password)) {
            System.out.println("Password validation failed: must be at least 6 characters");
            return false;
        }

        if (!authService.register(username, password, email, "worker")) {
            return false;
        }

        Worker worker = new Worker(username, firstName, lastName, email);
        return true;
    }

    private boolean validatePassword(String password) {
        return password.length() >= 6;
    }
}