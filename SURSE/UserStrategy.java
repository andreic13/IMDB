public class UserStrategy {
    private ExperienceStrategy experienceStrategy;

    public UserStrategy(ExperienceStrategy experienceStrategy) {
        this.experienceStrategy = experienceStrategy;
    }

    public int calculateExperience() {
        return experienceStrategy.calculateExperience();
    }
}
