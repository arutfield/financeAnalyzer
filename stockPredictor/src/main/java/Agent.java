import org.apache.log4j.Logger;

import java.util.Collections;
import java.util.LinkedList;
import java.util.Map;

public class Agent {

    private Weight lastDowClosingMultiplier;
    private double fitnessValueDowPrediction = 0;
    final static Logger logger = Logger.getLogger(Agent.class);

    public Agent(Weight lastDowClosingMultiplier) {
        this.lastDowClosingMultiplier = lastDowClosingMultiplier;
        this.fitnessValueDowPrediction = calculateFitnessPredictingDow(InputData.getDowJonesClosingList());
        logger.trace("new agent created with last dow multiplier of " + lastDowClosingMultiplier.findValue()
                + " and fitness value " + fitnessValueDowPrediction);
    }

    private double calculateFitnessPredictingDow(LinkedList<Double> dowClosingData) {
        double difference = 0;
        int i=-1;
        double prevData = 0;
        for (Double dowData : dowClosingData) {
            i++;
            if (i == 0)
            {
                prevData = dowData;
                continue;
            }
            double estimate = lastDowClosingMultiplier.findValue() * prevData;
            difference += Math.abs(estimate - dowData);
            prevData = dowData;
        }
        fitnessValueDowPrediction = difference;
        return fitnessValueDowPrediction;
    }

    public double getFitnessValueDowPrediction() {
        return fitnessValueDowPrediction;
    }

    public Weight getLastDowClosingMultiplier() {
        return lastDowClosingMultiplier;
    }
}
