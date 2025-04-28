import java.util.List;

public class Movie extends Production{
    private Long movieLength;
    private Long movieReleaseYear;

    public Movie(String productionTitle, List<String> productionDirectors, List<String> productionActors,
                 List<Genre> productionGenres, List<Rating> productionRatings, String productionDescription,
                 Long movieLength, Long movieReleaseYear) {
        super(productionTitle, productionDirectors, productionActors, productionGenres,
                productionRatings, productionDescription);
        this.movieLength = movieLength;
        this.movieReleaseYear = movieReleaseYear;
    }

    @Override
    public void displayInfo() {
        System.out.println("Title: " + super.productionTitle);
        System.out.println("Directors: " + super.productionDirectors);
        System.out.println("Actors: " + super.productionActors);
        System.out.println("Genres: " + super.productionGenres);
        System.out.println("-------Ratings-------");
        for (Rating rating : super.productionRatings) {
            System.out.println("Username: " + rating.getUsername());
            System.out.println("Rating: " + rating.getOneRating());
            System.out.println("Comment: " + rating.getComment());
            System.out.println("---");
        }
        System.out.println("---------------------");
        System.out.println("Description: " + super.productionDescription);
        System.out.println("Overall rating: " + super.productionOverallRating);
        System.out.println("Length: " + this.movieLength);
        System.out.println("Release year: " + this.movieReleaseYear);
    }

    //Setter
    public void setMovieLength(Long movieLength) {
        this.movieLength = movieLength;
    }
    public void setMovieReleaseYear(Long movieReleaseYear) {
        this.movieReleaseYear = movieReleaseYear;
    }
}