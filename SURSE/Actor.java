import java.util.List;

public class Actor implements Comparable<Actor> {
    protected String actorName;
    protected List<NameType> performances;
    protected String biography;

    public Actor(String actorName, List<NameType> performances, String biography) {
        this.actorName = actorName;
        this.performances = performances;
        this.biography = biography;
    }

    //getters:
    public String getActorName() {
        return this.actorName;
    }
    public List<NameType> getPerformances() {
        return this.performances;
    }
    public String getBiography() {
        return this.biography;
    }

    //setters:
    public void setActorName(String actorName) {
        this.actorName = actorName;
    }
    public void setPerformances(List<NameType> performances) {
        this.performances = performances;
    }
    public void setBiography(String biography) {
        this.biography = biography;
    }

    public void displayInfo() {
        System.out.println("Actor name: " + this.actorName);
        System.out.println("Performances: ");
        for (NameType nameType : this.performances) {
            System.out.println("---------------------");
            System.out.println("Title: " + nameType.getTitle());
            System.out.println("Type: " + nameType.getType());
        }
        System.out.println("Biography: " + this.biography);
        System.out.println();
    }

    @Override
    public int compareTo(Actor o) {
        return this.actorName.compareTo(o.actorName);
    }
}