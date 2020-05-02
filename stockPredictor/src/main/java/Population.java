import org.apache.log4j.Logger;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;

public class Population {
    LinkedList<Agent> agents = new LinkedList<>();
    final static Logger logger = Logger.getLogger(Population.class);


    public Population(String currentStockFile, String dowJonesDataFile, String unemploymentDataFile, String laborRateFile,
                      String bankBorrowingFile, int numberOfAgents) throws Exception {
        if (numberOfAgents < 1) {
            String message = "need at least one agent. " + numberOfAgents + " is unusable";
            logger.error(message);
            throw new Exception(message);
        }
        InputData.loadFiles(currentStockFile, dowJonesDataFile, unemploymentDataFile, laborRateFile, bankBorrowingFile);

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
        return getBestAgent();
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
            if (getBestAgent().getFitnessValue() < goalValue) {
                return getBestAgent();

            }
        }
        logger.warn("unable to reach goal of cost " + goalValue + " in " + maximumNumberOfGenerations + ". Returning best");
        return getBestAgent();
    }

    /**
     * find the agent with the smallest fitness function
     *
     * @return best agent
     */
    public Agent getBestAgent() {
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
                if (agent.getFitnessValue() < orderedAgent.getFitnessValue()) {
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
        StringBuilder message = new StringBuilder("best agent in reordering: ");
        for (Agent.WeightNameEnum weightName : Agent.WeightNameEnum.values()){
            message.append(agents.get(0).getWeight(weightName).findValue() +", ");
        }
        logger.debug(message + ", with value: " + agents.get(0).getFitnessValue());
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

            Weight[] weights = new Weight[Agent.WeightNameEnum.values().length];
            int j=0;
            for (Agent.WeightNameEnum weightNameEnum : Agent.WeightNameEnum.values()) {
                weights[j] = new Weight(parents[0].getWeight(weightNameEnum), parents[1].getWeight(weightNameEnum),
                        crossPoints[j], mutationProbability);
                j++;
            }

            Agent agent = new Agent(weights);
            //replace worst agent that hasn't been replaced yet
            agents.set(agents.size() - i - 1, agent);
        }
        orderAgentsByFitness();
    }

    public void printBestAgent(int generations, int numberOfChildren) {
        logger.info("printing best agent to file");
        String filenameFull = InputData.getCurrentStockFileName();
        int endIndex = filenameFull.indexOf(".");
        String filename;
        if (endIndex != -1)
        {
            filename = filenameFull.substring(0 , endIndex);
        } else {
            filename = filenameFull;
        }


        try (PrintWriter writer = new PrintWriter(new File(filename + "_Analysis.csv"))) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(InputData.getStartDate());
            cal.add(Calendar.DATE, 1);
            Date roundedStart = cal.getTime();
            SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
            StringBuilder headSb = new StringBuilder();
            headSb.append("Result of analyzing stock " + filename + " with " + generations + " generations and " + numberOfChildren + " per generation\n");
            headSb.append("Data started on " + formatter.format(roundedStart) + " and finished on " + formatter.format(InputData.getEndDate()) + "\n");
            writer.write(headSb.toString());

            //heading of weights
            StringBuilder weightNameSb = new StringBuilder();
            for (Agent.WeightNameEnum weightNameEnum : Agent.WeightNameEnum.values()) {
                weightNameSb.append(weightNameEnum.toString());
                if (weightNameEnum.ordinal() < Agent.WeightNameEnum.values().length - 1) {
                    weightNameSb.append(", ");
                }
            }
            weightNameSb.append("\n");
            writer.write(weightNameSb.toString());

            StringBuilder weightValuesSb = new StringBuilder();
            Agent bestAgent = getBestAgent();
            for (Agent.WeightNameEnum weightNameEnum : Agent.WeightNameEnum.values()) {
                weightValuesSb.append(bestAgent.getWeight(weightNameEnum).findValue());
                if (weightNameEnum.ordinal() < Agent.WeightNameEnum.values().length - 1) {
                    weightValuesSb.append(", ");
                }
            }
            weightValuesSb.append("\n");
            writer.write(weightValuesSb.toString());

            StringBuilder footerSb = new StringBuilder();
            Date currentDate = new Date();
            footerSb.append("Analysis completed on " + currentDate.toString());
            writer.write(footerSb.toString());
        } catch (FileNotFoundException ex) {
            logger.error(ex.getMessage(), ex);
        }

    }

}
