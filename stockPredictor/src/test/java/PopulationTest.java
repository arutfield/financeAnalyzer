import org.junit.Test;

import static org.junit.Assert.*;

public class PopulationTest {
    @Test
    public void createPopulation() throws Exception {
        Population population = new Population("target\\classes\\DJI.csv", 10000);
        Agent agent = population.findBestAgent();
        System.out.println("best agent weight: " + agent.getLastDowClosingMultiplier().printWeightValue());
        assertTrue(true);
    }

    @Test
    public void findBestPair() throws Exception {
        Population population = new Population("target\\classes\\DJI.csv", 10000);
        Agent[] agents = population.findFittestPair();
        System.out.println("best agent weight: " + agents[0].getLastDowClosingMultiplier().printWeightValue());
        System.out.println("best agent 2 weight: " + agents[1].getLastDowClosingMultiplier().printWeightValue());
        assertTrue(agents[0].getFitnessValueDowPrediction() < agents[1].getFitnessValueDowPrediction());
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
        Population population = new Population("target\\classes\\DJI.csv", 1);
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
        Population population = new Population("target\\classes\\DJI.csv", 10000);
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
        Population population = new Population("target\\classes\\DJI.csv", 10000);
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
            Population population = new Population("target\\classes\\DJI.csv", 8);
            Agent[] agents = population.getWorstAgents(10);
            assertFalse(false);
        } catch (Exception ex) {
            assertTrue(true);
        }
    }


    @Test
    public void testGetGenerations() throws Exception {
        int popSize = 200;
        int numberOfGenerations = 35;
        int numberOfNewChildren = 60;
        Population population = new Population("target\\classes\\DJI.csv", popSize);
        Agent bestAgent = population.getBestOfGenerations(numberOfGenerations, numberOfNewChildren);

        Population population2 = new Population("target\\classes\\DJI.csv", popSize);
        Agent bestAgent2 = population2.getBestOfGenerations(numberOfGenerations, numberOfNewChildren);

        System.out.println("best weight: " + bestAgent.getLastDowClosingMultiplier().findValue()
                + ", " + bestAgent2.getLastDowClosingMultiplier().findValue());
        assertEquals(bestAgent.getLastDowClosingMultiplier().findValue(),
                bestAgent2.getLastDowClosingMultiplier().findValue(), 0.002);
    }
}
