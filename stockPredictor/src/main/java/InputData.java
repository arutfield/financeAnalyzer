import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.*;

class DataSample {
    Date date;
   double dowJonesClosing;
   double dowJonesClosingPercent;
   double unemploymentRate;
   double unemploymentRatePercentChange;
   double civilianParticipationRate;
   double civilianParticipationRatePercentChange;
    public DataSample(Date date, Double dowJonesClosing, double dowJonesClosingPercent, double unemploymentRate,
                      double unemploymentRatePercentChange, double civilianParticipationRate,
                      double civilianParticipationRatePercentChange) {
        this.date = date;
        this.dowJonesClosing = dowJonesClosing;
        this.dowJonesClosingPercent = dowJonesClosingPercent;
        this.unemploymentRate = unemploymentRate;
        this.unemploymentRatePercentChange = unemploymentRatePercentChange;
        this.civilianParticipationRate = civilianParticipationRate;
        this.civilianParticipationRatePercentChange = civilianParticipationRatePercentChange;
    }
}


/**
 * Input data class. Stores all the known information that will be used
 */
public class InputData {
    private static Date startDate;
    private final static Logger logger = Logger.getLogger(InputData.class);
//    private static LinkedList<Double> dowJonesClosingList = new LinkedList<>();
 //   private static Map<Integer, Double> dowJonesClosingMapChangePercent = new HashMap<>();
    private static LinkedList<DataSample> allDataByDateList = new LinkedList<>();
    /**
     * constructor
     */
    public InputData() {
    }

    public static void loadFiles(String filenameDowData, String unemploymentDataFileName, String civilianParticipationRateFileName) {
        allDataByDateList.clear();
        BufferedReader br = null;
        BufferedReader brUE = null;
        BufferedReader brCv = null;
        String line = "";
        String cvsSplitBy = ",";

        try {

            //read in dow data

            br = new BufferedReader(new FileReader(filenameDowData));
            int dateDifference = -1;
            Map<Integer, Double> dowJonesClosingMap = new HashMap<Integer, Double>();
            while ((line = br.readLine()) != null) {

                // use comma as separator
                String[] data = line.split(cvsSplitBy);
                String dateOfStock = data[0];
                try {
                    int prevDateDifference = dateDifference;
                    Date date1 = new SimpleDateFormat("yyyy-MM-dd").parse(dateOfStock);
                    if (startDate == null) {
                        startDate = new Date(date1.getTime() - DateUtil.DAY_MILLISECONDS / 12);
                        logger.debug("start date of dow - " + startDate.toString());

                    }
                    dateDifference = (int) ((date1.getTime() - startDate.getTime()) / (1000.0 * 60 * 60 * 24));
                    dowJonesClosingMap.put(dateDifference, Double.valueOf(data[4]));
                    logger.trace("added day " + dateDifference + " for " + date1.toString() + ", "
                            + dowJonesClosingMap.get(dateDifference)
                            + " to map");
                    int dateRateOfChange = dateDifference - prevDateDifference;
                    if ((dateRateOfChange > 4) || (dateRateOfChange == 2) || dateRateOfChange < 1) {
                        logger.warn("unusual date difference gap of " + dateRateOfChange);
                    }
                } catch (ParseException ex) {
                    continue;
                }


            }


            //read in unemployment data

            brUE = new BufferedReader(new FileReader(unemploymentDataFileName));
            dateDifference = -1;
            Map<Integer, Double> unemploymentMap = new HashMap<Integer, Double>();
            while ((line = brUE.readLine()) != null) {

                // use comma as separator
                String[] data = line.split(cvsSplitBy);
                String dateOfUnemployment = data[0];
                try {
                    int prevDateDifference = dateDifference;
                    Date date1 = new SimpleDateFormat("yyyy-MM-dd").parse(dateOfUnemployment);

                    dateDifference = (int) ((date1.getTime() - startDate.getTime()) / (1000.0 * 60 * 60 * 24));
                    unemploymentMap.put(dateDifference, Double.valueOf(data[1]));
                    logger.trace("added unemployment " + dateDifference + " for " + date1.toString() + ", "
                            + unemploymentMap.get(dateDifference)
                            + " to map");
                    int dateRateOfChange = dateDifference - prevDateDifference;
                } catch (ParseException ex) {
                    continue;
                }
            }


            //read in civilian rate

            brCv = new BufferedReader(new FileReader(civilianParticipationRateFileName));
            dateDifference = -1;
            Map<Integer, Double> civilianParticipationMap = new HashMap<Integer, Double>();
            while ((line = brCv.readLine()) != null) {

                // use comma as separator
                String[] data = line.split(cvsSplitBy);
                String dateOfUnemployment = data[0];
                try {
                    int prevDateDifference = dateDifference;
                    Date date1 = new SimpleDateFormat("MM/dd/yyyy").parse(dateOfUnemployment);

                    dateDifference = (int) ((date1.getTime() - startDate.getTime()) / (1000.0 * 60 * 60 * 24));
                    civilianParticipationMap.put(dateDifference, Double.valueOf(data[1]));
                    logger.trace("added civilian participation " + dateDifference + " for " + date1.toString() + ", "
                            + civilianParticipationMap.get(dateDifference)
                            + " to map");
                    int dateRateOfChange = dateDifference - prevDateDifference;
                } catch (ParseException ex) {
                    continue;
                }
            }


            linearizeGaps(dowJonesClosingMap);
            fillMapBasedOnPrevious(unemploymentMap, Collections.max(dowJonesClosingMap.keySet()));
            fillMapBasedOnPrevious(civilianParticipationMap, Collections.max(dowJonesClosingMap.keySet()));
            for (int i = 0; i < Collections.max(dowJonesClosingMap.keySet()); i++) {
                Calendar cal = Calendar.getInstance();
                cal.setTime(startDate);
                cal.add(Calendar.DAY_OF_MONTH, i+1);
                if (i == 0){
                    allDataByDateList.add(new DataSample(cal.getTime(), dowJonesClosingMap.get(i), 0.0,
                            unemploymentMap.get(i), 0.0, civilianParticipationMap.get(i), 0.0 ));
                } else {
                    double previousDowValue = allDataByDateList.get(i - 1).dowJonesClosing;
                    double previousUnemploymentRateValue = allDataByDateList.get(i - 1).unemploymentRate;
                    double previousCivilianParticipationRateValue = allDataByDateList.get(i - 1).civilianParticipationRate;
                    double dowJonesClosingPercentChange = (dowJonesClosingMap.get(i) - previousDowValue) / previousDowValue * 100.0;
                    double unemploymentRatePercentChange = (unemploymentMap.get(i) - previousUnemploymentRateValue) / previousUnemploymentRateValue * 100.0;
                    double civilianParticipationRateChange = (civilianParticipationMap.get(i) - previousCivilianParticipationRateValue)
                            / previousCivilianParticipationRateValue * 100.0;
                    allDataByDateList.add(new DataSample(cal.getTime(), dowJonesClosingMap.get(i), dowJonesClosingPercentChange,
                            unemploymentMap.get(i), unemploymentRatePercentChange, civilianParticipationMap.get(i),
                            civilianParticipationRateChange));
                }
            }
        } catch (FileNotFoundException ex) {
            logger.error("File " + filenameDowData + " not found: " + ex.getMessage());
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }






    }



    private static void linearizeGaps(Map<Integer, Double> map) {
        int lastKnownKey = 0;
        for (int i = 1; i < Collections.max(map.keySet()); i++) {
            if (map.containsKey(i)) {
                lastKnownKey = i;
                continue;
            }
            int j = 0;
            while (!map.containsKey(i + j)) {
                j++;
            }
            int daysBetween = j + 1;
            double slope = (map.get(i + j) - map.get(lastKnownKey)) / daysBetween;
            map.put(i, slope * (i - lastKnownKey) + map.get(lastKnownKey));
            logger.trace("added day " + i + " with value " + map.get(i + j));
            lastKnownKey = i;
        }
    }

   private static void fillMapBasedOnPrevious(Map<Integer, Double> map, int dowJonesMapSize) throws Exception {
       int lastKnownKey = 0;
       for (int i = 0; i < dowJonesMapSize; i++) {
           if (map.containsKey(i)) {
               lastKnownKey = i;
               continue;
           }
           int j = 0;
           while (!map.containsKey(i - j)) {
               j++;
               if (j > 31) {
                   String message = "too many days back. Shouldn't be > 31";
                   logger.error(message);
                   throw new Exception(message);
               }
           }
           map.put(i, map.get(i-j));
           logger.trace("added unemployment day " + i + " with value " + map.get(i - j));
           lastKnownKey = i;
       }
   }


    /**
     * offload full dow jones into excel with date as days after start
     */
    public static void printFullDowJonesClosing() {
        try (PrintWriter writer = new PrintWriter(new File("allData.csv"))) {
            int i=0;
            StringBuilder headSb = new StringBuilder();
            headSb.append("date, DJ close, DJ % change, Unemployment, Unemployment % change, civilian rate, civilian % change\n");
            writer.write(headSb.toString());
            for (DataSample sample : allDataByDateList) {

                StringBuilder sb = new StringBuilder();
                sb.append(sample.date.toString());
                sb.append(',');
                sb.append(sample.dowJonesClosing);
                sb.append(',');
                sb.append(sample.dowJonesClosingPercent);
                sb.append(',');
                sb.append(sample.unemploymentRate);
                sb.append(',');
                sb.append(sample.unemploymentRatePercentChange);
                sb.append(',');
                sb.append(sample.civilianParticipationRate);
                sb.append(',');
                sb.append(sample.civilianParticipationRatePercentChange);
                sb.append('\n');

                writer.write(sb.toString());
                i++;
            }
            logger.info("done printing map to file");

        } catch (FileNotFoundException ex) {
            logger.error(ex.getMessage(), ex);
        }
    }

    public static LinkedList<DataSample> getAllDataList() {
        return allDataByDateList;
    }

}
