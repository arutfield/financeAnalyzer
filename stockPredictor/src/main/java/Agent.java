import org.apache.log4j.Logger;

import java.util.LinkedList;

public class Agent {

    private Weight lastDowClosingMultiplier;
    private Weight lastDowClosingPercentMultiplier;
    private Weight lastUnemploymentRateMultiplier;
    private double fitnessValueDowPrediction = 0;
    final static Logger logger = Logger.getLogger(Agent.class);

    public Agent(Weight lastDowClosingMultiplier, Weight lastDowClosingPercentMultiplier, Weight lastUnemploymentRateMultiplier) {
        this.lastDowClosingMultiplier = lastDowClosingMultiplier;
        this.lastDowClosingPercentMultiplier = lastDowClosingPercentMultiplier;
        this.lastUnemploymentRateMultiplier = lastUnemploymentRateMultiplier;
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
        double prevUnemploymentRate = 0;
        for (DataSample dataSample : dowClosingData) {
            i++;
            if (i == 0)
            {
                prevData = dataSample.dowJonesClosing;
                prevPercentData = 0.0;
                prevUnemploymentRate = dataSample.unemploymentRate;
                continue;
            }
            double estimate = lastDowClosingMultiplier.findValue() * prevData
                    + lastDowClosingPercentMultiplier.findValue() * prevPercentData
                    + lastUnemploymentRateMultiplier.findValue() * prevUnemploymentRate;
            double predictedDow = prevData * (1.0 + estimate);
            difference += Math.abs(predictedDow - dataSample.dowJonesClosing);
            prevData = dataSample.dowJonesClosing;
            prevPercentData = dataSample.dowJonesClosingPercent;
            prevUnemploymentRate = dataSample.unemploymentRate;
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

    public Weight getLastUnemploymentRateMultiplier() {
        return lastUnemploymentRateMultiplier;
    }


}
