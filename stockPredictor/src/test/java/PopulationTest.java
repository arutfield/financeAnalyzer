import org.junit.Test;

import static org.junit.Assert.*;

public class PopulationTest {
    @Test
    public void createPopulation() throws Exception {
        Population population = createPopulation( 10000);
        Agent agent = population.findBestAgent();
        System.out.println("best agent weight: " + agent.getWeight(Agent.WeightNameEnum.DOWCLOSING).printWeightValue());
        assertTrue(true);
    }

    @Test
    public void findBestPair() throws Exception {
        Population population = createPopulation(10000);
        Agent[] agents = population.findFittestPair();
        System.out.println("best agent fitness: " + agents[0].getFitnessValue());
        System.out.println("best agent 2 fitness: " + agents[1].getFitnessValue());
        System.out.println("difference: a-b: " + (agents[0].getFitnessValue() - agents[1].getFitnessValue()));
        assertTrue(agents[0].getFitnessValue() <= agents[1].getFitnessValue());
        int count = 0;
        for (Agent agent : population.getAgents()) {
            if (agent.getFitnessValue() < agents[1].getFitnessValue()) {
                count++;
                if (count > 1) {
                    assertTrue(false);
                }
            }
            if (agent.getFitnessValue() < agents[0].getFitnessValue()) {
                System.out.println("better weight found: " + agent.getFitnessValue() + " versus fitness "
                + agents[1].getFitnessValue());
                assertTrue(false);
            }
        }
        assertTrue(true);
    }


    @Test
    public void findBestPairError() throws Exception {
        Population population = createPopulation(1);
        boolean success = true;
        try {

            Agent[] agents = population.findFittestPair();

        } catch (Exception ex) {
            success = false;
        }
        assertFalse(success);
    }

    @Test
    public void findWorstPair() throws Exception {
        Population population = createPopulation(10000);
        Agent[] agents = population.getWorstAgents(2);
        System.out.println("worst agent weight: " + agents[0].getWeight(Agent.WeightNameEnum.DOWCLOSING).printWeightValue());
        System.out.println("worst agent 2 weight: " + agents[1].getWeight(Agent.WeightNameEnum.DOWCLOSING).printWeightValue());
        assertTrue(agents[0].getFitnessValue() > agents[1].getFitnessValue());
        int count = 0;
        for (Agent agent : population.getAgents()) {
            if (agent.getFitnessValue() > agents[1].getFitnessValue()) {
                count++;
                if (count > 1) {
                    assertTrue(false);
                }
            }
            if (agent.getFitnessValue() > agents[0].getFitnessValue()) {
                System.out.println("worse weight found: " + agent.getFitnessValue() + " versus fitness "
                        + agents[1].getFitnessValue());
                assertTrue(false);
            }
        }
        assertTrue(true);
    }

    @Test
    public void findWorstTen() throws Exception {
        Population population = createPopulation(10000);
        Agent[] agents = population.getWorstAgents(10);
        System.out.println("worst agent weight: " + agents[0].getWeight(Agent.WeightNameEnum.DOWCLOSING).printWeightValue());
        System.out.println("worst agent 2 weight: " + agents[1].getWeight(Agent.WeightNameEnum.DOWCLOSING).printWeightValue());
        assertTrue(agents[0].getFitnessValue() > agents[1].getFitnessValue());
        int count = 0;
        for (int i= 1; i<10; i++) {
                System.out.println("fitness to compare: " + agents[i].getFitnessValue()
                        + ", " + agents[i-1].getFitnessValue());
                assertTrue(agents[i].getFitnessValue() <= agents[i-1].getFitnessValue());
        }
        for (Agent agent : population.getAgents()) {
            if (agent.getFitnessValue() > agents[9].getFitnessValue()) {
                count++;
                if (count > 9) {
                    assertTrue(false);
                }
            }
            if (agent.getFitnessValue() > agents[0].getFitnessValue()) {
                System.out.println("worse weight found: " + agent.getFitnessValue() + " versus fitness "
                        + agents[1].getFitnessValue());
                assertTrue(false);
            }
        }
        assertTrue(true);
    }

    @Test
    public void findWorstError() {
        try {
            Population population = createPopulation(8);
            Agent[] agents = population.getWorstAgents(10);
            assertFalse(false);
        } catch (Exception ex) {
            assertTrue(true);
        }
    }


    @Test
    public void testGoalAlgorithm() throws Exception {
        Population population = createPopulation(600);
        InputData.printFullInformation();
        double goalValue = 0.7251;
        Agent bestAgent = population.getGoalFitnessFunction(goalValue, 400, 4000);
        assertTrue(bestAgent.getFitnessValue() <= goalValue);
    }


    @Test
    public void tightenGoalAlgorithmConstantDow() throws Exception {
        double goalValue = 1e-6;
        Population population = new Population("target\\classes\\constantDow.csv",
                "target\\classes\\constantDow.csv", "target\\classes\\UNRATE.csv",
                "target\\classes\\CIVPART.csv", "target\\classes\\bankBorrowing.csv", 500);;
        Agent bestAgent = population.getGoalFitnessFunction(goalValue, 300, 10000);
        assertTrue(bestAgent.getFitnessValue() <= goalValue);
        for (Agent.WeightNameEnum weightNameEnum : Agent.WeightNameEnum.values()) {
            assertEquals(bestAgent.getWeight(weightNameEnum).findValue(), 0, 1e-7);
        }

    }

    @Test
    public void tightenGoalAlgorithm1MillionthCurrentDown() throws Exception {
        double goalValue = 6e-5;
        Population population = new Population("target\\classes\\DowOneMillionthCurrent.csv",
                "target\\classes\\DowOneMillionthCurrent.csv", "target\\classes\\UNRATE.csv",
                "target\\classes\\CIVPART.csv", "target\\classes\\bankBorrowing.csv", 500);
        InputData.printFullInformation();
        Byte zeroByte = 0;
        Weight emptyWeight = new Weight(false, zeroByte, zeroByte, zeroByte, zeroByte, zeroByte, zeroByte, zeroByte, zeroByte);
        Weight[] weights = new Weight[Agent.WEIGHT_SIZE];
        for (int i=0; i<weights.length; i++) {
            weights[i] = emptyWeight;
        }
        weights[Agent.WeightNameEnum.DOWCLOSING.ordinal()] = new Weight(false, zeroByte, zeroByte, zeroByte,  zeroByte,  zeroByte, zeroByte, (byte) 1, zeroByte);
        Agent goodAgent = new Agent(weights);
        population.addAgent(goodAgent);
        Agent bestAgent = population.getGoalFitnessFunction(goalValue, 300, 10000);
        assertTrue(bestAgent.getFitnessValue() <= goalValue);
        assertEquals(bestAgent, goodAgent);
    }

    @Test
    public void tightenGoalAlgorithmDow0Point001Percent() throws Exception {
        double goalValue = 6e-5;
        Population population = new Population("target\\classes\\Dow0Point001.csv",
                "target\\classes\\Dow0Point001.csv", "target\\classes\\UNRATE.csv",
                "target\\classes\\CIVPART.csv", "target\\classes\\bankBorrowing.csv", 500);
        InputData.printFullInformation();
        Byte zeroByte = 0;
        Weight emptyWeight = new Weight(false, zeroByte, zeroByte, zeroByte, zeroByte, zeroByte, zeroByte, zeroByte, zeroByte);
        Weight[] weights = new Weight[Agent.WEIGHT_SIZE];
        for (int i=0; i<weights.length; i++) {
            weights[i] = emptyWeight;
        }
        weights[1] = new Weight(false, (byte) 1, zeroByte, zeroByte, zeroByte,  zeroByte, zeroByte, zeroByte, zeroByte);
        Agent goodAgent = new Agent(weights);
        population.addAgent(goodAgent);
        Agent bestAgent = population.getGoalFitnessFunction(goalValue, 300, 10000);
        assertTrue(bestAgent.getFitnessValue() <= goalValue);
        assertEquals(bestAgent, goodAgent);
    }

    @Test
    public void tightenGoalAlgorithmOneTenthUnemploymentRate() throws Exception {
        double goalValue = 6e-5;
        Population population = new Population("target\\classes\\OneTenthUnemployment.csv",
                "target\\classes\\OneTenthUnemployment.csv", "target\\classes\\UNRATE.csv",
                "target\\classes\\CIVPART.csv", "target\\classes\\bankBorrowing.csv", 500);
        InputData.printFullInformation();
        Byte zeroByte = 0;
        Weight emptyWeight = new Weight(false, zeroByte, zeroByte, zeroByte, zeroByte, zeroByte, zeroByte, zeroByte, zeroByte);
        Weight[] weights = new Weight[Agent.WEIGHT_SIZE];
        for (int i=0; i<weights.length; i++) {
            weights[i] = emptyWeight;
        }
        weights[Agent.WeightNameEnum.UNEMPLOYMENTRATE.ordinal()] = new Weight(false, zeroByte, (byte) 1, zeroByte, zeroByte,  zeroByte,  zeroByte, zeroByte,  zeroByte);
        Agent goodAgent = new Agent(weights);
        population.addAgent(goodAgent);
        Agent bestAgent = population.getGoalFitnessFunction(goalValue, 300, 10000);
        assertTrue(bestAgent.getFitnessValue() <= goalValue);
        assertEquals(bestAgent, goodAgent);
    }

    @Test
    public void tightenGoalAlgorithmTwoUnemploymentRateChange() throws Exception {
        double goalValue = 6e-5;
        Population population = new Population("target\\classes\\TwoUnemploymentChange.csv","target\\classes\\TwoUnemploymentChange.csv", "target\\classes\\UNRATE.csv",
                "target\\classes\\CIVPART.csv", "target\\classes\\bankBorrowing.csv", 500);
        InputData.printFullInformation();
        Byte zeroByte = 0;
        Weight emptyWeight = new Weight(false, zeroByte, zeroByte, zeroByte, zeroByte, zeroByte, zeroByte, zeroByte, zeroByte);
        Weight[] weights = new Weight[Agent.WEIGHT_SIZE];
        for (int i=0; i<weights.length; i++) {
            weights[i] = emptyWeight;
        }
        weights[Agent.WeightNameEnum.UNEMPLOYMENTRATECHANGE.ordinal()] = new Weight(false, (byte) 2,  zeroByte,zeroByte, zeroByte,  zeroByte,  zeroByte, zeroByte,  zeroByte);
        Agent goodAgent = new Agent(weights);
        population.addAgent(goodAgent);
        Agent bestAgent = population.getGoalFitnessFunction(goalValue, 300, 10000);
        assertTrue(bestAgent.getFitnessValue() <= goalValue);
        assertEquals(bestAgent, goodAgent);
    }

    @Test
    public void tightenGoalAlgorithmLaborRateDow() throws Exception {
        double goalValue = 2e-4;
        Population population = new Population("target\\classes\\DowLaborRate.csv",
                "target\\classes\\DowLaborRate.csv", "target\\classes\\UNRATE.csv",
                "target\\classes\\CIVPART.csv", "target\\classes\\bankBorrowing.csv", 500);
        Byte zeroByte = 0;
        Weight[] weightArray = new Weight[Agent.WEIGHT_SIZE];
        for (int i=0; i<weightArray.length; i++) {
            weightArray[i] = new Weight(false, zeroByte, zeroByte, zeroByte, zeroByte, zeroByte,zeroByte, zeroByte, zeroByte);
        }
        weightArray[Agent.WeightNameEnum.LABORRATE.ordinal()] = new Weight(false, zeroByte, zeroByte, (byte) 1, zeroByte, zeroByte, zeroByte, zeroByte, zeroByte);
        Agent goodAgent = new Agent(weightArray);
        population.addAgent(goodAgent);
        Agent bestAgent = population.getGoalFitnessFunction(goalValue, 300, 10000);
        assertTrue(bestAgent.getFitnessValue() <= goalValue);
        assertEquals(bestAgent, goodAgent);
    }

    @Test
    public void tightenGoalAlgorithmLaborRateChangeDow() throws Exception {
        double goalValue = 2e-4;
        Population population = new Population("target\\classes\\DowLaborRatePercentChangeMinusFive.csv",
                "target\\classes\\DowLaborRatePercentChangeMinusFive.csv", "target\\classes\\UNRATE.csv",
                "target\\classes\\CIVPART.csv", "target\\classes\\bankBorrowing.csv", 500);
        Byte zeroByte = 0;
        Weight[] weightArray = new Weight[Agent.WEIGHT_SIZE];
        for (int i=0; i<weightArray.length; i++) {
            weightArray[i] = new Weight(false, zeroByte, zeroByte, zeroByte, zeroByte, zeroByte,zeroByte, zeroByte, zeroByte);
        }
        weightArray[Agent.WeightNameEnum.LABORRATEPERCENTCHANGE.ordinal()] = new Weight(true, (byte) 5, zeroByte, zeroByte, zeroByte, zeroByte, zeroByte, zeroByte, zeroByte);
        Agent goodAgent = new Agent(weightArray);
        population.addAgent(goodAgent);
        Agent bestAgent = population.getGoalFitnessFunction(goalValue, 300, 10000);
        assertTrue(bestAgent.getFitnessValue() <= goalValue);
        assertEquals(bestAgent, goodAgent);

    }

    @Test
    public void tightenGoalAlgorithmMoneyBorrowedDow() throws Exception {
        double goalValue = 2e-4;
        Population population = new Population("target\\classes\\BorrowedMoneySevenPointOne.csv",
                "target\\classes\\BorrowedMoneySevenPointOne.csv", "target\\classes\\UNRATE.csv",
                "target\\classes\\CIVPART.csv", "target\\classes\\bankBorrowing.csv", 500);
        Byte zeroByte = 0;
        Weight[] weightArray = new Weight[Agent.WEIGHT_SIZE];
        for (int i=0; i<weightArray.length; i++) {
            weightArray[i] = new Weight(false, zeroByte, zeroByte, zeroByte, zeroByte, zeroByte,zeroByte, zeroByte, zeroByte);
        }
        weightArray[Agent.WeightNameEnum.BORROWEDMONEY.ordinal()] = new Weight(false,  zeroByte, zeroByte, zeroByte, zeroByte, zeroByte, zeroByte, (byte) 7, (byte) 1);
        Agent goodAgent = new Agent(weightArray);
        population.addAgent(goodAgent);
        Agent bestAgent = population.getGoalFitnessFunction(goalValue, 300, 10000);
        assertTrue(bestAgent.getFitnessValue() <= goalValue);
        assertEquals(bestAgent, goodAgent);
    }

    @Test
    public void tightenGoalAlgorithmMoneyBorrowedChangeDow() throws Exception {
        double goalValue = 2e-4;
        Population population = new Population("target\\classes\\BorrowedMoneyNegThreePointTwo.csv",
                "target\\classes\\BorrowedMoneyNegThreePointTwo.csv", "target\\classes\\UNRATE.csv",
                "target\\classes\\CIVPART.csv", "target\\classes\\bankBorrowing.csv", 500);
        Byte zeroByte = 0;
        Weight[] weightArray = new Weight[Agent.WEIGHT_SIZE];
        for (int i=0; i<weightArray.length; i++) {
            weightArray[i] = new Weight(false, zeroByte, zeroByte, zeroByte, zeroByte, zeroByte,zeroByte, zeroByte, zeroByte);
        }
        weightArray[Agent.WeightNameEnum.BORROWEDMONEYPERCENTCHANGE.ordinal()] = new Weight(true,  zeroByte, zeroByte, (byte) 3, (byte) 2, zeroByte, zeroByte, zeroByte, zeroByte);
        Agent goodAgent = new Agent(weightArray);
        population.addAgent(goodAgent);
        Agent bestAgent = population.getGoalFitnessFunction(goalValue, 300, 10000);
        assertTrue(bestAgent.getFitnessValue() <= goalValue);
        assertEquals(bestAgent, goodAgent);
    }


    @Test
    public void tightenGoalAlgorithmMoneyBorrowedChangeAndDow() throws Exception {
        double goalValue = 2e-4;
        Population population = new Population("target\\classes\\BorrowedMoneyNegThreePointTwoDowMinus51.csv",
                "target\\classes\\BorrowedMoneyNegThreePointTwoDowMinus51.csv", "target\\classes\\UNRATE.csv",
                "target\\classes\\CIVPART.csv", "target\\classes\\bankBorrowing.csv", 500);
        Byte zeroByte = 0;
        Weight[] weightArray = new Weight[Agent.WEIGHT_SIZE];
        for (int i=0; i<weightArray.length; i++) {
            weightArray[i] = new Weight(false, zeroByte, zeroByte, zeroByte, zeroByte, zeroByte,zeroByte, zeroByte, zeroByte);
        }
        weightArray[Agent.WeightNameEnum.DOWCLOSING.ordinal()] = new Weight(true, zeroByte, zeroByte, (byte) 5, (byte) 1,  zeroByte, zeroByte, zeroByte, zeroByte);
        weightArray[Agent.WeightNameEnum.BORROWEDMONEYPERCENTCHANGE.ordinal()] = new Weight(true,  zeroByte, zeroByte, (byte) 3, (byte) 2, zeroByte, zeroByte, zeroByte, zeroByte);
        Agent goodAgent = new Agent(weightArray);
        population.addAgent(goodAgent);
        Agent bestAgent = population.getGoalFitnessFunction(goalValue, 300, 10000);
        assertTrue(bestAgent.getFitnessValue() <= goalValue);
        assertEquals(bestAgent, goodAgent);
    }

    @Test
    public void tightenGoalAlgorithmDow0Point001PercentOffset() throws Exception {
        double goalValue = 6e-5;
        Population population = new Population("target\\classes\\Dow0Point001.csv", "target\\classes\\Dow0Point001.csv","target\\classes\\UNRATE.csv",
                "target\\classes\\CIVPART.csv", "target\\classes\\bankBorrowing.csv", 500);
        InputData.printFullInformation();
        Byte zeroByte = 0;
        Weight emptyWeight = new Weight(false, zeroByte, zeroByte, zeroByte, zeroByte, zeroByte, zeroByte, zeroByte, zeroByte);
        Weight[] weights = new Weight[Agent.WEIGHT_SIZE];
        for (int i=0; i<weights.length; i++) {
            weights[i] = emptyWeight;
        }
        weights[Agent.WeightNameEnum.OFFSET.ordinal()] = new Weight(false, zeroByte, (byte) 1,  zeroByte, zeroByte,  zeroByte, zeroByte, zeroByte, zeroByte);
        Agent goodAgent = new Agent(weights);
        population.addAgent(goodAgent);
        Agent bestAgent = population.getGoalFitnessFunction(goalValue, 300, 10000);
        assertTrue(bestAgent.getFitnessValue() <= goalValue);
        assertEquals(bestAgent, goodAgent);
    }


    private Population createPopulation(int popSize) throws Exception {
      return new Population("target\\classes\\FDN.csv","target\\classes\\DJI.csv", "target\\classes\\UNRATE.csv",
              "target\\classes\\CIVPART.csv", "target\\classes\\bankBorrowing.csv", popSize);
    }
}
