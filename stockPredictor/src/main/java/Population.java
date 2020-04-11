import org.apache.log4j.Logger;

import java.util.LinkedList;

public class Population {
    LinkedList<Agent> agents = new LinkedList<>();
    final static Logger logger = Logger.getLogger(Population.class);


    public Population(String dowJonesDataFile, String unemploymentDataFile, String laborRateFile,
                      String bankBorrowingFile, int numberOfAgents) throws Exception {
        if (numberOfAgents < 1) {
            String message = "need at least one agent. " + numberOfAgents + " is unusable";
            logger.error(message);
            throw new Exception(message);
        }
        InputData.loadFiles(dowJonesDataFile, unemploymentDataFile, laborRateFile, bankBorrowingFile);

        //add zero weight agent
        Weight zeroWeightArray[] = new Weight[Agent.WEIGHT_SIZE];
        byte zeroByte = 0;
        for (int j=0; j<zeroWeightArray.length; j++) {
            zeroWeightArray[j] = new Weight(true, zeroByte, zeroByte,zeroByte, zeroByte, zeroByte, zeroByte, zeroByte, zeroByte);
        }
        agents.add(new Agent(zeroWeightArray));
        if (numberOfAgents > 1) {
            agents.add(new Agent(zeroWeightArray));
        }
        for (int i = 2; i < numberOfAgents; i++) {
            Weight weights[] = new Weight[Agent.WEIGHT_SIZE];
            String weightMessage = "agent created with weights ";
            for (int j=0; j<weights.length; j++) {
                weights[j] = new Weight();
                weightMessage += ", " + weights[j].findValue();
            }
            Agent agent = new Agent(weights);
            agents.add(agent);
            logger.trace(weightMessage);
        }
        orderAgentsByFitness();
    }

    /**
     * replaces agent at end of list and reorders
     * @param agent to add
     */
    public void addAgent(Agent agent){
        agents.add(agents.size()-1, agent);
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
     * runs genetic algorithm until fitness function reaches goal value
     * @param goalValue value to reach
     * @param numberOfChildren per generation
     * @param maximumNumberOfGenerations maximum allowed generations before bailing out
     * @return best agent at end
     * @throws Exception error
     */
    public Agent getGoalFitnessFunction(double goalValue, int numberOfChildren, int maximumNumberOfGenerations) throws Exception {

        for (int i=0; i<maximumNumberOfGenerations; i++) {

            logger.debug("generation: " + i);
            generateChildren(numberOfChildren);
            if (findBestAgent().getFitnessValueDowPrediction() < goalValue) {
                return findBestAgent();

            }
        }
        logger.warn("unable to reach goal of cost " + goalValue + " in " + maximumNumberOfGenerations + ". Returning best");
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
                + agents.get(0).getLastUnemploymentRateMultiplier().findValue() + ", "
                + agents.get(0).getLastUnemploymentRatePercentChangeMultiplier().findValue() + ", "
                + agents.get(0).getLastCivilianParticipationRateMultiplier().findValue() + ", "
                + agents.get(0).getLastCivilianParticipationRateChangeMultiplier().findValue() + ", "
                + agents.get(0).getBorrowedMoneyMultiplier().findValue() + ", "
                + agents.get(0).getBorrowedMoneyRateChangeMultiplier().findValue() + ", "
                + agents.get(0).getOffset().findValue() + ", with value: "
                + agents.get(0).getFitnessValueDowPrediction());
    }

    /**
     * gets the worst X agents
     * @param numberOfAgents number of agents to return
     * @return list of the X worst agents from worst to best
     * @throws Exception error
     */
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


    /**
     * generates the children for the next generation
     * @param numberOfChildren number of children to generate
     * @throws Exception error
     */
    public void generateChildren(int numberOfChildren) throws Exception {
        Agent[] parents = findFittestPair();
        double mutationProbability = 0.1;

        for (int i = 0; i < numberOfChildren; i++) {
            int[] crossPoints = new int[Agent.WEIGHT_SIZE];
            for (int j =0; j<crossPoints.length; j++) {
                crossPoints[j] = (int) Math.floor(Math.random() * (Weight.NUM_DIGITS + 1));
            }

            Weight newDowMultiplier = new Weight(parents[0].getLastDowClosingMultiplier(),
                        parents[1].getLastDowClosingMultiplier(), crossPoints[0], mutationProbability);
                Weight newDowPercentMultiplier = new Weight(parents[0].getLastDowClosingPercentMultiplier(),
                        parents[1].getLastDowClosingPercentMultiplier(), crossPoints[1], mutationProbability);
                Weight newUnemploymentRateMultiplier = new Weight(parents[0].getLastUnemploymentRateMultiplier(),
                        parents[1].getLastUnemploymentRateMultiplier(), crossPoints[2], mutationProbability);
            Weight newUnemploymentRatePercentChangeMultiplier = new Weight(parents[0].getLastUnemploymentRatePercentChangeMultiplier(),
                    parents[1].getLastUnemploymentRatePercentChangeMultiplier(), crossPoints[3], mutationProbability);
            Weight newCivilianRateMultiplier = new Weight(parents[0].getLastCivilianParticipationRateMultiplier(),
                    parents[1].getLastCivilianParticipationRateMultiplier(), crossPoints[4], mutationProbability);
            Weight newCivilianRatePercentChangeMultiplier = new Weight(parents[0].getLastCivilianParticipationRateChangeMultiplier(),
                    parents[1].getLastCivilianParticipationRateChangeMultiplier(), crossPoints[5], mutationProbability);
            Weight newBorrowedMultiplier = new Weight(parents[0].getBorrowedMoneyMultiplier(),
                    parents[1].getBorrowedMoneyMultiplier(), crossPoints[6], mutationProbability);
            Weight newBorrowedPercentChangeMultiplier = new Weight(parents[0].getBorrowedMoneyRateChangeMultiplier(),
                    parents[1].getBorrowedMoneyRateChangeMultiplier(), crossPoints[7], mutationProbability);
            Weight newOffset = new Weight(parents[0].getOffset(),
                    parents[1].getOffset(), crossPoints[8], mutationProbability);
            Weight[] weights = {newDowMultiplier, newDowPercentMultiplier, newUnemploymentRateMultiplier,
                    newUnemploymentRatePercentChangeMultiplier, newCivilianRateMultiplier,
                    newCivilianRatePercentChangeMultiplier, newBorrowedMultiplier, newBorrowedPercentChangeMultiplier,
                    newOffset};

            Agent agent = new Agent(weights);
            //replace worst agent that hasn't been replaced yet
            agents.set(agents.size() - i - 1, agent);
        }
        orderAgentsByFitness();
    }
}
