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
            Agent agent = new Agent(weight, inputData.getDowJonesClosingList());
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

    /**
     * find the best two agents
     * @return best two agents
     * @throws Exception not enough agents
     */
    public Agent[] findFittestPair() throws Exception {
        if (agents.size() < 2) {
            String message = "less than two agents found. Only " + agents.size() + " agents";
            logger.error(message);
            throw new Exception(message);
        }
        Agent[] bestPair = new Agent[2];
        double bestFitnessFunction = Double.POSITIVE_INFINITY;
        double secondBestFitnessFunction = Double.POSITIVE_INFINITY;
        for (Agent agent : agents) {
            if (agent.getFitnessValueDowPrediction() > secondBestFitnessFunction) {
                continue;
            } else {
                if (agent.getFitnessValueDowPrediction() > bestFitnessFunction
                        && agent.getFitnessValueDowPrediction() < secondBestFitnessFunction) {
                    // between best and second best
                    secondBestFitnessFunction = agent.getFitnessValueDowPrediction();
                    bestPair[1] = agent;
                    logger.trace("new second best weight: " + agent.getLastDowClosingMultiplier().printWeightValue());
                } else {
                    // new best parent
                    secondBestFitnessFunction = bestFitnessFunction;
                    bestFitnessFunction = agent.getFitnessValueDowPrediction();
                    bestPair[1] = bestPair[0];
                    bestPair[0] = agent;
                    logger.trace("new best weight: " + agent.getLastDowClosingMultiplier().printWeightValue());
                }
            }
        }
        return bestPair;
    }

    public LinkedList<Agent> getAgents() {
        return agents;
    }

}
