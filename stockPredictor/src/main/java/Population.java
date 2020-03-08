import org.apache.log4j.Logger;

import java.util.LinkedList;

public class Population {
    LinkedList<Agent> agents = new LinkedList<>();
    final static Logger logger = Logger.getLogger(Population.class);

    public Population(String dowJonesDataFile, int numberOfAgents) throws Exception {
        if (numberOfAgents < 1) {
            String message = "need at least one agent. " + numberOfAgents + " is unusable";
            logger.error(message);
            throw new Exception(message);
        }
        InputData inputData = new InputData(dowJonesDataFile);
        for (int i=0; i<numberOfAgents; i++) {
            Weight weight = new Weight();
            Agent agent = new Agent(weight, inputData.getDowJonesClosingMap());
            agents.add(agent);
            logger.trace("agent created with weight " + weight.printWeightValue());
        }
    }

    /**
     * find the agent with the smallest fitness function
     * @return best agent
     */
    public Agent findBestAgent() {
        Agent bestAgent = null;
        double bestFitnessFunction = Double.POSITIVE_INFINITY;
        for (Agent agent : agents) {
            if (agent.getFitnessValueDowPrediction() < bestFitnessFunction) {
                bestAgent = agent;
                bestFitnessFunction = agent.getFitnessValueDowPrediction();
            }
        }
        return bestAgent;
    }
}
