import java.util.List;
import java.util.Scanner;
import java.util.SortedSet;

public class Contributor<T extends Comparable<T>> extends Staff<T> implements RequestsManager {
    public Contributor(Information userInformation, AccountType type, String username, int experience,
                       List<String> notifications, SortedSet preferences, List<Request> todoRequests, SortedSet<T> addedByHim) {
        super(userInformation, type, username, experience, notifications, preferences, todoRequests, addedByHim);
    }

    @Override
    public void createRequest(Request r) {
        IMDB.getInstance().allRequests.add(r);

        //ObserverPattern:
        //notificare solving user:
        //caz admin:
        if (r.getSolvingUser().equals("ADMIN")) {
            RequestHolder.addRequest(r);

            for (User u : IMDB.getInstance().allAccounts) {
                //adaugare admini:
                if (u instanceof Admin) {
                    NotificationManager.getInstance().addObserver(u);
                }
            }
            //send notifications:
            NotificationManager.getInstance().notifyObservers("User: " + r.getRequestUser() +
                    " has a request for you!");
            for (User u : IMDB.getInstance().allAccounts) {
                //stergere admini:
                if (u instanceof Admin) {
                    NotificationManager.getInstance().removeObserver(u);
                }
            }
        } else if (r.getSolvingUser() != null) {
            //caz contributor:
            for (User u : IMDB.getInstance().allAccounts) {
                if (u instanceof Contributor) {
                    Contributor contributor = (Contributor) u;
                    if (contributor.getUsername().equals(r.getSolvingUser())) {
                        NotificationManager.getInstance().addObserver(u);
                        NotificationManager.getInstance().notifyObservers("User: " + r.getRequestUser() +
                                " has a request for you!");
                        NotificationManager.getInstance().removeObserver(u);
                        break;
                    }
                }
            }
        }
    }

    @Override
    public void removeRequest(Request r) {
        if(r.getSolvingUser().equals("ADMIN")) {
            RequestHolder.removeRequest(r);
        }
        IMDB.getInstance().allRequests.remove(r);
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
}
