public class RequestStrategy implements ExperienceStrategy {
    private int experience;

    public RequestStrategy(int experience) {
        this.experience = experience;
    }

    @Override
    public int calculateExperience() {
        return experience + 5;
    }
}
