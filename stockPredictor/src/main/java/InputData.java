import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.*;

import javax.xml.crypto.Data;

class DataSample {
   double dowJonesClosing;
   double dowJonesClosingPercent;

    public DataSample(Double dowJonesClosing, double dowJonesClosingPercent) {
        this.dowJonesClosing = dowJonesClosing;
        this.dowJonesClosingPercent = dowJonesClosingPercent;
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

    public static void loadDowJonesClosing(String filenameDowData) {
        allDataByDateList.clear();
        BufferedReader br = null;
        String line = "";
        String cvsSplitBy = ",";

        try {

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
            linearizeGaps(dowJonesClosingMap);
            for (int i = 0; i < Collections.max(dowJonesClosingMap.keySet()); i++) {
                if (i == 0){
                    allDataByDateList.add(new DataSample(dowJonesClosingMap.get(i), 0.0 ));
                } else {
                    double previousDowValue = allDataByDateList.get(i - 1).dowJonesClosing;
                    double dowJonesClosingPercentChange = (dowJonesClosingMap.get(i) - previousDowValue) / previousDowValue * 100.0;
                    allDataByDateList.add(new DataSample(dowJonesClosingMap.get(i), dowJonesClosingPercentChange));
                }
            }
        } catch (FileNotFoundException ex) {
            logger.error("File " + filenameDowData + " not found: " + ex.getMessage());
            ex.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
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




    /**
     * offload full dow jones into excel with date as days after start
     */
    public static void printFullDowJonesClosing() {
        try (PrintWriter writer = new PrintWriter(new File("DJIScrap.csv"))) {
            int i=0;
            for (DataSample sample : allDataByDateList) {

                StringBuilder sb = new StringBuilder();
                sb.append(i);
                sb.append(',');
                sb.append(sample.dowJonesClosing);
                sb.append(',');
                sb.append(sample.dowJonesClosingPercent);
                sb.append('\n');

                writer.write(sb.toString());
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
