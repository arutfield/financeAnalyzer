import org.apache.log4j.Logger;

public class Main {
    final static Logger logger = Logger.getLogger(Main.class);
        public static void main(String[] args) {
            try {
                int numberOfChildrenPerGeneration = 100;
                int generations = 200;
                Population population = new Population("target\\classes\\FDN.csv","target\\classes\\DJI.csv", "target\\classes\\UNRATE.csv",
                        "target\\classes\\CIVPART.csv", "target\\classes\\bankBorrowing.csv", 500);
                population.getBestOfGenerations(generations, numberOfChildrenPerGeneration);
                population.printBestAgent(generations, numberOfChildrenPerGeneration);

            } catch (Exception ex) {
                logger.error(ex.getMessage(), ex);
                ex.printStackTrace();
            }

        }

}
