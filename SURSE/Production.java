import java.util.List;

public abstract class Production implements Comparable<Production> {
    protected String productionTitle;
    protected List<String> productionDirectors;
    protected List<String> productionActors;
    protected List<Genre> productionGenres;
    protected List<Rating> productionRatings;
    protected String productionDescription;
    protected Double productionOverallRating;

    //datele citite din fisiere:
    public Production(String productionTitle, List<String> productionDirectors, List<String> productionActors, List<Genre> productionGenres,
                      List<Rating> productionRatings, String productionDescription) {
        this.productionTitle = productionTitle;
        this.productionDirectors = productionDirectors;
        this.productionActors = productionActors;
        this.productionGenres = productionGenres;
        this.productionRatings = productionRatings;
        this.productionDescription = productionDescription;
        this.productionOverallRating = calcOverallRating(this.productionRatings);
    }

    private Double calcOverallRating(List<Rating> movieRatings) {
        Double movieOverallRating;
        movieOverallRating = 0.0;
        for(Rating rating : movieRatings) {
            movieOverallRating += rating.getOneRating();
        }
        return movieOverallRating / movieRatings.size();
    }

    public abstract void displayInfo();

    public int compareTo(Production o) {
        return this.productionTitle.compareTo(o.productionTitle);
    }

    //Settere:
    public void setProductionTitle(String productionTitle) {
        this.productionTitle = productionTitle;
    }
    public void setProductionDirectors(List<String> productionDirectors) {
        this.productionDirectors = productionDirectors;
    }
    public void setProductionActors(List<String> productionActors) {
        this.productionActors = productionActors;
    }
    public void setProductionGenres(List<Genre> productionGenres) {
        this.productionGenres = productionGenres;
    }
    public void setProductionRatings(List<Rating> productionRatings) {
        this.productionRatings = productionRatings;
    }
    public void setProductionDescription(String productionDescription) {
        this.productionDescription = productionDescription;
    }

    //getter:
    public String getProductionTitle() {
        return this.productionTitle;
    }

    public List<Rating> getProductionRatings() {
        return this.productionRatings;
    }

    public void updateOverallRating() {
        this.productionOverallRating = calcOverallRating(this.productionRatings);

    }
}