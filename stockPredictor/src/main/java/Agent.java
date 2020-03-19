import org.apache.log4j.Logger;

import java.util.LinkedList;

public class Agent {

    private Weight lastDowClosingMultiplier;
    private Weight lastDowClosingPercentMultiplier;
    private double fitnessValueDowPrediction = 0;
    final static Logger logger = Logger.getLogger(Agent.class);

    public Agent(Weight lastDowClosingMultiplier, Weight lastDowClosingPercentMultiplier) {
        this.lastDowClosingMultiplier = lastDowClosingMultiplier;
        this.lastDowClosingPercentMultiplier = lastDowClosingPercentMultiplier;
        this.fitnessValueDowPrediction = calculateFitnessPredictingDow();
        logger.trace("new agent created with last dow multiplier of " + lastDowClosingMultiplier.findValue()
                + " and fitness value " + fitnessValueDowPrediction);
    }

    private double calculateFitnessPredictingDow() {
        LinkedList<DataSample> dowClosingData = InputData.getAllDataList();

        double difference = 0;
        int i=-1;
        double prevData = 0;
        double prevPercentData = 0;
        for (DataSample dataSample : dowClosingData) {
            i++;
            if (i == 0)
            {
                prevData = dataSample.dowJonesClosing;
               // prevPercentData =
                continue;
            }
            double estimate = lastDowClosingMultiplier.findValue() * prevData
                    + lastDowClosingPercentMultiplier.findValue() * prevPercentData;
            difference += Math.abs(estimate - dataSample.dowJonesClosing);
            prevData = dataSample.dowJonesClosing;
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

    public Weight getLastDowClosingPercentMultiplier() {
        return lastDowClosingPercentMultiplier;
    }

}
