import org.apache.log4j.Logger;

import java.util.LinkedList;


public class Agent {
    public enum WeightNameEnum {
        CURRENTSTOCKCLOSING(0),
        CURRENTSTOCKPERCENTCHANGE(1),
        DOWCLOSING(2),
        DOWCLOSINGPERCENTCHANGE(3),
        UNEMPLOYMENTRATE(4),
        UNEMPLOYMENTRATECHANGE(5),
        LABORRATE(6),
        LABORRATEPERCENTCHANGE(7),
        BORROWEDMONEY(8),
        BORROWEDMONEYPERCENTCHANGE(9),
        OFFSET(10);

        private final int value;
        private WeightNameEnum(int value) {
            this.value = value;
        }
    }

    static final int WEIGHT_SIZE = WeightNameEnum.values().length;

    private Weight[] weights = new Weight[WEIGHT_SIZE];
    private double fitnessValue = 0;
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
        this.fitnessValue = calculateFitness();
        logger.trace("new agent created with fitness value " + fitnessValue);
    }

    private double calculateFitness() {
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
                prevData[WeightNameEnum.CURRENTSTOCKCLOSING.value] = dataSample.currentStockClosing;
                prevData[WeightNameEnum.CURRENTSTOCKPERCENTCHANGE.value] = 0.0;
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

            double estimatedPercentIncrease = getWeight(WeightNameEnum.OFFSET).findValue();
            for (int j=0; j<WEIGHT_SIZE-1; j++) {
                estimatedPercentIncrease += weights[j].findValue() * prevData[j];
            }
            double actualPercentIncrease = dataSample.currentStockClosingPercent;
            double error = Math.abs(estimatedPercentIncrease - actualPercentIncrease);
            totalDifference += error;
            differences[i] = error;
            prevData[WeightNameEnum.CURRENTSTOCKCLOSING.value] = dataSample.currentStockClosing;
            prevData[WeightNameEnum.CURRENTSTOCKPERCENTCHANGE.value] = dataSample.currentStockClosingPercent;
            prevData[WeightNameEnum.DOWCLOSING.value] = dataSample.dowJonesClosing;
            prevData[WeightNameEnum.DOWCLOSINGPERCENTCHANGE.value] = dataSample.dowJonesClosingPercent;
            prevData[WeightNameEnum.UNEMPLOYMENTRATE.value] = dataSample.unemploymentRate;
            prevData[WeightNameEnum.UNEMPLOYMENTRATECHANGE.value] = dataSample.unemploymentRatePercentChange;
            prevData[WeightNameEnum.LABORRATE.value] = dataSample.civilianParticipationRate;
            prevData[WeightNameEnum.LABORRATEPERCENTCHANGE.value] = dataSample.civilianParticipationRatePercentChange;
            prevData[WeightNameEnum.BORROWEDMONEY.value] = dataSample.moneyBorrowed;
            prevData[WeightNameEnum.BORROWEDMONEYPERCENTCHANGE.value] = dataSample.moneyBorrowedPercentChange;
        }
        fitnessValue = totalDifference / ((double) allData.size());
        if (fitnessValue == Double.POSITIVE_INFINITY) {
            fitnessValue = Double.MAX_VALUE;
        }
        calculateStandardDeviation(differences, fitnessValue);
        return fitnessValue;
    }

    private void calculateStandardDeviation(double[] differences, double mean) {
        double total = 0;
        for (double diff : differences) {
            total += (diff - mean)*(diff - mean);
        }
        standardDeviation = Math.sqrt(total/((double) differences.length));
    }


    public double getFitnessValue() {
        return fitnessValue;
    }

    public Weight getWeight(WeightNameEnum weightName) {
        return weights[weightName.value];
    }

    public double getStandardDeviation() {
        return standardDeviation;
    }



}
