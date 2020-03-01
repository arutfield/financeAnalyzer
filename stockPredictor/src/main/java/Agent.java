import java.util.Map;

public class Agent {
    private Weight lastDowMultiplier;
    private double fitnessValueDowPrediction = 0;

    public Agent(Weight lastDowMultiplier, Map<Integer, Double>  dowData) {
        this.lastDowMultiplier = lastDowMultiplier;
        this.fitnessValueDowPrediction = calculateFitnessPredictingDow(dowData);
    }

    private double calculateFitnessPredictingDow(Map<Integer, Double> dowData) {
        return 0;
    }
}
