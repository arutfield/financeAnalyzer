import org.apache.log4j.Logger;

import java.util.LinkedList;


public class Agent {
    static final int WEIGHT_SIZE = 9;
    private Weight lastDowClosingMultiplier;
    private Weight lastDowClosingPercentMultiplier;
    private Weight lastUnemploymentRateMultiplier;
    private Weight lastUnemploymentRatePercentChangeMultiplier;
    private Weight lastCivilianParticipationRateMultiplier;
    private Weight lastCivilianParticipationRateChangeMultiplier;
    private Weight borrowedMoneyMultiplier;
    private Weight borrowedMoneyRateChangeMultiplier;
    private Weight offset;
    private double fitnessValueDowPrediction = 0;
    private double standardDeviation = Double.POSITIVE_INFINITY;
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
        this.borrowedMoneyMultiplier = weights[6];
        this.borrowedMoneyRateChangeMultiplier = weights[7];
        this.offset = weights[8];
        this.fitnessValueDowPrediction = calculateFitnessPredictingDow();
        logger.trace("new agent created with last dow multiplier of " + lastDowClosingMultiplier.findValue()
                + " and fitness value " + fitnessValueDowPrediction);
    }

    private double calculateFitnessPredictingDow() {
        LinkedList<DataSample> allData = InputData.getAllDataList();

        double totalDifference = 0;
        int i=-1;
        double prevData = 0;
        double prevPercentData = 0;
        double prevUnemploymentRate = 0;
        double prevCivilianRate = 0;
        double prevBankBorrowed = 0;
        double prevUnemploymentRatePercentChange = 0;
        double prevCivilianRatePercentChange = 0;
        double prevBankBorrowedPercentChange = 0;
        double[] differences = new double[allData.size()];
        for (DataSample dataSample : allData) {
            i++;
            if (i == 0)
            {
                prevData = dataSample.dowJonesClosing;
                prevPercentData = 0.0;
                prevUnemploymentRate = dataSample.unemploymentRate;
                prevUnemploymentRatePercentChange = 0.0;
                prevCivilianRate = dataSample.civilianParticipationRate;
                prevCivilianRatePercentChange = 0.0;
                prevBankBorrowed = dataSample.moneyBorrowed;
                prevBankBorrowedPercentChange = 0.0;
                continue;
            }
            double estimatedPercentIncrease = lastDowClosingMultiplier.findValue() * prevData
                    + lastDowClosingPercentMultiplier.findValue() * prevPercentData
                    + lastUnemploymentRateMultiplier.findValue() * prevUnemploymentRate
                    + lastUnemploymentRatePercentChangeMultiplier.findValue() * prevUnemploymentRatePercentChange
                    + lastCivilianParticipationRateMultiplier.findValue() * prevCivilianRate
                    + lastCivilianParticipationRateChangeMultiplier.findValue() * prevCivilianRatePercentChange
                    + borrowedMoneyMultiplier.findValue() * prevBankBorrowed
                    + borrowedMoneyRateChangeMultiplier.findValue() * prevBankBorrowedPercentChange
                    + offset.findValue();
            double actualDowPercentIncrease = (dataSample.dowJonesClosing - prevData) / prevData * 100.0;
            double error = Math.abs(estimatedPercentIncrease - actualDowPercentIncrease);
            totalDifference += error;
            differences[i] = error;
            prevData = dataSample.dowJonesClosing;
            prevPercentData = dataSample.dowJonesClosingPercent;
            prevUnemploymentRate = dataSample.unemploymentRate;
            prevUnemploymentRatePercentChange = dataSample.unemploymentRatePercentChange;
            prevCivilianRatePercentChange = dataSample.civilianParticipationRatePercentChange;
            prevBankBorrowed = dataSample.moneyBorrowed;
            prevBankBorrowedPercentChange = dataSample.moneyBorrowedPercentChange;
        }
        fitnessValueDowPrediction = totalDifference / ((double) allData.size());
        if (fitnessValueDowPrediction == Double.POSITIVE_INFINITY) {
            fitnessValueDowPrediction = Double.MAX_VALUE;
        }
        calculateStandardDeviation(differences, fitnessValueDowPrediction);
        return fitnessValueDowPrediction;
    }

    private void calculateStandardDeviation(double[] differences, double mean) {
        double total = 0;
        for (double diff : differences) {
            total += (diff - mean)*(diff - mean);
        }
        standardDeviation = Math.sqrt(total/((double) differences.length));
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

    public Weight getBorrowedMoneyMultiplier() {
        return borrowedMoneyMultiplier;
    }

    public Weight getBorrowedMoneyRateChangeMultiplier() {
        return borrowedMoneyRateChangeMultiplier;
    }


    public Weight getOffset() {
        return offset;
    }

    public double getStandardDeviation() {
        return standardDeviation;
    }



}
