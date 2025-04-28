public class ProductionAddedStrategy implements ExperienceStrategy {
    private int experience;

    public ProductionAddedStrategy(int experience) {
        this.experience = experience;
    }

    @Override
    public int calculateExperience() {
        return experience + 5;
    }
}
