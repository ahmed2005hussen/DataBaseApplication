public class ClientSignUp implements SignUp {
    private authenticationService authService;

    public ClientSignUp() {
        this.authService = new authenticationService();
    }

    @Override
    public boolean signUp(String username, String password, String email, String... additionalInfo) {
        if (additionalInfo.length < 2) {
            System.out.println("Missing required information for client signup");
            return false;
        }

        String firstName = additionalInfo[0];
        String lastName = additionalInfo[1];

        return register(username, password, email, firstName, lastName);
    }

    @Override
    public boolean register(String username, String password, String email, String firstName, String lastName) {
        if (!authService.register(username, password, email, "client")) {
            return false;
        }

        Client client = new Client(username, firstName, lastName, email);
        return true;
    }
}