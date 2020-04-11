import org.junit.Test;

import static org.junit.Assert.*;

public class PopulationTest {
    @Test
    public void createPopulation() throws Exception {
        Population population = createPopulation( 10000);
        Agent agent = population.findBestAgent();
        System.out.println("best agent weight: " + agent.getLastDowClosingMultiplier().printWeightValue());
        assertTrue(true);
    }

    @Test
    public void findBestPair() throws Exception {
        Population population = createPopulation(10000);
        Agent[] agents = population.findFittestPair();
        System.out.println("best agent fitness: " + agents[0].getFitnessValueDowPrediction());
        System.out.println("best agent 2 fitness: " + agents[1].getFitnessValueDowPrediction());
        System.out.println("difference: a-b: " + (agents[0].getFitnessValueDowPrediction() - agents[1].getFitnessValueDowPrediction()));
        assertTrue(agents[0].getFitnessValueDowPrediction() <= agents[1].getFitnessValueDowPrediction());
        int count = 0;
        for (Agent agent : population.getAgents()) {
            if (agent.getFitnessValueDowPrediction() < agents[1].getFitnessValueDowPrediction()) {
                count++;
                if (count > 1) {
                    assertTrue(false);
                }
            }
            if (agent.getFitnessValueDowPrediction() < agents[0].getFitnessValueDowPrediction()) {
                System.out.println("better weight found: " + agent.getFitnessValueDowPrediction() + " versus fitness "
                + agents[1].getFitnessValueDowPrediction());
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
        System.out.println("worst agent weight: " + agents[0].getLastDowClosingMultiplier().printWeightValue());
        System.out.println("worst agent 2 weight: " + agents[1].getLastDowClosingMultiplier().printWeightValue());
        assertTrue(agents[0].getFitnessValueDowPrediction() > agents[1].getFitnessValueDowPrediction());
        int count = 0;
        for (Agent agent : population.getAgents()) {
            if (agent.getFitnessValueDowPrediction() > agents[1].getFitnessValueDowPrediction()) {
                count++;
                if (count > 1) {
                    assertTrue(false);
                }
            }
            if (agent.getFitnessValueDowPrediction() > agents[0].getFitnessValueDowPrediction()) {
                System.out.println("worse weight found: " + agent.getFitnessValueDowPrediction() + " versus fitness "
                        + agents[1].getFitnessValueDowPrediction());
                assertTrue(false);
            }
        }
        assertTrue(true);
    }

    @Test
    public void findWorstTen() throws Exception {
        Population population = createPopulation(10000);
        Agent[] agents = population.getWorstAgents(10);
        System.out.println("worst agent weight: " + agents[0].getLastDowClosingMultiplier().printWeightValue());
        System.out.println("worst agent 2 weight: " + agents[1].getLastDowClosingMultiplier().printWeightValue());
        assertTrue(agents[0].getFitnessValueDowPrediction() > agents[1].getFitnessValueDowPrediction());
        int count = 0;
        for (int i= 1; i<10; i++) {
                System.out.println("fitness to compare: " + agents[i].getFitnessValueDowPrediction()
                        + ", " + agents[i-1].getFitnessValueDowPrediction());
                assertTrue(agents[i].getFitnessValueDowPrediction() <= agents[i-1].getFitnessValueDowPrediction());
        }
        for (Agent agent : population.getAgents()) {
            if (agent.getFitnessValueDowPrediction() > agents[9].getFitnessValueDowPrediction()) {
                count++;
                if (count > 9) {
                    assertTrue(false);
                }
            }
            if (agent.getFitnessValueDowPrediction() > agents[0].getFitnessValueDowPrediction()) {
                System.out.println("worse weight found: " + agent.getFitnessValueDowPrediction() + " versus fitness "
                        + agents[1].getFitnessValueDowPrediction());
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
    public void testGetGenerations() throws Exception {
        int popSize = 500;
        int numberOfGenerations = 1000;
        int numberOfNewChildren = 300;
        Population population = createPopulation(popSize);
        Agent bestAgent = population.getBestOfGenerations(numberOfGenerations, numberOfNewChildren);

        Population population2 = createPopulation(popSize);
        Agent bestAgent2 = population2.getBestOfGenerations(numberOfGenerations, numberOfNewChildren);

        System.out.println("agent 1 weights: " + bestAgent.getLastDowClosingMultiplier().findValue()
                + ", " + bestAgent.getLastDowClosingPercentMultiplier().findValue()
                + ", " + bestAgent.getLastUnemploymentRateMultiplier().findValue());
        System.out.println("agent 2 weights: " + bestAgent2.getLastDowClosingMultiplier().findValue()
                + ", " + bestAgent2.getLastDowClosingPercentMultiplier().findValue()
                + ", " + bestAgent2.getLastUnemploymentRateMultiplier().findValue());
        assertEquals(bestAgent.getFitnessValueDowPrediction(),
                bestAgent2.getFitnessValueDowPrediction(), 0.0002);

    }

    @Test
    public void testGoalAlgorithm() throws Exception {
        Population population = createPopulation(600);
        Agent bestAgent = population.getGoalFitnessFunction(0.49500717182709576, 400, 4000);
        assertTrue(bestAgent.getFitnessValueDowPrediction() <= 0.49500717182709576);
    }


    @Test
    public void tightenGoalAlgorithmConstantDow() throws Exception {
        double goalValue = 1e-6;
        Population population = new Population("target\\classes\\constantDow.csv", "target\\classes\\UNRATE.csv",
                "target\\classes\\CIVPART.csv", "target\\classes\\bankBorrowing.csv", 500);;
        Agent bestAgent = population.getGoalFitnessFunction(goalValue, 300, 10000);
        assertTrue(bestAgent.getFitnessValueDowPrediction() <= goalValue);
        assertEquals(bestAgent.getLastUnemploymentRateMultiplier().findValue(), 0, 1e-7);
        assertEquals(bestAgent.getLastDowClosingPercentMultiplier().findValue(), 0, 1e-7);
        assertEquals(bestAgent.getLastDowClosingMultiplier().findValue(), 0, 1e-7);
        assertEquals(bestAgent.getBorrowedMoneyMultiplier().findValue(), 0, 1e-7);
        assertEquals(bestAgent.getBorrowedMoneyRateChangeMultiplier().findValue(), 0, 1e-7);
        assertEquals(bestAgent.getLastCivilianParticipationRateChangeMultiplier().findValue(), 0, 1e-7);
        assertEquals(bestAgent.getLastCivilianParticipationRateMultiplier().findValue(), 0, 1e-7);
        assertEquals(bestAgent.getLastUnemploymentRatePercentChangeMultiplier().findValue(), 0, 1e-7);
        assertEquals(bestAgent.getOffset().findValue(), 0, 1e-7);

    }

    @Test
    public void tightenGoalAlgorithmDow0Point001Percent() throws Exception {
        double goalValue = 6e-5;
        Population population = new Population("target\\classes\\Dow0Point001.csv", "target\\classes\\UNRATE.csv",
                "target\\classes\\CIVPART.csv", "target\\classes\\bankBorrowing.csv", 500);
        InputData.printFullDowJonesClosing();
        Byte zeroByte = 0;
        Weight emptyWeight = new Weight(false, zeroByte, zeroByte, zeroByte, zeroByte, zeroByte, zeroByte, zeroByte, zeroByte);
        Weight[] weights = new Weight[Agent.WEIGHT_SIZE];
        for (int i=0; i<weights.length; i++) {
            weights[i] = emptyWeight;
        }
        weights[1] = new Weight(false, (byte) 1, zeroByte, zeroByte, zeroByte, zeroByte, zeroByte, zeroByte, zeroByte);
        //Agent goodAgent = new Agent(weights);
        //population.addAgent(goodAgent);
        //Agent goodAgent2 = new Agent(weights);
        //population.addAgent(goodAgent2);
        Agent bestAgent = population.getGoalFitnessFunction(goalValue, 300, 10000);
        assertTrue(bestAgent.getFitnessValueDowPrediction() <= goalValue);
        /*assertEquals(bestAgent.getLastUnemploymentRateMultiplier().findValue(), 0, 1e-7);
        assertEquals(bestAgent.getLastDowClosingPercentMultiplier().findValue(), 0, 1e-7);
        assertEquals(bestAgent.getLastDowClosingMultiplier().findValue(), 0, 1e-7);
        assertEquals(bestAgent.getBorrowedMoneyMultiplier().findValue(), 0, 1e-7);
        assertEquals(bestAgent.getBorrowedMoneyRateChangeMultiplier().findValue(), 0, 1e-7);
        assertEquals(bestAgent.getLastCivilianParticipationRateChangeMultiplier().findValue(), 0, 1e-7);
        assertEquals(bestAgent.getLastCivilianParticipationRateMultiplier().findValue(), 0, 1e-7);
        assertEquals(bestAgent.getLastUnemploymentRatePercentChangeMultiplier().findValue(), 0, 1e-7);
        assertEquals(bestAgent.getOffset().findValue(), 0.1, 1e-7);*/
    }


    private Population createPopulation(int popSize) throws Exception {
      return new Population("target\\classes\\DJI.csv", "target\\classes\\UNRATE.csv",
              "target\\classes\\CIVPART.csv", "target\\classes\\bankBorrowing.csv", popSize);
    }
}
