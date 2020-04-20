import org.apache.log4j.Logger;

import java.util.LinkedList;


public class Agent {
    public enum WeightNameEnum {
        DOWCLOSING(0),
        DOWCLOSINGPERCENTCHANGE(1),
        UNEMPLOYMENTRATE(2),
        UNEMPLOYMENTRATECHANGE(3),
        LABORRATE(4),
        LABORRATEPERCENTCHANGE(5),
        BORROWEDMONEY(6),
        BORROWEDMONEYPERCENTCHANGE(7),
        OFFSET(8);

        private final int value;
        private WeightNameEnum(int value) {
            this.value = value;
        }
    }

    static final int WEIGHT_SIZE = WeightNameEnum.values().length;

    private Weight[] weights = new Weight[WEIGHT_SIZE];
    private double fitnessValueDowPrediction = 0;
    private double standardDeviation = Double.POSITIVE_INFINITY;
    final static Logger logger = Logger.getLogger(Agent.class);

    public Agent(Weight[] weights) throws Exception {
        if (weights.length < WEIGHT_SIZE) {
            String message = "not enough weights";
            logger.error(message);
            throw new Exception(message);
        }
        for (int i=0; i<WEIGHT_SIZE; i++) {
            this.weights[i] = weights[i];
        }
        this.fitnessValueDowPrediction = calculateFitnessPredictingDow();
        logger.trace("new agent created with fitness value " + fitnessValueDowPrediction);
    }

    private double calculateFitnessPredictingDow() {
        LinkedList<DataSample> allData = InputData.getAllDataList();

        double totalDifference = 0;
        int i=-1;
        double[] prevData = new double[WEIGHT_SIZE-1];
        for (int j=0; j<WEIGHT_SIZE-1; j++) {
            prevData[j] = 0;
        }
        double[] differences = new double[allData.size()];
        for (DataSample dataSample : allData) {
            i++;
            if (i == 0)
            {
                prevData[WeightNameEnum.DOWCLOSING.value] = dataSample.dowJonesClosing;
                prevData[WeightNameEnum.DOWCLOSINGPERCENTCHANGE.value] = 0.0;
                prevData[WeightNameEnum.UNEMPLOYMENTRATE.value] = dataSample.unemploymentRate;
                prevData[WeightNameEnum.UNEMPLOYMENTRATECHANGE.value] = 0.0;
                prevData[WeightNameEnum.LABORRATE.value] = dataSample.civilianParticipationRate;
                prevData[WeightNameEnum.LABORRATEPERCENTCHANGE.value] = 0.0;
                prevData[WeightNameEnum.BORROWEDMONEY.value] = dataSample.moneyBorrowed;
                prevData[WeightNameEnum.BORROWEDMONEYPERCENTCHANGE.value] = 0.0;
                continue;
            }

            double estimatedPercentIncrease = getOffset().findValue();
            for (int j=0; j<WEIGHT_SIZE-1; j++) {
                estimatedPercentIncrease += weights[j].findValue() * prevData[j];
            }
            double actualDowPercentIncrease = (dataSample.dowJonesClosing - prevData[WeightNameEnum.DOWCLOSING.value]) / prevData[WeightNameEnum.DOWCLOSING.value] * 100.0;
            double error = Math.abs(estimatedPercentIncrease - actualDowPercentIncrease);
            totalDifference += error;
            differences[i] = error;
            prevData[WeightNameEnum.DOWCLOSING.value] = dataSample.dowJonesClosing;
            prevData[WeightNameEnum.DOWCLOSINGPERCENTCHANGE.value] = dataSample.dowJonesClosingPercent;
            prevData[WeightNameEnum.UNEMPLOYMENTRATE.value] = dataSample.unemploymentRate;
            prevData[WeightNameEnum.UNEMPLOYMENTRATECHANGE.value] = dataSample.unemploymentRatePercentChange;
            prevData[WeightNameEnum.LABORRATE.value] = dataSample.civilianParticipationRate;
            prevData[WeightNameEnum.LABORRATEPERCENTCHANGE.value] = dataSample.civilianParticipationRatePercentChange;
            prevData[WeightNameEnum.BORROWEDMONEY.value] = dataSample.moneyBorrowed;
            prevData[WeightNameEnum.BORROWEDMONEYPERCENTCHANGE.value] = dataSample.moneyBorrowedPercentChange;
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
        return weights[WeightNameEnum.DOWCLOSING.value];
    }

    public Weight getLastDowClosingPercentMultiplier() {
        return weights[WeightNameEnum.DOWCLOSINGPERCENTCHANGE.value];
    }

    public Weight getLastUnemploymentRateMultiplier() {
        return weights[WeightNameEnum.UNEMPLOYMENTRATE.value];
    }

    public Weight getLastUnemploymentRatePercentChangeMultiplier() {
        return weights[WeightNameEnum.UNEMPLOYMENTRATECHANGE.value];
    }

    public Weight getLastCivilianParticipationRateMultiplier() {
        return weights[WeightNameEnum.LABORRATE.value];
    }

    public Weight getLastCivilianParticipationRateChangeMultiplier() {
        return weights[WeightNameEnum.LABORRATEPERCENTCHANGE.value];
    }

    public Weight getBorrowedMoneyMultiplier() {
        return weights[WeightNameEnum.BORROWEDMONEY.value];
    }

    public Weight getBorrowedMoneyRateChangeMultiplier() {
        return weights[WeightNameEnum.BORROWEDMONEYPERCENTCHANGE.value];
    }


    public Weight getOffset() {
        return weights[WeightNameEnum.OFFSET.value];
    }

    public double getStandardDeviation() {
        return standardDeviation;
    }



}
