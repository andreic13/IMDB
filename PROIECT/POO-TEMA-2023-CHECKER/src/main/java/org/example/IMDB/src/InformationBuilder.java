import java.time.LocalDateTime;

public class InformationBuilder {
    private Credentials userCredentials;
    private String name;
    private String country;
    private int age;
    private char gender;
    private LocalDateTime birthDate;

    public InformationBuilder setCredentials(Credentials userCredentials) {
        this.userCredentials = userCredentials;
        return this;
    }

    public InformationBuilder setName(String name) {
        this.name = name;
        return this;
    }

    public InformationBuilder setCountry(String country) {
        this.country = country;
        return this;
    }

    public InformationBuilder setAge(int age) {
        this.age = age;
        return this;
    }

    public InformationBuilder setGender(char gender) {
        this.gender = gender;
        return this;
    }

    public InformationBuilder setBirthDate(LocalDateTime birthDate) {
        this.birthDate = birthDate;
        return this;
    }

    public User.Information build() {
        return new User.Information(userCredentials, name, country, age, gender, birthDate);
    }
}