public interface SignUp {
    boolean signUp(String username, String password, String email, String... additionalInfo);

    boolean register(String username, String password, String email, String firstName, String lastName);
}