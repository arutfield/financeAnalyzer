import org.apache.log4j.Logger;
import org.apache.commons.cli.*;

import java.util.Iterator;

public class Main {
    final static Logger logger = Logger.getLogger(Main.class);
        public static void main(String[] args) {
            try {

                Options options = new Options();

                Option inputFileOption = new Option("i", "inputfile", true, "file with stock to analyze");
                inputFileOption.setRequired(true);
                options.addOption(inputFileOption);

                Option dowFileOption = new Option("d", "dowfile", true, "file with dow jones stock");
                dowFileOption.setRequired(false);
                options.addOption(dowFileOption);

                Option unemploymentOption = new Option("u", "unemploymentfile", true, "file with unemployment spreadsheet");
                unemploymentOption.setRequired(false);
                options.addOption(unemploymentOption);

                Option civParticipationOption = new Option("f", "civparticipationfile", true,
                        "file with civilian participation rate");
                civParticipationOption.setRequired(false);
                options.addOption(civParticipationOption);

                Option bankOption = new Option("b", "bankborrowingfile", true,
                        "file with bank borrowing rate");
                bankOption.setRequired(false);
                options.addOption(bankOption);


                Option childrenOption = new Option("c", "children", true, "number of children");
                childrenOption.setRequired(false);
                options.addOption(childrenOption);

                Option populationOption = new Option("p", "population", true, "size of population");
                populationOption.setRequired(false);
                options.addOption(populationOption);


                Option generationOption = new Option("g", "generations", true, "number of generations");
                generationOption.setRequired(false);
                options.addOption(generationOption);

                Option mutationOption = new Option("m", "mutation", true, "probability of mutation");
                mutationOption.setRequired(false);
                options.addOption(mutationOption);

                CommandLineParser parser = new DefaultParser();
                HelpFormatter formatter = new HelpFormatter();
                CommandLine cmd = null;

                try {
                    cmd = parser.parse(options, args);
                } catch (ParseException e) {
                    System.out.println(e.getMessage());
                    formatter.printHelp("", options);
                    System.exit(1);
                }

                int numberOfChildrenPerGeneration = Integer.parseInt(cmd.getOptionValue("children", "200"));
                if (numberOfChildrenPerGeneration < 0 ) {
                    String outOfRangeMessage = "Number of children of " + numberOfChildrenPerGeneration + " is out of range." +
                            " Must be at least 0. Keeping at 200";
                    logger.warn(outOfRangeMessage);
                    System.out.println(outOfRangeMessage);
                    numberOfChildrenPerGeneration = 200;
                }
                int generations = Integer.parseInt(cmd.getOptionValue("generations", "300"));
                if (generations < 0 ) {
                    String outOfRangeMessage = "Number of generations of " + generations + " is out of range." +
                            " Must be at least 0. Keeping at 300";
                    logger.warn(outOfRangeMessage);
                    System.out.println(outOfRangeMessage);
                    generations = 300;
                }

                int populationSize = Integer.parseInt(cmd.getOptionValue("population", "500"));
                if (populationSize < 0 ) {
                    String outOfRangeMessage = "Population size of " + populationSize + " is out of range." +
                            " Must be at least 0. Keeping at 500";
                    populationSize = 500;
                    logger.warn(outOfRangeMessage);
                    System.out.println(outOfRangeMessage);
                    generations = 300;
                }


                double mutationProbability = Double.parseDouble(cmd.getOptionValue("mutation", "0.1"));
                if (mutationProbability < 0 || mutationProbability > 1) {
                    String outOfRangeMessage = "Mutation probability of " + mutationProbability + " is out of range." +
                            " Must be between 0 and 1. Keeping at 0.1";
                    logger.warn(outOfRangeMessage);
                    System.out.println(outOfRangeMessage);
                    mutationProbability = 0.1;
                }
                String argumentMessage = generations + " generations chosen with " + numberOfChildrenPerGeneration
                        + " children per generation, mutation probability of " + mutationProbability;
                System.out.println(argumentMessage);
                logger.info(argumentMessage);

                if (numberOfChildrenPerGeneration > populationSize) {
                    String tooManyChildrenMessage = "Cannot have " + numberOfChildrenPerGeneration + " per generation in population size of " + populationSize;
                    System.out.println(tooManyChildrenMessage);
                    logger.error(tooManyChildrenMessage);
                    System.exit(2);
                }
                StringBuilder fullInfo = new StringBuilder("All options set: \n");
                for (Iterator<Option> it = cmd.iterator(); it.hasNext(); ) {
                    Option option = it.next();
                    fullInfo.append("arg name: ").append(option.getLongOpt()).append(": ").append(option.getValue());
                    fullInfo.append("\n");
                }
                System.out.println(fullInfo);
                logger.info(fullInfo);
                Population population = new Population(cmd.getOptionValue("inputfile"),
                        cmd.getOptionValue("dowfile", "DJI.csv"),
                        cmd.getOptionValue("unemploymentfile", "UNRATE.csv"),
                        cmd.getOptionValue("civparticipationfile","CIVPART.csv"),
                         cmd.getOptionValue("bankfile", "bankBorrowing.csv"), populationSize);
                population.getBestOfGenerations(generations, numberOfChildrenPerGeneration, mutationProbability);
                population.printBestAgent(generations, numberOfChildrenPerGeneration, mutationProbability);

            } catch (Exception ex) {
                logger.error(ex.getMessage(), ex);
                ex.printStackTrace();
                System.exit(3);
            }

        }

}
