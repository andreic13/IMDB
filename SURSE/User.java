import java.time.LocalDateTime;
import java.util.List;
import java.util.SortedSet;

public abstract class User<T> implements Observer {
    private Information userInformation;
    private AccountType type;
    private String username;
    private int experience;
    private List<String> notifications;
    private SortedSet preferences;

    public User(Information userInformation, AccountType type, String username, int experience,
                List<String> notifications, SortedSet preferences) {
        this.userInformation = userInformation;
        this.type = type;
        this.username = username;
        this.experience = experience;
        this.notifications = notifications;
        this.preferences = preferences;
    }

    public abstract void addPreferences(T elem);

    public abstract void removePreferences(T elem);

    public abstract void updateExperience(int experience);

    public abstract int logout();

    //Getter:
    public SortedSet<T> getPreferences() {
        return this.preferences;
    }
    public Information getUserInformation() {return this.userInformation; }
    public String getUsername() {
        return this.username;
    }
    public int getExperience() {
        return this.experience;
    }
    public AccountType getAccountType() {
        return this.type;
    }
    public List<String> getNotifications() {
        return this.notifications;
    }

    //Setter:
    public void setExperience(int experience) {
        this.experience = experience;
    }

    static class Information{
        private Credentials userCredentials;
        private String name;
        private String country;
        private int age;
        private char gender;
        private LocalDateTime birthDate;

        public Information (Credentials userCredentials, String name, String country,
                            int age, char gender, LocalDateTime birthDate) {
            this.userCredentials = userCredentials;
            this.name = name;
            this.country = country;
            this.age = age;
            this.gender = gender;
            this.birthDate = birthDate;
        }

        //Gettere:
        public Credentials getUserCredentials() {
            return userCredentials;
        }
        public String getName() {
            return name;
        }

        public String getCountry() {
            return country;
        }

        public int getAge() {
            return age;
        }

        public char getGender() {
            return gender;
        }

        public LocalDateTime getBirthDate() {
            return birthDate;
        }
    }

    //ObserverPattern:
    @Override
    public void update(String notification) {
        this.notifications.add(notification);
    }
}