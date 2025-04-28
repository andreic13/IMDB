import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;

public class Admin<T extends Comparable<T>> extends Staff<T> {
    public Admin(Information userInformation, AccountType type, String username, int experience,
                 List<String> notifications, SortedSet preferences, List<Request> todoRequests, SortedSet<T> addedByHim) {
        super(userInformation, type, username, experience, notifications, preferences, todoRequests, addedByHim);
    }

    @Override
    public void addPreferences(T elem) {
        getPreferences().add(elem);
    }

    @Override
    public void removePreferences(T elem) {
        getPreferences().remove(elem);
    }

    @Override
    public void updateExperience(int experience) {
        setExperience(experience);
    }

    @Override
    public int logout() {
        System.out.println("Do you want to loggout or exit the app?");
        System.out.println("1. Logout");
        System.out.println("2. Exit");
        Scanner scanner = new Scanner(System.in);
        int option = scanner.nextInt();
        switch (option) {
            case 1:
                System.out.println("logged out");
                return 0;
            case 2:
                System.out.println("exited");
                return 1;
            default:
                System.out.println("Invalid option!");
                return 0;
        }
    }

    public void addDeleteUserSystem(User currentUser) {
        Admin admin = (Admin) currentUser;
        System.out.println("Add or delete user: ");
        System.out.println("1. Add");
        System.out.println("2. Delete");
        Scanner scanner = new Scanner(System.in);
        int option = scanner.nextInt();
        scanner.nextLine(); //pentru a citi si \n
        switch (option) {
            case 1: //add
                System.out.println("Fill user Information: ");
                System.out.println();
                System.out.println("Email: ");
                String email = scanner.nextLine();
                try {
                    if (email.equals("")) {
                        throw new InformationIncompleteException("Email field is empty!");
                    }
                } catch (InformationIncompleteException e) {
                    System.out.println(e.getMessage());
                    break;
                }
                System.out.println("Password: ");
                String password = scanner.nextLine();
                try {
                    if (password.equals("")) {
                        throw new InformationIncompleteException("Password field is empty!");
                    }
                } catch (InformationIncompleteException e) {
                    System.out.println(e.getMessage());
                    break;
                }
                System.out.println("Name: ");
                String name = scanner.nextLine();
                try {
                    if (name.equals("")) {
                        throw new InformationIncompleteException("Name field is empty!");
                    }
                } catch (InformationIncompleteException e) {
                    System.out.println(e.getMessage());
                    break;
                }
                System.out.println("Country: ");
                String country = scanner.nextLine();
                System.out.println("Age: ");
                int age = scanner.nextInt();
                scanner.nextLine(); //pentru a citi si \n
                System.out.println("Gender: (F-Female/M-Male/N-Neutral)");
                char gender = scanner.nextLine().charAt(0);
                System.out.println("Birth date: (Type: yyyy-MM-dd HH:mm)");
                LocalDateTime birthDate = null;
                try {
                    String birthDateString = scanner.nextLine();
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
                    birthDate = LocalDateTime.parse(birthDateString, formatter);
                } catch (DateTimeParseException e) {
                    System.out.println("Wrong date format!");
                    break;
                }
                //Alcatuire information:
                InformationBuilder informationBuilder = new InformationBuilder();
                informationBuilder.setCredentials(new Credentials(email, password));
                informationBuilder.setName(name);
                informationBuilder.setCountry(country);
                informationBuilder.setAge(age);
                informationBuilder.setGender(gender);
                informationBuilder.setBirthDate(birthDate);
                User.Information userInformation = informationBuilder.build();

                System.out.println("Account type: ");
                System.out.println("1. Regular");
                System.out.println("2. Contributor");
                System.out.println("3. Admin");
                int option2 = scanner.nextInt();
                scanner.nextLine(); //pentru a citi si \n
                AccountType accountType = null;
                switch (option2) {
                    case 1: //add regular
                        accountType = AccountType.REGULAR;
                        break;
                    case 2: //add contributor
                        accountType = AccountType.CONTRIBUTOR;
                        break;
                    case 3: //add admin
                        accountType = AccountType.ADMIN;
                        break;
                }

                System.out.println("Username: ");
                String username = scanner.nextLine();
                System.out.println("Experience: ");
                int experience = scanner.nextInt();
                scanner.nextLine(); //pentru a citi si \n

                //Notifications, preferences si contributions sunt initial goale:
                List<String> notifications = new ArrayList<>();
                SortedSet<Object> preferences = new TreeSet(new Comparator<Object>() {
                    @Override
                    public int compare(Object o1, Object o2) {
                        if (o1 instanceof Production && o2 instanceof Production) {
                            Production p1 = (Production) o1;
                            Production p2 = (Production) o2;
                            return p1.productionTitle.compareTo(p2.productionTitle);
                        } else if (o1 instanceof Actor && o2 instanceof Actor) {
                            Actor a1 = (Actor) o1;
                            Actor a2 = (Actor) o2;
                            return a1.getActorName().compareTo(a2.actorName);
                        } else if (o1 instanceof Production && o2 instanceof Actor) {
                            Production p1 = (Production) o1;
                            Actor a2 = (Actor) o2;
                            return p1.getProductionTitle().compareTo(a2.actorName);
                        } else if (o1 instanceof Actor && o2 instanceof Production) {
                            Actor a1 = (Actor) o1;
                            Production p2 = (Production) o2;
                            return a1.getActorName().compareTo(p2.productionTitle);
                        } else {
                            return 0;
                        }
                    }
                });
                SortedSet<Object> addedByHim = new TreeSet(new Comparator<Object>() {
                    @Override
                    public int compare(Object o1, Object o2) {
                        if (o1 instanceof Production && o2 instanceof Production) {
                            Production p1 = (Production) o1;
                            Production p2 = (Production) o2;
                            return p1.productionTitle.compareTo(p2.productionTitle);
                        } else if (o1 instanceof Actor && o2 instanceof Actor) {
                            Actor a1 = (Actor) o1;
                            Actor a2 = (Actor) o2;
                            return a1.getActorName().compareTo(a2.actorName);
                        } else if (o1 instanceof Production && o2 instanceof Actor) {
                            Production p1 = (Production) o1;
                            Actor a2 = (Actor) o2;
                            return p1.getProductionTitle().compareTo(a2.actorName);
                        } else if (o1 instanceof Actor && o2 instanceof Production) {
                            Actor a1 = (Actor) o1;
                            Production p2 = (Production) o2;
                            return a1.getActorName().compareTo(p2.productionTitle);
                        } else {
                            return 0;
                        }
                    }
                });

                //Alcatuire user:
                User user = UserFactory.createUser(userInformation, accountType,
                        username, experience, notifications, preferences, addedByHim);
                IMDB.getInstance().allAccounts.add(user);

                System.out.println("User added!");
                break;
            case 2: //delete
                System.out.println("Type the username of the user you want to delete: ");
                String username2 = scanner.nextLine();
                int found = 0;
                for (User thisuser : IMDB.getInstance().allAccounts) {
                    if (thisuser.getUsername().equals(username2)) {
                        found = 1;
                        if(thisuser instanceof Admin) {
                            System.out.println("You can't delete an admin!");
                            break;
                        }
                        IMDB.getInstance().allAccounts.remove(thisuser);
                        System.out.println("User deleted!");
                        break;
                    }
                }
                if (found == 0) {
                    System.out.println("User not found!");
                }
                break;
        }
    }


}