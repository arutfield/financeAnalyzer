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
        for (int i = 0; i < numberOfAgents; i++) {
            Weight weight = new Weight();
            Agent agent = new Agent(weight);
            agents.add(agent);
            logger.trace("agent created with weight " + weight.printWeightValue());
        }
        orderAgentsByFitness();
    }

    /**
     * find the agent with the smallest fitness function
     *
     * @return best agent
     */
    public Agent findBestAgent() {
        return agents.get(0);
    }

    /**
     * find the best two agents
     *
     * @return best two agents
     * @throws Exception not enough agents
     */
    public Agent[] findFittestPair() throws Exception {
        if (agents.size() < 2) {
            String message = "less than two agents found. Only " + agents.size() + " agents";
            logger.error(message);
            throw new Exception(message);
        }
        Agent[] bestPair = {agents.get(0), agents.get(1)};
        return bestPair;
    }

    private void orderAgentsByFitness() {
        LinkedList<Agent> orderedList = new LinkedList<>();
        int counter = 0;
        orderedList.add(agents.get(0));
        for (Agent agent : agents) {
            counter++;
            if (counter == 1) {
                continue;
            }
            int position = 0;
            for (Agent orderedAgent : orderedList) {
                if (agent.getFitnessValueDowPrediction() < orderedAgent.getFitnessValueDowPrediction()) {
                    orderedList.add(position, agent);
                    break;
                }
                position++;
            }
        }
        agents.clear();
        agents = orderedList;
    }

    public Agent[] getWorstAgents(int numberOfAgents) throws Exception {
        if (agents.size() < numberOfAgents) {
            String message = "less than " + numberOfAgents + " agents found. Only " + agents.size() + " agents";
            logger.error(message);
            throw new Exception(message);
        }
        Agent worstAgents[] = new Agent[numberOfAgents];
        for (int i = 0; i < numberOfAgents; i++) {
            // get agents back in reverse order from worst to best
            worstAgents[i] = agents.get(agents.size() - 1 - i);
        }
        return worstAgents;
    }

    public LinkedList<Agent> getAgents() {
        return agents;
    }

    private void generateChildren(int numberOfChildren) throws Exception {
        Agent[] parents = findFittestPair();
        //number of weight attributes is 6, so range should be [0,7)
        for (int i = 0; i < numberOfChildren; i++) {
            int crossPoint = (int) Math.floor(Math.random() * 7);
            Weight newDowMultiplier = new Weight(parents[0], parents[1], crossPoint);
            Agent agent = new Agent(newDowMultiplier);
            //replace worst agent that hasn't been replaced yet
            agents.set(agents.size() - i, agent);
        }
        orderAgentsByFitness();
    }
}
