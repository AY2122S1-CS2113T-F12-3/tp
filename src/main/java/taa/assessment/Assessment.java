package taa.assessment;

public class Assessment {
    private String name;
    private double weightage;

    public Assessment(String name, double weightage) {
        this.name = name;
        this.weightage = weightage;
    }

    public String getName() {
        return name;
    }

    public double getWeightage() {
        return weightage;
    }

    /**
     * Format the string to print out the assessment.
     * For Midterms with 20% weightage,
     * this will be printed: Midterms (20.0%)
     *
     * @return Assessment string.
     */
    @Override
    public String toString() {
        return String.format("%s (%.1f%%)", name, weightage);
    }
}
