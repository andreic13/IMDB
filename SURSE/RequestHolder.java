import java.util.ArrayList;
import java.util.List;

public class RequestHolder {
    public static List<Request> allRequests = new ArrayList<>();

    public static void addRequest(Request req) {
        allRequests.add(req);
    }

    public static void removeRequest(Request req) {
        allRequests.remove(req);
    }

    public static void displayRequests() {
        for(Request req : allRequests) {
            System.out.println("Type: " + req.getType());
            System.out.println("Date: " + req.getDate());
            System.out.println("Request user: " + req.getRequestUser());
            if (req.getType().equals(RequestType.ACTOR_ISSUE)) {
                System.out.println("Actor name: " + req.getTitle_name());
            } else if (req.getType().equals(RequestType.MOVIE_ISSUE)) {
                System.out.println("Movie title: " + req.getTitle_name());
            }
            System.out.println("Solving user: " + req.getSolvingUser());
            System.out.println("Description: " + req.getDescription());
            System.out.println();
        }
    }
}