import java.util.*;
import java.lang.*;

public class UserFactory {
    public static User createUser(User.Information userInformation, AccountType accountType,
                                  String username, int experience, List<String> notifications,
                                  SortedSet preferences, SortedSet addedByHim) {
        User user;
        switch (accountType) {
            case REGULAR:
                user = new Regular(userInformation, accountType, username, experience, notifications, preferences);
                break;
            case CONTRIBUTOR:
                user = new Contributor(userInformation, accountType,
                        username, experience, notifications, preferences, new ArrayList<Request>(), addedByHim);
                break;
            case ADMIN:
                user = new Admin(userInformation, accountType,
                        username, experience, notifications, preferences, new ArrayList<Request>(), addedByHim);
                break;
            default:
                throw new IllegalArgumentException("accountType necunoscut: " + accountType);
        }

        return user;
    }
}
