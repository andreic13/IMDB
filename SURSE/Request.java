import java.time.LocalDateTime;

public class Request {
    private RequestType type;
    private LocalDateTime date;
    private String title_name;
    private String description;
    private String requestUser;
    private String solvingUser;

    public Request(RequestType type, String title_name, String description, LocalDateTime date,
                   String requestUser) {
        this.type = type;
        this.date = date;
        this.title_name = title_name;
        this.description = description;
        this.requestUser = requestUser;
        //atribuire solvingUser:
        switch (this.type) {
            case DELETE_ACCOUNT:
                this.solvingUser = "ADMIN";
                break;
            case OTHERS:
                this.solvingUser = "ADMIN";
                break;
            case ACTOR_ISSUE:
                for (User user : IMDB.getInstance().allAccounts) {
                    if (user instanceof Staff && this.title_name != null) {
                        Staff staff = (Staff) user;
                        if (staff.getAddedByHim() == null) {
                            continue;
                            //if staff added the actor:
                        }

                        for (Object contr : staff.getAddedByHim()) {
                            if (contr instanceof Actor) {
                                Actor actor = (Actor) contr;
                                if (actor.getActorName().equals(this.title_name)) {
                                    this.solvingUser = staff.getUsername();
                                    break;
                                }
                            }
                        }
                    }
                }
                break;
            case MOVIE_ISSUE:
                for (User user : IMDB.getInstance().allAccounts) {
                    if (user instanceof Staff && this.title_name != null) {
                        Staff staff = (Staff) user;
                        if (staff.getAddedByHim() == null) {
                            continue;
                        }

                        for (Object contr : staff.getAddedByHim()) {
                            if (contr instanceof Production) {
                                Production prod = (Production) contr;
                                if (prod.getProductionTitle().equals(this.title_name)) {
                                    this.solvingUser = staff.getUsername();
                                    break;
                                }
                            }
                        }
                    }
                }
                break;
            default:
                throw new IllegalArgumentException("requestType necunoscut " + this.type);
        }
    }

    //getter:
    public RequestType getType() {
        return this.type;
    }
    public LocalDateTime getDate() {
        return this.date;
    }
    public String getTitle_name() {
        return this.title_name;
    }
    public String getDescription() {
        return this.description;
    }
    public String getRequestUser() {
        return this.requestUser;
    }
    public String getSolvingUser() {
        return this.solvingUser;
    }
}