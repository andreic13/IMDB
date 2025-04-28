import java.util.List;
import java.util.Map;

public class Series extends Production{
    private Long seriesReleaseYear;
    private Long seriesNumberOfSeasons;
    private Map<String, List<Episode>> seriesSeasonsMap;

    public Series(String productionTitle, List<String> productionDirectors, List<String> productionActors,
                  List<Genre> productionGenres, List<Rating> productionRatings, String productionDescription,
                  Long seriesReleaseYear, Long seriesNumberOfSeasons, Map<String, List<Episode>> seriesSeasonsMap) {
        super(productionTitle, productionDirectors, productionActors,
                productionGenres, productionRatings, productionDescription);
        this.seriesReleaseYear = seriesReleaseYear;
        this.seriesNumberOfSeasons = seriesNumberOfSeasons;
        this.seriesSeasonsMap = seriesSeasonsMap;
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
        System.out.println("Release year: " + this.seriesReleaseYear);
        System.out.println("Number of seasons: " + this.seriesNumberOfSeasons);
        //System.out.println("Seasons map: " + this.seriesSeasonsMap);
        System.out.println("Seasons map: ");
        for (Map.Entry<String, List<Episode>> entry : this.seriesSeasonsMap.entrySet()) {
            System.out.println("---------------------");
            System.out.println("Season: " + entry.getKey());
            System.out.println("Episodes: ");
            for (Episode episode : entry.getValue()) {
                System.out.println("*********************");
                System.out.println("Episode name: " + episode.getEpisodeName());
                System.out.println("Episode length: " + episode.getEpisodeLength());
                System.out.println("*********************");
            }
            System.out.println("---------------------");
        }
    }

    //Setter
    public void setSeriesReleaseYear(Long seriesReleaseYear) {
        this.seriesReleaseYear = seriesReleaseYear;
    }
    public void setSeriesNumberOfSeasons(Long seriesNumberOfSeasons) {
        this.seriesNumberOfSeasons = seriesNumberOfSeasons;
    }
    public void setSeriesSeasonsMap(Map<String, List<Episode>> seriesSeasonsMap) {
        this.seriesSeasonsMap = seriesSeasonsMap;
    }
}