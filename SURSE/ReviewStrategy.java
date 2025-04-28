public class ReviewStrategy implements ExperienceStrategy {
    private int experience;

    public ReviewStrategy(int experience) {
        this.experience = experience;
    }

    @Override
    public int calculateExperience() {
        return experience + 1;
    }
}
