public class Credentials {
    private String email;
    private String password;

    public Credentials(String email, String password) {
        this.email = email;
        this.password = password;
    }

    //Gettere:
    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
}