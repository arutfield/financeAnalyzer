import org.apache.log4j.Logger;

import java.util.LinkedList;


public class Agent {
    private static final int WEIGHT_SIZE = 6;
    private Weight lastDowClosingMultiplier;
    private Weight lastDowClosingPercentMultiplier;
    private Weight lastUnemploymentRateMultiplier;
    private Weight lastUnemploymentRatePercentChangeMultiplier;
    private Weight lastCivilianParticipationRateMultiplier;
    private Weight lastCivilianParticipationRateChangeMultiplier;
    private double fitnessValueDowPrediction = 0;
    final static Logger logger = Logger.getLogger(Agent.class);

    public Agent(Weight[] weights) throws Exception {
        if (weights.length < WEIGHT_SIZE) {
            String message = "not enough weights";
            logger.error(message);
            throw new Exception(message);
        }
        this.lastDowClosingMultiplier = weights[0];
        this.lastDowClosingPercentMultiplier = weights[1];
        this.lastUnemploymentRateMultiplier = weights[2];
        this.lastUnemploymentRatePercentChangeMultiplier = weights[3];
        this.lastCivilianParticipationRateMultiplier = weights[4];
        this.lastCivilianParticipationRateChangeMultiplier = weights[5];
        this.fitnessValueDowPrediction = calculateFitnessPredictingDow();
        logger.trace("new agent created with last dow multiplier of " + lastDowClosingMultiplier.findValue()
                + " and fitness value " + fitnessValueDowPrediction);
    }

    private double calculateFitnessPredictingDow() {
        LinkedList<DataSample> allData = InputData.getAllDataList();

        double difference = 0;
        int i=-1;
        double prevData = 0;
        double prevPercentData = 0;
        double prevUnemploymentRate = 0;
        double prevCivilianRate = 0;
        double prevPrediction = 0;
        double prevUnemploymentRatePercentChange = 0;
        double prevCivilianRatePercentChange = 0;
        for (DataSample dataSample : allData) {
            i++;
            if (i == 0)
            {
                prevPrediction = dataSample.dowJonesClosing;
                prevData = dataSample.dowJonesClosing;
                prevPercentData = 0.0;
                prevUnemploymentRate = dataSample.unemploymentRate;
                prevUnemploymentRatePercentChange = 0.0;
                prevCivilianRate = dataSample.civilianParticipationRate;
                prevCivilianRatePercentChange = 0.0;
                continue;
            }
            double estimate = lastDowClosingMultiplier.findValue() * prevData
                    + lastDowClosingPercentMultiplier.findValue() * prevPercentData
                    + lastUnemploymentRateMultiplier.findValue() * prevUnemploymentRate
                    + lastUnemploymentRatePercentChangeMultiplier.findValue() * prevUnemploymentRatePercentChange
                    + lastCivilianParticipationRateMultiplier.findValue() * prevCivilianRate
                    + lastCivilianParticipationRateChangeMultiplier.findValue() * prevCivilianRatePercentChange;
            double predictedDow = prevData * (1.0 + estimate);
            difference += Math.abs(predictedDow - dataSample.dowJonesClosing);
            prevData = dataSample.dowJonesClosing;
            prevPercentData = dataSample.dowJonesClosingPercent;
            prevUnemploymentRate = dataSample.unemploymentRate;
            prevUnemploymentRatePercentChange = dataSample.unemploymentRatePercentChange;
            prevCivilianRatePercentChange = dataSample.civilianParticipationRatePercentChange;
            prevPrediction = predictedDow;
        }
        fitnessValueDowPrediction = difference / ((double) allData.size());
        if (fitnessValueDowPrediction == Double.POSITIVE_INFINITY) {
            fitnessValueDowPrediction = Double.MAX_VALUE;
        }
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

    public Weight getLastUnemploymentRatePercentChangeMultiplier() {
        return lastUnemploymentRatePercentChangeMultiplier;
    }

    public Weight getLastCivilianParticipationRateMultiplier() {
        return lastCivilianParticipationRateMultiplier;
    }

    public Weight getLastCivilianParticipationRateChangeMultiplier() {
        return lastCivilianParticipationRateChangeMultiplier;
    }
}
