import org.apache.log4j.Logger;

import java.util.LinkedList;

public class Population {
    LinkedList<Agent> agents = new LinkedList<>();
    final static Logger logger = Logger.getLogger(Population.class);

    public Population(String dowJonesDataFile, String unemploymentDataFile, int numberOfAgents) throws Exception {
        if (numberOfAgents < 1) {
            String message = "need at least one agent. " + numberOfAgents + " is unusable";
            logger.error(message);
            throw new Exception(message);
        }
        InputData.loadDowJonesClosing(dowJonesDataFile, unemploymentDataFile);
        for (int i = 0; i < numberOfAgents; i++) {
            Weight weight1 = new Weight();
            Weight weight2 = new Weight();
            Weight weight3 = new Weight();
            Agent agent = new Agent(weight1, weight2, weight3);
            agents.add(agent);
            logger.trace("agent created with weights " + weight1.printWeightValue() + "," + weight2.printWeightValue());
        }
        orderAgentsByFitness();
    }

    public Agent getBestOfGenerations(int numberOfGenerations, int numberOfChildren) throws Exception {
        if (numberOfGenerations < 1) {
            String message = "need at least one generation, have " + numberOfGenerations;
            logger.error(message);
            throw new Exception(message);
        }
        for (int i=0; i<numberOfGenerations; i++) {
            generateChildren(numberOfChildren);
        }
        return findBestAgent();
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
            boolean foundSpot = false;
            for (Agent orderedAgent : orderedList) {
                if (agent.getFitnessValueDowPrediction() < orderedAgent.getFitnessValueDowPrediction()) {
                    orderedList.add(position, agent);
                    foundSpot = true;
                    break;
                }
                position++;
            }
            if (!foundSpot) {
                orderedList.add(agent);//add to end if it's the worst
            }
        }
        agents.clear();
        agents = orderedList;
        logger.debug("best agent in reordering: " + agents.get(0).getLastDowClosingMultiplier().findValue() + ", "
                + agents.get(0).getLastDowClosingPercentMultiplier().findValue() + ", "
                + agents.get(0).getLastUnemploymentRateMultiplier().findValue() + ", with value: "
                + agents.get(0).getFitnessValueDowPrediction());
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



    public void generateChildren(int numberOfChildren) throws Exception {
        Agent[] parents = findFittestPair();
        double mutationProbability = 0.2;
        //number of weight attributes is 6, so range should be [0,7]
        for (int i = 0; i < numberOfChildren; i++) {
            int crossPoint = (int) Math.floor(Math.random() * 8);
            Weight newDowMultiplier = new Weight(parents[0].getLastDowClosingMultiplier(),
                    parents[1].getLastDowClosingMultiplier(), crossPoint, mutationProbability);

            int crossPoint2 = (int) Math.floor(Math.random() * 8);
            Weight newDowPercentMultiplier = new Weight(parents[0].getLastDowClosingPercentMultiplier(),
                    parents[1].getLastDowClosingPercentMultiplier(), crossPoint2, mutationProbability);
            int crossPoint3 = (int) Math.floor(Math.random() * 8);
            Weight newUnemploymentRateMultiplier = new Weight(parents[0].getLastUnemploymentRateMultiplier(),
                    parents[1].getLastUnemploymentRateMultiplier(), crossPoint3, mutationProbability);
            Agent agent = new Agent(newDowMultiplier, newDowPercentMultiplier, newUnemploymentRateMultiplier);
            //replace worst agent that hasn't been replaced yet
            agents.set(agents.size() - i - 1, agent);
        }
        orderAgentsByFitness();
    }
}
