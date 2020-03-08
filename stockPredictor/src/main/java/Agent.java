import org.apache.log4j.Logger;

import java.util.Collections;
import java.util.Map;

public class Agent {
    private Weight lastDowClosingMultiplier;
    private double fitnessValueDowPrediction = 0;
    final static Logger logger = Logger.getLogger(Agent.class);

    public Agent(Weight lastDowClosingMultiplier,
                 Map<Integer, Double> dowData) {
        this.lastDowClosingMultiplier = lastDowClosingMultiplier;
        this.fitnessValueDowPrediction = calculateFitnessPredictingDow(dowData);
        logger.debug("new agent created with last dow multiplier of " + lastDowClosingMultiplier.findValue()
                + " and fitness value " + fitnessValueDowPrediction);
    }

    private double calculateFitnessPredictingDow(Map<Integer, Double> dowClosingData) {
        double difference = 0;
        for (int i = 1; i < Collections.max(dowClosingData.keySet()); i++) {
            double estimate = lastDowClosingMultiplier.findValue() * dowClosingData.get(i - 1);
            difference += Math.abs(estimate - dowClosingData.get(i));
        }
        fitnessValueDowPrediction = difference;
        return fitnessValueDowPrediction;
    }

    public double getFitnessValueDowPrediction() {
        return fitnessValueDowPrediction;
    }


}
