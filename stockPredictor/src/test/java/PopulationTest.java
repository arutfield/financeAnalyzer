import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

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

}
