public class Rating {
    private String username;
    private Long oneRating;
    private String comment;

    public Rating(String username, Long oneRating, String comment) {
        this.username = username;
        this.oneRating = oneRating;
        this.comment = comment;
    }

    //getter:
    public Long getOneRating() {
        return this.oneRating;
    }
    public String getUsername() {
        return this.username;
    }
    public String getComment() {
        return this.comment;
    }
}